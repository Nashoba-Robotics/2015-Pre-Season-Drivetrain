package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroPIDSource implements PIDSource
{
	@Override
	public double pidGet() 
	{
		double angle = Drive.getInstance().getAngleRadians();
		SmartDashboard.putNumber("Gyro Pid Source", angle);
		return angle;
	}
}
