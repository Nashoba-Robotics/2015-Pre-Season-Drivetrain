package edu.nr.robotics.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForwardGyroCommand extends Command {

	private boolean reset = true;
	
	private double Kp = 0.03;
	
    public DriveForwardGyroCommand() {
        requires(DriveSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	if(reset)
    	{
    		reset = false;
    		//TODO Take an initial angle
    	}
    	double angle = DriveSubsystem.getInstance().getAngle();
    	DriveSubsystem.getInstance().drive(OI.getInstance().getJoyX1(), angle*Kp); // drive towards heading 0
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	DriveSubsystem.getInstance().drive(0, 0);
    	reset = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriveSubsystem.getInstance().drive(0, 0);
    }
}
