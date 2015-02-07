package edu.nr.robotics.subsystems.frontElevator;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GoToHeightCommand extends Command {
	double height;
    public GoToHeightCommand(double height) {
        requires(FrontElevator.getInstance());
        this.height = height;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	FrontElevator.getInstance().setSetpoint(height);
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
