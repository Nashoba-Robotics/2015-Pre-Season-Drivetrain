package edu.nr.robotics.commands;

import edu.nr.robotics.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForwardUntilIRValueCommand extends Command {

	private int IRValue;
    public DriveForwardUntilIRValueCommand(int IRValue) {
        requires(DriveSubsystem.getInstance());
        this.IRValue = IRValue;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	DriveSubsystem.getInstance().drive(1, 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(DriveSubsystem.getInstance().getIRDistance() == IRValue)
        {
        	return true;
        }
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	DriveSubsystem.getInstance().drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriveSubsystem.getInstance().drive(0, 0);
    }
}
