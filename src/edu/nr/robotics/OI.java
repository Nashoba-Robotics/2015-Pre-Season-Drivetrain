package edu.nr.robotics;

import edu.nr.robotics.subsystems.pneumatics.SolenoidForwardCommand;
import edu.nr.robotics.subsystems.pneumatics.SolenoidOffCommand;
import edu.nr.robotics.subsystems.pneumatics.SolenoidReverseCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	public static final boolean USING_XBOX = true;
	public static final boolean USING_ARCADE = true;
	
	private static OI singleton;
	
	Joystick xBox;
	Joystick stickTankLeft;
	Joystick stickTankRight;
	Joystick stickArcade;
	//xBox goes in 0, joystick for Arcade goes in 1, left joystick for tank goes in 2, right joystick for tank goes in 3
	
	private OI()
	{


		
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
			xBox = new Joystick(0);
			new JoystickButton(xBox, solenoidOff).whenPressed(new SolenoidOffCommand());
			new JoystickButton(xBox, solenoidForward).whenPressed(new SolenoidForwardCommand());
			new JoystickButton(xBox, solenoidReverse).whenPressed(new SolenoidReverseCommand());

		}
		else
		{
			/* Update this whenever a button is used. Don't use one of these buttons.
			 * Used buttons: 2,8,10,12
			 */
			solenoidOff = 8;
			solenoidForward = 10;
			solenoidReverse = 12;
			stickArcade = new Joystick(1);
			stickTankLeft = new Joystick(2);
			stickTankRight = new Joystick(3);
			if(USING_ARCADE)
			{
				new JoystickButton(stickArcade, solenoidOff).whenPressed(new SolenoidOffCommand());
				new JoystickButton(stickArcade, solenoidForward).whenPressed(new SolenoidForwardCommand());
				new JoystickButton(stickArcade, solenoidReverse).whenPressed(new SolenoidReverseCommand());
			}
			else
			{
				new JoystickButton(stickTankLeft, solenoidOff).whenPressed(new SolenoidOffCommand());
				new JoystickButton(stickTankLeft, solenoidForward).whenPressed(new SolenoidForwardCommand());
				new JoystickButton(stickTankLeft, solenoidReverse).whenPressed(new SolenoidReverseCommand());
			}

		}

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
			return -xBox.getRawAxis(1);
		}
		else
		{
			return -stickArcade.getY();
		}
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_XBOX)
		{
			return -xBox.getRawAxis(4);
		}
		else
		{
			return -stickArcade.getZ();
		}
	}

	public double getTankLeftValue()
	{
		if(USING_XBOX)
		{
			return -xBox.getRawAxis(1);
		}
		else
		{
			return -stickTankLeft.getY();
		}
	}

	public double getTankRightValue()
	{
		if(USING_XBOX)
		{
			return xBox.getRawAxis(3);
		}
		else
		{
			return stickTankRight.getY();
		}
	}
	
	public double getAmplifyValue()
	{
		if(USING_XBOX)
		{
			return xBox.getRawAxis(3);
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
			return xBox.getRawAxis(2);
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
			return 0.5;
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
			return getRawButton(6, xBox);
		}
		else
		{
			return getRawButton(2, stickArcade);
		}
	}
	
	public boolean getRawButton(int buttonNumber, Joystick stick)
	{
		return stick.getRawButton(buttonNumber);
	}
}

