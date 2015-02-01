package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;

public class RotationPIDOutput implements PIDOutput
{
	@Override
	public void pidWrite(double output) 
	{
		Drive.getInstance().setRawMotorSpeed(-output, output);
	}
}
