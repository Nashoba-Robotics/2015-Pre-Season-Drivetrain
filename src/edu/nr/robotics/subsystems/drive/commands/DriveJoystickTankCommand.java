package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.drive.Drive;
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
    	
    	// make sure that the control is actual human input, rather than garbage data
    	if(Math.abs(left) < deadZone)
    	{
    		left = 0;
    	}
    	if(Math.abs(right) < deadZone)
    	{
    		right = 0;
    	}
    	
    	// square the inputs (while preserving the sign) to increase fine control while permitting full power
        if (right >= 0.0) {
            right = (right * right);
        } else {
            right = -(right * right);
        }
        if (left >= 0.0) {
            left = (left * left);
        } else {
            left = -(left * left);
        }
        
    	Drive.getInstance().tankDrive(left, right);
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
