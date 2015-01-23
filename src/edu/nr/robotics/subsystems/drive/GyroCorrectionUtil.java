package edu.nr.robotics.subsystems.drive;

public class GyroCorrectionUtil 
{
	private boolean initialized = false;
	private double initialAngle;
	
	private static final double ANGLE_CORRECTION_INTENSITY = 0.02, MAX_ANGLE_CORRECTION_SPEED = 0.2;
	
	public GyroCorrectionUtil()
	{
		
	}
	
	public double getTurnValue()
	{
		if(!initialized)
		{
			reset();
			initialized = true;
		}
		
		//Determine turn intensity based off of angle error
    	double currentGyroAngle = Drive.getInstance().getAngle();
    	double turn = (currentGyroAngle-initialAngle)* ANGLE_CORRECTION_INTENSITY;
    	if(turn<0)
    		turn = Math.max(-MAX_ANGLE_CORRECTION_SPEED, turn);
    	else
    		turn = Math.min(MAX_ANGLE_CORRECTION_SPEED, turn);
    	
    	return turn;
	}
	
	/**
	 * Causes the initial angle value to be reset the next time getTurnValue() is called. Use this in the end() and interrupted()
	 * functions of commands to make sure when the commands are restarted, the initial angle value is reset.
	 */
	public void stop()
	{
		initialized = false;
	}
	
	public void reset()
	{
		initialAngle = Drive.getInstance().getAngle();
	}
}
