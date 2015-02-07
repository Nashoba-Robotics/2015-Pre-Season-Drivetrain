package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.EmptyCommand;

/**
 *
 */
public class AdjustToteOneHeightCommand extends EmptyCommand {
	
    public AdjustToteOneHeightCommand() {
        requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	FrontElevator.getInstance().setSetpoint(FrontElevator.HEIGHT_ADJUST_TOTE_ONE);
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
