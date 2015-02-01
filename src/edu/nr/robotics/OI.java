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
	public static boolean USING_ARCADE = true;
	
	private static OI singleton;
	
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
		/* Update this whenever a button is used. Don't use one of these buttons.
		 * Used buttons: 2,8,10,12
		 */
		solenoidOff = 8;
		solenoidForward = 10;
		solenoidReverse = 12;
		stickArcade = new Joystick(0);
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
		return -stickArcade.getY();
	}
	
	public double getArcadeTurnValue()
	{
		return -stickArcade.getZ() / 2;
	}

	public double getTankLeftValue()
	{
		return -stickTankLeft.getY();
	}

	public double getTankRightValue()
	{
		return stickTankRight.getY();
	}
	
	public double getAmplifyValue()
	{
		return 0;
	}
	
	public double getDecreaseValue()
	{
		return 0;
	}
	
	public double getDefaultMaxValue()
	{
		return 1;
	}
	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
			return getRawButton(2, stickArcade);
	}
	
	public boolean getRawButton(int buttonNumber, Joystick stick)
	{
		return stick.getRawButton(buttonNumber);
	}
}

