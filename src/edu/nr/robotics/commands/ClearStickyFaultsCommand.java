package edu.nr.robotics.commands;

import edu.nr.robotics.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClearStickyFaultsCommand extends Command {

    public ClearStickyFaultsCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	Robot.pdp.clearStickyFaults();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
