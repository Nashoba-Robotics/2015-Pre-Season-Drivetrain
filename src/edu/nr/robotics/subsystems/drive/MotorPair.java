package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		if(output  < 0)
			output = Math.max(-0.7, output);
		else
			output = Math.min(0.7, output);
		
		first.set(output);
		second.set(output);
		SmartDashboard.putNumber("MotorPair", output);
	}
	
	public void set(double output)
	{
		//Same thing
		pidWrite(output);
	}
	
}
