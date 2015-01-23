package edu.nr.robotics.subsystems.pneumatics;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Pneumatics extends Subsystem
{
	private DoubleSolenoid solenoid;

	private Pneumatics()
	{
		solenoid = new DoubleSolenoid(RobotMap.pneumaticsModule, 
				  RobotMap.doubleSolenoidForward, 
				  RobotMap.doubleSolenoidReverse);
	}
	
	@Override
	protected void initDefaultCommand() 
	{
		//Nothing to do here
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

	
	private static Pneumatics singleton;
	public static void init()
	{
		if(singleton == null)
			singleton = new Pneumatics();
	}
	
	public static Pneumatics getInstance()
	{
		init();
		return singleton;
	}
}
