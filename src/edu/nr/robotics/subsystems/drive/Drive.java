
package edu.nr.robotics.subsystems.drive;

import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.commands.AutonomousCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickArcadeCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickTankCommand;
import edu.nr.robotics.subsystems.drive.commands.DrivePositionCommand;
import edu.nr.robotics.subsystems.drive.commands.ResetEncoderCommand;
import edu.nr.robotics.subsystems.drive.commands.ZeroNavXCommand;
import edu.nr.robotics.subsystems.drive.mxp.NavX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.VictorSP;

/**
 *
 */
public class Drive extends Subsystem 
{	
	
	private static Drive singleton;
	
	private AnalogInput IRSensor;
	
	private Encoder leftEnc, rightEnc;
	private double ticksPerRev = 256, wheelDiameter = 0.4975;
	
	private DigitalInput bumper1, bumper2;
	
	private Ultrasonic leftUltrasonic, rightUltrasonic;
	
	private PIDController leftPid, rightPid;
	
	MotorPair leftMotors, rightMotors;
	
	AnalogInput IRSensor2;
	
	//Max speed of the robot in ft/sec (used to scale down encoder values for PID) See constructor for details.
	private final double MAX_ENCODER_RATE = 20;
	CANTalon[] talons;
	
	private Drive()
	{
		talons = new CANTalon[4];
		talons[0] = new CANTalon(RobotMap.leftFrontTalon);
		talons[1] = new CANTalon(RobotMap.leftBackTalon);
		talons[2] = new CANTalon(RobotMap.rightFrontTalon);
		talons[3] = new CANTalon(RobotMap.rightBackTalon);
		
		setTalonProperties();
		
		leftMotors = new MotorPair(talons[0], talons[1]);
		rightMotors = new MotorPair(talons[2], talons[3]);
		
		leftEnc = new Encoder(RobotMap.ENCODER1_A, RobotMap.ENCODER1_B);
		rightEnc = new Encoder(RobotMap.ENCODER2_A, RobotMap.ENCODER2_B);
		
		leftEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		rightEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		
		double distancePerPulse = (1 / ticksPerRev) * Math.PI * wheelDiameter;
		
		//Max speed of robot is 20 ft/sec, so in order for our PIDController to work, the scale of encoder rate
		//in ft/sec must be on a scale of -1 to 1 (so it can be used to calculate motor output)
		leftEnc.setDistancePerPulse(distancePerPulse / MAX_ENCODER_RATE);
		rightEnc.setDistancePerPulse(distancePerPulse / MAX_ENCODER_RATE);
		
		leftPid = new PIDController(0.5, 0, 0, 1, leftEnc, leftMotors);
		rightPid = new PIDController(0.5, 0, 0, 1, rightEnc, rightMotors);
		leftPid.enable();
		rightPid.enable();
		
		SmartDashboard.putData("Left Side PID", leftPid);
		SmartDashboard.putData("Right Side PID", rightPid);
		
		IRSensor = new AnalogInput(RobotMap.IRSensor);
		
		IRSensor2 = new AnalogInput(RobotMap.IRSensor2);
		
		bumper1 = new DigitalInput(RobotMap.BUMPER_BUTTON_1);
		bumper2 = new DigitalInput(RobotMap.BUMPER_BUTTON_2);
		
		leftUltrasonic = new Ultrasonic(RobotMap.VEX_LEFT_ULTRASONIC_PING, RobotMap.VEX_LEFT_ULTRASONIC_ECHO);
		rightUltrasonic = new Ultrasonic(RobotMap.VEX_RIGHT_ULTRASONIC_PING, RobotMap.VEX_RIGHT_ULTRASONIC_ECHO);
		
		
		NavX.init();
		
		SmartDashboard.putData(new ResetEncoderCommand());
		SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData(new ZeroNavXCommand());
        SmartDashboard.putBoolean("Joystick Arcade?", OI.USING_ARCADE);
        SmartDashboard.putBoolean("Xbox?", OI.USING_XBOX);
        
        SmartDashboard.putNumber("Goal X", 0);
		SmartDashboard.putNumber("Goal Y", 0);
		SmartDashboard.putNumber("Goal Angle", 0);
		SmartDashboard.putData(new DrivePositionCommand(0, 0, 0));
	}
	
	public static Drive getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new Drive();
		}
	}
	
	public void setTalonProperties()
	{
		for(int i = 0; i < talons.length; i++)
		{
			talons[i].enableBrakeMode(true);
			talons[i].setVoltageRampRate(1);
		}
	}
	
	public void initDefaultCommand()
	{
		if(OI.USING_ARCADE)
		{
			setDefaultCommand(new DriveJoystickArcadeCommand());
		}
		else
		{
			setDefaultCommand(new DriveJoystickTankCommand());
		}
    }

	public void arcadeDrive(double moveValue, double rotateValue)
	{
		this.arcadeDrive(moveValue, rotateValue, false);
	}
	
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) 
	{
        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);

        if (squaredInputs) 
        {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        rightMotorSpeed = -rightMotorSpeed;
        
    	leftPid.setSetpoint(leftMotorSpeed);
        rightPid.setSetpoint(rightMotorSpeed);
	}
	
	public void tankDrive(double leftMotorSpeed, double rightMotorSpeed)
	{
		leftPid.setSetpoint(leftMotorSpeed);
        rightPid.setSetpoint(rightMotorSpeed);
	}
	
	public void setDriveP(double p)
	{
		leftPid.setPID(p, 0, 0, 1);
		rightPid.setPID(p, 0, 0, 1);
	}
	
	private double limit(double num)
	{
		if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
	}
	
	public double getIRDistance() 
	{
		return IRSensor.getVoltage();
	}

	public double getAngle() 
	{
		return NavX.getInstance().getYaw();
	}
	
	public double getEncoderAve()
	{
		return (getEncoder1Distance() + getEncoder2Distance()) / 2f;
	}
	
	public void resetEncoders()
	{
		leftEnc.reset();
		rightEnc.reset();
	}
	
	public double getEncoder1Distance()
	{
		return leftEnc.getDistance() * MAX_ENCODER_RATE;
	}
	
	public double getEncoder2Distance()
	{
		return -rightEnc.getDistance() * MAX_ENCODER_RATE;
	}
	
	public double getLeftEncoderSpeed()
	{
		return leftEnc.getRate() * MAX_ENCODER_RATE;
	}
	
	public double getRightEncoderSpeed()
	{
		return -rightEnc.getRate() * MAX_ENCODER_RATE;
	}
	
	public double getEncoderAverageSpeed()
	{
		return (getRightEncoderSpeed() + getLeftEncoderSpeed()) / 2;
	}
	
	public boolean getBumper1()
	{
		return !bumper1.get();
	}
	
	public boolean getBumper2()
	{
		return !bumper2.get();
	}
	
	private boolean leftUltrasonicInit = false;
	public double getLeftUltrasonicValue()
	{
		if(!leftUltrasonicInit)
		{
			leftUltrasonic.setAutomaticMode(true);
			leftUltrasonicInit = true;
		}
		return leftUltrasonic.getRangeInches();
	}
	
	private boolean rightUltrasonicInit = false;
	public double getRightUltrasonicValue()
	{
		if(!rightUltrasonicInit)
		{
			rightUltrasonic.setAutomaticMode(true);
			rightUltrasonicInit = true;
		}
		
		return rightUltrasonic.getRangeInches();
	}
	
	public void sendEncoderInfo()
	{
		SmartDashboard.putNumber("Encoder 1", getEncoder1Distance());
		SmartDashboard.putNumber("Encoder 2", getEncoder2Distance());
		SmartDashboard.putNumber("Encoder Average", getEncoderAve());
		
		SmartDashboard.putNumber("IR 2 Voltage", IRSensor2.getVoltage());
		SmartDashboard.putNumber("Velocity", getEncoderAverageSpeed());
		
		SmartDashboard.putBoolean("Button 1", this.getBumper1());
		SmartDashboard.putBoolean("Button 2", this.getBumper2());
		
		SmartDashboard.putNumber("NavX Yaw", NavX.getInstance().getYaw());
		SmartDashboard.putNumber("NavX Roll", NavX.getInstance().getRoll());
		SmartDashboard.putNumber("NavX Pitch", NavX.getInstance().getPitch());
		
		SmartDashboard.putNumber("Gyro", getAngle());
		
		double ultrasonic = getRightUltrasonicValue();
		if(ultrasonic < 225 && ultrasonic > 0)
			SmartDashboard.putNumber("Right Ultrasonic", ultrasonic);
		
		ultrasonic = getLeftUltrasonicValue();
		if(ultrasonic < 225 && ultrasonic > 0)
			SmartDashboard.putNumber("Left Ultrasonic", ultrasonic);
	}
}

