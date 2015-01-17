
package edu.nr.robotics.subsystems;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
	
	private Talon leftFront;
	private Talon leftBack;
	private Talon rightFront;
	private Talon rightBack;
	private RobotDrive robotDrive;
	
	private DoubleSolenoid solenoid;

	private static DriveSubsystem singleton;
	
	private AnalogInput IRSensor;
	
	private Gyro gyro;
	
	private Encoder enc1, enc2;
	private double ticksPerRev = 250, wheelDiameter = 0.5;
	
	private DriveSubsystem()
	{
		leftFront = new Talon(RobotMap.leftFront);
		leftBack = new Talon(RobotMap.leftBack);
		rightFront = new Talon(RobotMap.rightFront);
		rightBack = new Talon(RobotMap.rightBack);
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
	}
	
	public static DriveSubsystem getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
            singleton = new DriveSubsystem();
	}
	
	public void initDefaultCommand() {
        setDefaultCommand(new DriveJoystickCommand());
    }

	public void drive(double outputMagnitude, double curve) {
		robotDrive.arcadeDrive(outputMagnitude, curve);
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
		return enc2.getDistance();
	}
	
	public void sendEncoderInfo()
	{
		SmartDashboard.putNumber("Encoder 1", getEncoder1Distance());
		SmartDashboard.putNumber("Encoder 2", getEncoder2Distance());
		SmartDashboard.putNumber("Encoder Average", getEncoderAve());
	}
}

