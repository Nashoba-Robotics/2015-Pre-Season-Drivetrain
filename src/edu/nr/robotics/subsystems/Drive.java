
package edu.nr.robotics.subsystems;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;

/**
 *
 */
public class Drive extends Subsystem 
{
	//We use a mix of Talons and victors, so just use generic 'SpeedController' class for variable types
	private SpeedController leftFront, leftBack, rightFront, rightBack;
	
	private RobotDrive robotDrive;
	
	private DoubleSolenoid solenoid;

	private static Drive singleton;
	
	private AnalogInput IRSensor;
	
	private Gyro gyro;
	
	private Encoder enc1, enc2;
	private double ticksPerRev = 250, wheelDiameter = 0.4975;
	
	private DigitalInput bumper1, bumper2;
	
	private I2C ultrasonic;
	
	private Drive()
	{
		leftFront = new TalonSRX(RobotMap.leftFrontTalon);
		leftBack = new TalonSRX(RobotMap.leftBackTalon);
		
		rightFront = new VictorSP(RobotMap.rightFrontVictor);
		rightBack = new VictorSP(RobotMap.rightBackVictor);
		
		robotDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		robotDrive.setSafetyEnabled(false);
		
		solenoid = new DoubleSolenoid(RobotMap.pneumaticsModule, RobotMap.doubleSolenoidForward, RobotMap.doubleSolenoidReverse);
		
		IRSensor = new AnalogInput(RobotMap.IRSensor);
		
		gyro = new Gyro(RobotMap.gyro);
		
		enc1 = new Encoder(RobotMap.ENCODER1_A, RobotMap.ENCODER1_B);
		enc2 = new Encoder(RobotMap.ENCODER2_A, RobotMap.ENCODER2_B);
		
		double distancePerPulse = (1 / ticksPerRev) * Math.PI * wheelDiameter;
		enc1.setDistancePerPulse(distancePerPulse);
		enc2.setDistancePerPulse(distancePerPulse);
		
		bumper1 = new DigitalInput(RobotMap.BUMPER_BUTTON_1);
		bumper2 = new DigitalInput(RobotMap.BUMPER_BUTTON_2);
		
		ultrasonic = new I2C(Port.kOnboard, 112);
		
		SmartDashboard.putData(new ResetEncoderCommand());
	}
	
	public static Drive getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
            singleton = new Drive();
	}
	
	public void initDefaultCommand() {
        setDefaultCommand(new DriveJoystickCommand());
    }

	public void drive(double outputMagnitude, double curve) 
	{
		robotDrive.arcadeDrive(outputMagnitude, curve, false);
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
		return (enc1.getDistance() + enc2.getDistance()) / 2f;
	}
	
	public void resetEncoders()
	{
		enc1.reset();
		enc2.reset();
	}
	
	public double getEncoder1Distance()
	{
		return enc1.getDistance();
	}
	
	public double getEncoder2Distance()
	{
		return -enc2.getDistance();
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
		
		SmartDashboard.putBoolean("Bumper 1", getBumper1());
		SmartDashboard.putBoolean("Bumper 2", getBumper2());
	}
}

