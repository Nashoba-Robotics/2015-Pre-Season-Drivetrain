package org.usfirst.frc.team1768.robot.commands;

import org.usfirst.frc.team1768.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainJoystickCommand extends Command {

    public DrivetrainJoystickCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	double x = Robot.oi.getX();
    	double y = Robot.oi.getY();
    	double z = Robot.oi.getZ();
    	
    	if( Math.abs(x) < 0.05)
    	{
    		x = 0;
    	}
    	
    	if( Math.abs(y) < 0.05)
    	{
    		y = 0;
    	}
    	
    	if( Math.abs(z) < 0.05 )
    	{
    		z = 0;
    	}
    	
    	Robot.drivetrain.driveRobot(y, z);
    	
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
