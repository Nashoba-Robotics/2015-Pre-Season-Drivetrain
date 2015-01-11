package edu.nr.robotics;



import edu.nr.robotics.commands.DriveDistanceCommand;
import edu.nr.robotics.commands.DriveDistanceInfraredCommand;
import edu.nr.robotics.commands.DrivetrainJoystickCommand;
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
		new JoystickButton(joy0, 5).whenPressed(DriveDistanceCommand.getTestingCommand());
		new JoystickButton(joy0, 3).whenPressed(DriveDistanceCommand.getTestingReverseCommand());
		new JoystickButton(joy0, 1).whenPressed(new DrivetrainJoystickCommand());
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

