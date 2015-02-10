package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickArcadeCommand;
import edu.nr.robotics.subsystems.drive.commands.DrivePositionCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	public static boolean USING_ARCADE = true;
	public static boolean USING_SPLIT_ARCADE = false;
	public static boolean USING_COFFIN = false;
	
	private static OI singleton;
	
	Joystick stickTankLeft;
	Joystick stickTankRight;
	Joystick stickArcade;
	Joystick coffin;
	//xBox goes in 0, joystick for Arcade goes in 1, left joystick for tank goes in 2, right joystick for tank goes in 3
	
	private OI()
	{
		if(USING_ARCADE && !USING_SPLIT_ARCADE)
		{
			stickArcade = new Joystick(0);
		}
		else
		{
			stickTankLeft = new Joystick(2);
			stickTankRight = new Joystick(3);
		}
		if(USING_COFFIN)
		{
			coffin = new Joystick(1);
		}
		
		Joystick buttonAssignmentStick;
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				buttonAssignmentStick = stickTankRight;
			else
				buttonAssignmentStick = stickArcade;
		}
		else
		{
			buttonAssignmentStick = stickTankLeft;
		}
		
		/* Update this whenever a button is used. Don't use one of these buttons.
		 * Used buttons: 10,12, 1
		 */
		new JoystickButton(buttonAssignmentStick, 3).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				new DrivePositionCommand(true).start();
			}
		});
		new JoystickButton(buttonAssignmentStick, 4).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				new DriveJoystickArcadeCommand().start();
			}
		});
		new JoystickButton(buttonAssignmentStick, 12).whenPressed(new DriveDistanceCommand(-14.5/12d, 0.5));
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
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return -stickTankLeft.getY();
			else
				return -stickArcade.getY();
		}
		else
		{
			return 0;
		}
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return -stickTankRight.getX();
			else
				return -stickArcade.getZ();
		}
		else
		{
			return 0;
		}
	}

	public double getTankLeftValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return -stickTankLeft.getY();
	}

	public double getTankRightValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return stickTankRight.getY();
	}
	
	public double getAmplifyMultiplyer()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return stickTankRight.getRawButton(1)?2:1;
			else
				return stickArcade.getRawButton(1)?2:1;
		}
		
		return 1;
	}
		
	public double getDecreaseValue()
	{
		return 1;
	}
	
	public double getFrontElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//THIS NEEDS TO BE SET
		}
		return 0;
	}
	
	public double getBackElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//THIS NEEDS TO BE SET
		}
		return 0;
	}

	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return stickTankRight.getRawButton(2);
			else
				return stickArcade.getRawButton(2);
		}
		return false;
	}
}

