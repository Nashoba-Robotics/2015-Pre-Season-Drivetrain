package edu.nr.robotics.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForwardCommand extends Command 
{
	private double speed;
	private boolean useGyroCorrection;
	
	private double initialAngle;
	private boolean reset = true;
	
	private double Kp = 0.03;
	
    public DriveForwardCommand(double speed, boolean useGyroCorrection)
    {
        requires(Drive.getInstance());
        this.speed = speed;
        this.useGyroCorrection = useGyroCorrection;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	if(reset)
    	{
    		reset = false;
    		initialAngle = Drive.getInstance().getAngle();
    	}
    	
    	double turn = 0;
    	
    	if(useGyroCorrection)
    	{
    		//Determine turn intensity based off of angle error
	    	double currentGyroAngle = Drive.getInstance().getAngle();
	    	turn = (currentGyroAngle-initialAngle)*Kp;
	    	if(turn<0)
	    		turn = Math.max(-0.4, turn);
	    	else
	    		turn = Math.min(0.4, turn);
    	}
    	
    	Drive.getInstance().drive(speed, turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Drive.getInstance().drive(0, 0);
    	reset = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	//Cleanup is the same whether ending peacefully or not (in this case)
    	end();
    }
}
