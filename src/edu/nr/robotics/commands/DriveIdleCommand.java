package edu.nr.robotics.commands;

import edu.nr.robotics.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveIdleCommand extends Command {

    public DriveIdleCommand() {
        requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Drive.getInstance().drive(0, 0);
    	Drive.getInstance().solenoidOff();
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
