package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

public class MotorPair implements PIDOutput
{
	private SpeedController first, second;
	
	public MotorPair(SpeedController first, SpeedController second)
	{
		this.first = first;
		this.second = second;
	}
	
	@Override
	public void pidWrite(double output) 
	{
		first.set(output);
		second.set(output);
	}
	
	public void set(double output)
	{
		//Same thing
		pidWrite(output);
	}
}
