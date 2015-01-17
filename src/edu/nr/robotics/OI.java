package edu.nr.robotics;

import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	
	private static OI singleton;
	
	Joystick stick = new Joystick(RobotMap.joystick);
	
	
	
	private OI()
	{

		
	}

	public static OI getInstance()
	{
		if(singleton == null)
            singleton = new OI();
		
		return singleton;
		
	}
	
	public double getJoyX1()
	{
		return -stick.getRawAxis(0);
	}
	
	public double getJoyY1()
	{
		return -stick.getRawAxis(1);
	}
	
	public double getJoyX2()
	{
		return -stick.getRawAxis(4);
	}
	
	public double getJoyY2()
	{
		return -stick.getRawAxis(5);
	}
	
	public double getJoyZ1()
	{
		return stick.getRawAxis(2);
	}
	
	public double getJoyZ2()
	{
		return stick.getRawAxis(3);
	}
	
	public boolean getButton(int buttonNumber)
	{
		return stick.getRawButton(buttonNumber);
	}
}

