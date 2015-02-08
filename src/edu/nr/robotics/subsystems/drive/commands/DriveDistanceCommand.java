package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrectionUtil;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistanceCommand extends Command
{
	private double distanceFeet, speed;
	private AngleGyroCorrectionUtil gyroCorrection;
	
	private double initialEncoderDistance;
	private boolean resetEncoders = true;
	
    public DriveDistanceCommand(double distanceFeet, double speed) 
    {
    	this.distanceFeet = distanceFeet;
    	this.speed = Math.abs(speed) * Math.signum(distanceFeet);
    	gyroCorrection = new AngleGyroCorrectionUtil();
    	requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    protected void execute()
    {
		double turn = gyroCorrection.getTurnValue();
		
		if(resetEncoders)
		{
			initialEncoderDistance = Drive.getInstance().getEncoderAve();
			resetEncoders = false;
			Drive.getInstance().setDriveP(1);
		}
		
		double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		
		double tempSpeed = speed;
		
		double err = (distanceFeet - distanceDriven);
		
		double pMove = Math.max(Math.abs(err / 1* tempSpeed), 0.07) * Math.signum(tempSpeed);
		SmartDashboard.putNumber("P Move", pMove);
		
		double move;
		if(speed < 0)
			move = Math.max(pMove, tempSpeed);
		else
			move = Math.min(pMove, tempSpeed);
		
		Drive.getInstance().arcadeDrive(move, turn);
		
		SmartDashboard.putNumber("Drive Distance Move", move);
		SmartDashboard.putNumber("Drive Distance Turn", turn);
		SmartDashboard.putNumber("Drive Distance Err", err);
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
    	gyroCorrection.clearInitialValue();
    	resetEncoders = true;
    	Drive.getInstance().setDriveP(Drive.JOYSTICK_DRIVE_P);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
