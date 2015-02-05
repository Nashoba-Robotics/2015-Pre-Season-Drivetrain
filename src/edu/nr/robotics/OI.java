package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;
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
		stickArcade = new Joystick(0);
		stickTankLeft = new Joystick(2);
		stickTankRight = new Joystick(3);
		
		Joystick buttonAssignmentStick;
		if(USING_ARCADE)
		{
			buttonAssignmentStick = stickArcade;
		}
		else
		{
			buttonAssignmentStick = stickTankLeft;
		}
		
		/* Update this whenever a button is used. Don't use one of these buttons.
		 * Used buttons: 10,12
		 */
		new JoystickButton(buttonAssignmentStick, 3).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				Drive.getInstance().startLaserPolling();
			}
		});
		new JoystickButton(buttonAssignmentStick, 4).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				Drive.getInstance().stopLaserPolling();
			}
		});
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

