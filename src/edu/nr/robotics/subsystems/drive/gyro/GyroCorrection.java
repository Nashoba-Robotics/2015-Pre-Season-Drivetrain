package edu.nr.robotics.subsystems.drive.gyro;

public abstract class GyroCorrection
{
	private static final double ANGLE_CORRECTION_INTENSITY = 0.02, MAX_ANGLE_CORRECTION_SPEED = 0.2;
	private boolean initialized = false;
	
	public double getTurnValue()
	{
		if(initialized == false)
		{
			reset();
			initialized = true;
		}
		
		double turn = getAngleError() * ANGLE_CORRECTION_INTENSITY;
    	if(turn<0)
    		turn = Math.max(-MAX_ANGLE_CORRECTION_SPEED, turn);
    	else
    		turn = Math.min(MAX_ANGLE_CORRECTION_SPEED, turn);
    	
    	return turn;
	}
	
	protected abstract double getAngleError();
	public abstract void reset();
	
	/**
	 * Causes the initial angle value to be reset the next time getTurnValue() is called. Use this in the end() and interrupted()
	 * functions of commands to make sure when the commands are restarted, the initial angle value is reset.
	 */
	public void clearInitialValue()
	{
		initialized = false;
	}
}
