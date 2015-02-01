package edu.nr.robotics.subsystems.drive.gyro;

import edu.nr.robotics.subsystems.drive.Drive;

public class AngleGyroCorrectionUtil extends GyroCorrection
{
	private double initialAngle;
	
	public double getAngleError()
	{
		//Error is just based off initial angle
    	return (Drive.getInstance().getAngleDegrees() - initialAngle);
	}
	
	public void reset()
	{
		initialAngle = Drive.getInstance().getAngleDegrees();
	}
}
