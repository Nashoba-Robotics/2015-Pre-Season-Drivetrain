package edu.nr.robotics;



import edu.nr.robotics.commands.DriveDistanceCommand;
import edu.nr.robotics.commands.DriveDistanceInfraredCommand;
import edu.nr.robotics.commands.DrivetrainJoystickCommand;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
	private boolean usingXboxController = true;
	
	Joystick joy0;
	
	public OI()
	{
		joy0 = new Joystick(0);
		
		int driveForwardTestButton, driveReverseTestButton, driveJoystickButton;
		
		if(usingXboxController)
		{
			driveForwardTestButton = 4;
			driveReverseTestButton = 3;
			driveJoystickButton = 6;
		}
		else
		{
			driveForwardTestButton = 5;
			driveReverseTestButton = 3;
			driveJoystickButton = 1;
		}
		new JoystickButton(joy0, driveForwardTestButton).whenPressed(DriveDistanceCommand.getTestingCommand());
		new JoystickButton(joy0, driveReverseTestButton).whenPressed(DriveDistanceCommand.getTestingReverseCommand());
		new JoystickButton(joy0, driveJoystickButton).whenPressed(new DrivetrainJoystickCommand());
	}
	
	public double getX()
	{
		if(usingXboxController)
		{
			return joy0.getRawAxis(0);
		}
		else
		{
			return joy0.getAxis(AxisType.kX);	
		}
	}
	
	public double getY()
	{
		if(usingXboxController)
			return joy0.getRawAxis(1);
		else
			return joy0.getAxis(AxisType.kY);
	}
	
	public double getZ()
	{
		if(usingXboxController)
		{
			return joy0.getRawAxis(4);
		}
		else
		{
			return joy0.getAxis(AxisType.kZ);
		}
	}
}

