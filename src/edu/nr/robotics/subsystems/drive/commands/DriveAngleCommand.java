package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.GyroPIDSource;
import edu.nr.robotics.subsystems.drive.RotationPIDOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveAngleCommand extends Command 
{
	boolean reset = true;
	double targetDeltaAngleRadians;
	PIDController pidController;
	GyroPIDSource gyroSource;
	
    public DriveAngleCommand(double targetDeltaAngleRadians) 
    {
    	requires(Drive.getInstance());
    	gyroSource = new GyroPIDSource();
    	pidController = new PIDController(0.5, 0.073, 0, gyroSource, new RotationPIDOutput());
    	SmartDashboard.putData("Angle PID", pidController);
    	this.targetDeltaAngleRadians = targetDeltaAngleRadians;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    int count = 0;
    protected void execute() 
    {
    	if(reset)
    	{
    		pidController.enable();
    		pidController.setSetpoint(gyroSource.pidGet() + targetDeltaAngleRadians);
    		reset = false;
    	}
    	SmartDashboard.putNumber("Drive Angle Command err", pidController.getError());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pidController.getError()) < 0.01;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	pidController.disable();
    	reset = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
