package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.GyroCorrectionUtil;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoDriveDistance extends Command
{
	private long startTime;
	private double distanceFeet, speed;
	private GyroCorrectionUtil gyroCorrection;
	
	private double initialEncoderDistance;
	private boolean resetEncoders = true;
	
    public AutoDriveDistance(double distanceFeet, double speed) 
    {
    	this.distanceFeet = distanceFeet;
    	this.speed = Math.abs(speed) * Math.signum(distanceFeet);
    	gyroCorrection = new GyroCorrectionUtil();
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    double flip = .01;
    
    protected void execute()
    {
		double turn = gyroCorrection.getTurnValue();
		
		if(resetEncoders)
		{
			initialEncoderDistance = Drive.getInstance().getEncoderAve();
			resetEncoders = false;
			Drive.getInstance().setDriveP(4);
			startTime = System.currentTimeMillis();
		}
		
		double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		
		double tempSpeed = speed;
		
		double absDriven = Math.abs(distanceDriven);
		if((absDriven > 4 && absDriven < 8) || (absDriven > 14 && absDriven < 18))
			tempSpeed = Math.signum(tempSpeed) * 0.2;
		else if(absDriven > 18)
			tempSpeed = Math.signum(tempSpeed) * 0.1;
		
		double err = (distanceFeet - distanceDriven);
		
		/*double pMove = Math.abs(err * tempSpeed) * Math.signum(tempSpeed);
		SmartDashboard.putNumber("P Move", pMove);
		
		double move;
		if(speed < 0)
			move = Math.max(pMove, tempSpeed);
		else
			move = Math.min(pMove, tempSpeed);*/
		
		double move = tempSpeed;
		Drive.getInstance().arcadeDrive(move, turn);
		
		SmartDashboard.putNumber("Drive Distance Move", move + move * flip);
		SmartDashboard.putNumber("Drive Distance Turn", turn + turn * flip);
		SmartDashboard.putNumber("Err", err + err * flip);
		flip *= -1;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	/*double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		double err = (distanceFeet - distanceDriven);
		
    	if(distanceFeet < 0)
    	{
    		return (err > 0);
    	}
		else
		{
			return (err < 0);
		}*/
    	
    	return (Drive.getInstance().getBumper1() && Drive.getInstance().getBumper2());
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	SmartDashboard.putNumber("Drive Distance Time", (System.currentTimeMillis() - startTime)/1000f);
    	gyroCorrection.stop();
    	resetEncoders = true;
    	Drive.getInstance().setDriveP(0.5);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
