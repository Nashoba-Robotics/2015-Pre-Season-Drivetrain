
package edu.nr.robotics.subsystems;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
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
	
	private DriveSubsystem()
	{
		leftFront = new Talon(RobotMap.leftFront);
		leftBack = new Talon(RobotMap.leftBack);
		rightFront = new Talon(RobotMap.rightFront);
		rightBack = new Talon(RobotMap.rightBack);
		robotDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		robotDrive.setSafetyEnabled(false);
		
		solenoid = new DoubleSolenoid(RobotMap.pneumaticsModule, RobotMap.doubleSolenoidForward, RobotMap.doubleSolenoidReverse);
		
		setIRSensor(new AnalogInput(RobotMap.IRSensor));
		
		setGyro(new Gyro(RobotMap.gyro));

	}
	
	public static DriveSubsystem getInstance()
    {
		if(singleton == null)
            singleton = new DriveSubsystem();
        
        return singleton;
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

	/**
	 * @return the IRSensor
	 */
	public AnalogInput getIRSensor() {
		return IRSensor;
	}

	/**
	 * @param IRSensor the IRSensor to set
	 */
	public void setIRSensor(AnalogInput IRSensor) {
		this.IRSensor = IRSensor;
	}

	/**
	 * @return the gyro
	 */
	public Gyro getGyro() {
		return gyro;
	}

	/**
	 * @param gyro the gyro to set
	 */
	public void setGyro(Gyro gyro) {
		this.gyro = gyro;
	}
}

