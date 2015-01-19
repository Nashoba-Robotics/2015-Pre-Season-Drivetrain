package edu.nr.robotics;

import edu.nr.robotics.commands.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	public static final boolean USING_XBOX = true;
	
	private static OI singleton;
	
	Joystick stick;
	
	private OI()
	{
		stick = new Joystick(0);
		
		//Use this space for assigning button numbers, then below the if/else statement, create the actual JoystickButtons using
		//the button numbers determined here
		int solenoidOff;
		int solenoidForward;
		int solenoidReverse;
		if(USING_XBOX)
		{
			/* Update this whenever a button is used. Don't use one of these buttons.
			 * Used buttons: 1,2,3,6
			 */
			solenoidOff = 1;
			solenoidForward = 2;
			solenoidReverse = 3;
		}
		else
		{
			/* Update this whenever a button is used. Don't use one of these buttons.
			 * Used buttons: 2,8,10,12
			 */
			solenoidOff = 8;
			solenoidForward = 10;
			solenoidReverse = 12;
		}
		new JoystickButton(stick, solenoidOff).whenPressed(new SolenoidOffCommand());
		new JoystickButton(stick, solenoidForward).whenPressed(new SolenoidForwardCommand());
		new JoystickButton(stick, solenoidReverse).whenPressed(new SolenoidReverseCommand());

		//Warning: button 2 on the Logitech stick is reserved for gyro correction while driving. Do not use.
		//button 6 on the xbox controller is reserved for gyro correction as well. See useGyroCorrection() function below.
		
	}

	public static OI getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
            singleton = new OI();
	}
	
	public double getArcadeMoveValue()
	{
		if(USING_XBOX)
		{
			return -stick.getY(Hand.kLeft);
		}
		else
		{
			return -stick.getY();
		}
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_XBOX)
		{
			return -stick.getRawAxis(4);
		}
		else
		{
			return -stick.getZ();
		}
	}
	
	public double getAmplifyValue()
	{
		if(USING_XBOX)
		{
			return stick.getRawAxis(3);
		}
		else
		{
			return 0;
		}
	}
	
	public double getDecreaseValue()
	{
		if(USING_XBOX)
		{
			return stick.getRawAxis(2);
		}
		else
		{
			return 0;
		}
	}
	
	public double getDefaultMaxValue()
	{
		if(USING_XBOX)
		{
			return 0.675;
		}
		else
		{
			return 1;
		}
	}
	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
		if(USING_XBOX)
		{
			return getRawButton(6);
		}
		else
		{
			return getRawButton(2);
		}
	}
	
	public boolean getRawButton(int buttonNumber)
	{
		return stick.getRawButton(buttonNumber);
	}
}

