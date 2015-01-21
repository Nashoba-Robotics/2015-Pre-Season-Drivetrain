
package edu.nr.robotics.subsystems;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;

/**
 *
 */
public class Drive extends Subsystem 
{	
	private RobotDrive robotDrive;
	
	private DoubleSolenoid solenoid;

	private static Drive singleton;
	
	private AnalogInput IRSensor;
	
	private Gyro gyro;
	
	private Encoder leftEnc, rightEnc;
	private double ticksPerRev = 256, wheelDiameter = 0.4975;
	
	private DigitalInput bumper1, bumper2;
	
	private I2C ultrasonic;
	
	private PIDController leftPid, rightPid;
	
	MotorPair leftMotors, rightMotors;
	
	AnalogInput IRSensor2;
	
	private Drive()
	{
		leftMotors = new MotorPair(new TalonSRX(RobotMap.leftFrontTalon), new TalonSRX(RobotMap.leftBackTalon));
		rightMotors = new MotorPair(new VictorSP(RobotMap.rightFrontVictor), new VictorSP(RobotMap.rightBackVictor));
		
		leftEnc = new Encoder(RobotMap.ENCODER1_A, RobotMap.ENCODER1_B);
		rightEnc = new Encoder(RobotMap.ENCODER2_A, RobotMap.ENCODER2_B);
		
		leftEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		rightEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		
		leftPid = new PIDController(4, 0, 0, 1, leftEnc, leftMotors);
		rightPid = new PIDController(4, 0, 0, 1, rightEnc, rightMotors);
		leftPid.enable();
		rightPid.enable();
		
		SmartDashboard.putData("Left Side PID", leftPid);
		SmartDashboard.putData("Right Side PID", rightPid);
		
		solenoid = new DoubleSolenoid(RobotMap.pneumaticsModule, 
									  RobotMap.doubleSolenoidForward, 
									  RobotMap.doubleSolenoidReverse);
		
		IRSensor = new AnalogInput(RobotMap.IRSensor);
		
		IRSensor2 = new AnalogInput(RobotMap.IRSensor2);
		
		gyro = new Gyro(RobotMap.gyro);
		
		double distancePerPulse = (1 / ticksPerRev) * Math.PI * wheelDiameter;
		leftEnc.setDistancePerPulse(distancePerPulse/20);
		rightEnc.setDistancePerPulse(distancePerPulse/20);
		
		bumper1 = new DigitalInput(RobotMap.BUMPER_BUTTON_1);
		bumper2 = new DigitalInput(RobotMap.BUMPER_BUTTON_2);
		
		ultrasonic = new I2C(Port.kOnboard, 112);
		
		SmartDashboard.putData(new ResetEncoderCommand());
        SmartDashboard.putNumber("Forward Speed", 0);
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
	
	public void initDefaultCommand()
	{
        setDefaultCommand(new DriveJoystickCommand());
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
        
        SmartDashboard.putNumber("Left Motor Setpoint", leftMotorSpeed);
        SmartDashboard.putNumber("Right Motor Setpoint", rightMotorSpeed);
        
    	leftPid.setSetpoint(leftMotorSpeed);
        rightPid.setSetpoint(rightMotorSpeed);
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
	
	public void solenoidForward(){
		solenoid.set(Value.kForward);
	}
	
	public void solenoidOff(){
		solenoid.set(Value.kOff);
	}
	
	public void solenoidReverse(){
		solenoid.set(Value.kReverse);
	}

	public double getIRDistance() 
	{
		return IRSensor.getVoltage();
	}

	public double getAngle() 
	{
		return gyro.getAngle();
	}
	
	public double getEncoderAve()
	{
		return (leftEnc.getDistance() + rightEnc.getDistance()) / 2f;
	}
	
	public void resetEncoders()
	{
		leftEnc.reset();
		rightEnc.reset();
	}
	
	public double getEncoder1Distance()
	{
		return leftEnc.getDistance();
	}
	
	public double getEncoder2Distance()
	{
		return -rightEnc.getDistance();
	}
	
	public double getLeftEncoderSpeed()
	{
		return leftEnc.getRate();
	}
	
	public double getRightEncoderSpeed()
	{
		return rightEnc.getRate();
	}
	
	public boolean getBumper1()
	{
		return bumper1.get();
	}
	
	public boolean getBumper2()
	{
		return bumper2.get();
	}
	
	/**
	 * Gets the distance value from the ultrasonic sensor and send a request for a new reading.
	 * @return The distance in centimeters
	 */
	public int getUltrasonicValue()
	{
		//Get the last reading from the device
		byte[] result = new byte[2];
		ultrasonic.readOnly(result, 2);
		
		//Send a new read command to the device
		ultrasonic.writeBulk(new byte[] { 81 } );
		
		return ((result[0] & 0xff) << 8) | (result[1] & 0xff);
	}
	
	public void sendEncoderInfo()
	{
		SmartDashboard.putNumber("Encoder 1", getEncoder1Distance());
		SmartDashboard.putNumber("Encoder 2", getEncoder2Distance());
		SmartDashboard.putNumber("Encoder Average", getEncoderAve());
		
		SmartDashboard.putNumber("Right Encoder Rate", this.getRightEncoderSpeed());
		SmartDashboard.putNumber("Left Encoder Rate", this.getLeftEncoderSpeed());
		SmartDashboard.putNumber("IR 2 Voltage", IRSensor2.getVoltage());
	}
}

