package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistanceCommand extends Command
{
	private double distanceFeet, speed;
	private GyroCorrectionUtil gyroCorrection;
	
	private double initialEncoderDistance;
	private boolean resetEncoders = true;
	
    public DriveDistanceCommand(double distanceFeet, double speed) 
    {
    	this.distanceFeet = distanceFeet;
    	this.speed = Math.abs(speed) * Math.signum(distanceFeet);
    	gyroCorrection = new GyroCorrectionUtil();
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    protected void execute()
    {
		double turn = gyroCorrection.getTurnValue();
		
		if(resetEncoders)
			initialEncoderDistance = Drive.getInstance().getEncoderAve();
		
		double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		double err = (distanceFeet - distanceDriven);
		
		double pMove = err / 2 * speed;
		
		double move;
		if(speed < 0)
			move = Math.max(pMove, speed);
		else
			move = Math.min(pMove, speed);
		
		Drive.getInstance().arcadeDrive(move, turn);
		
		SmartDashboard.putNumber("Drive Distance Move", move);
		SmartDashboard.putNumber("Drive Distance Turn", turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		double err = (distanceFeet - distanceDriven);
		
    	if(distanceFeet < 0)
    	{
    		return (err > 0);
    	}
		else
		{
			return (err < 0);
		}
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	gyroCorrection.stop();
    	resetEncoders = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
