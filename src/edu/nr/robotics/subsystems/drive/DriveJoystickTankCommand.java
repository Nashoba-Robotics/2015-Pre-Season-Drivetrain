package edu.nr.robotics.subsystems.drive;

import edu.nr.robotics.OI;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveJoystickTankCommand extends Command {

    public DriveJoystickTankCommand() {
        requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
    
    private final double deadZone = 0.05;

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double left = OI.getInstance().getTankLeftValue();
    	double right = OI.getInstance().getTankRightValue();
    	if(Math.abs(left) < deadZone)
    	{
    		left = 0;
    	}
    	if(Math.abs(right) < deadZone)
    	{
    		right = 0;
    	}
    	Drive.getInstance().tankDrive(OI.getInstance().getTankLeftValue(), OI.getInstance().getTankRightValue());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
