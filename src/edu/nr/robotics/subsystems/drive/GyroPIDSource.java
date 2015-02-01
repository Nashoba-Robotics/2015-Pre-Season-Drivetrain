package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDSource;

public class GyroPIDSource implements PIDSource
{
	@Override
	public double pidGet() 
	{
		return Drive.getInstance().getAngleRadians();
	}
}
