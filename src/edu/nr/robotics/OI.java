package org.usfirst.frc.team1768.robot;



import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
	Joystick joy0;
	
	public OI()
	{
		joy0 = new Joystick(0);
	}
	
	public double getX()
	{
		return joy0.getAxis(AxisType.kX);	
	}
	
	public double getY()
	{
		return joy0.getAxis(AxisType.kY);
	}
	
	public double getZ()
	{
		return joy0.getAxis(AxisType.kZ);
	}
}

