package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotorPair implements PIDOutput
{
	private static int count = 1;
	
	private SpeedController first, second;
	private int num;
	
	public MotorPair(SpeedController first, SpeedController second)
	{
		num = count;
		count++;
		this.first = first;
		this.second = second;
	}
	
	@Override
	public void pidWrite(double output) 
	{
		first.set(output);
		second.set(output);
		SmartDashboard.putNumber("MotorPair " + num + " actual output", output);
	}
	
	public void set(double output)
	{
		//Same thing
		pidWrite(output);
	}
	
}
