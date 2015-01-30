package edu.nr.robotics.subsystems.drive.gyro;

import edu.nr.robotics.Fieldcentric;

/**
 * Uses the fieldcentric position and angle values to make sure the robot turns to the correct angle to arrive at its destination
 */
public class PositionGyroCorrectionUtil// extends GyroCorrection
{
	/*private double initialX, initialY, initialAngleRadians;
	private double targetDistanceY, targetDistanceX;
	
	public PositionGyroCorrectionUtil(double targetDistanceY, double targetDistanceX)
	{
		this.targetDistanceY = targetDistanceY;
		this.targetDistanceX = targetDistanceX;
	}
	
	@Override
	protected double getAngleError() 
	{
		double fieldDeltaX = (Fieldcentric.getX() - initialX);
		double fieldDeltaY = (Fieldcentric.getY() - initialY);
		
		//Apply the rotation to the coordinate system in order to get our horizontal and vertical error relative to our initial angle
		double dx = fieldDeltaX * Math.cos(-initialAngleRadians) - fieldDeltaY * Math.sin(-initialAngleRadians);
		double dy = fieldDeltaX * Math.sin(-initialAngleRadians) + fieldDeltaY * Math.cos(-initialAngleRadians);
		
		dx = targetDistanceX - dx;
		dy = targetDistanceY - dy;
		
		double targetAngle = Math.atan2(dy, dx);
		
		return initialAngleRadians - targetAngle;
	}

	@Override
	public void reset() 
	{
		initialX = Fieldcentric.getX();
		initialY = Fieldcentric.getY();
		initialAngleRadians = Fieldcentric.getFieldCentricAngleRadians();
	}*/
}
