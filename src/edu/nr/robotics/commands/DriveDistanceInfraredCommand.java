package edu.nr.robotics.commands;

import edu.nr.robotics.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistanceInfraredCommand extends Command {

	private float speed, maxInfraredVoltage;
	
	private double initialGyroAngle;
	
    public DriveDistanceInfraredCommand(float speed, float maxInfraredVoltage) 
    {
    	this.requires(Robot.drivetrain);
    	
    	this.speed = speed;
    	this.maxInfraredVoltage = maxInfraredVoltage;
    }
    
    private final float angleCorrectionIntensity = 0.25f;

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	initialGyroAngle = Robot.drivetrain.getAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	double angle = Robot.drivetrain.getAngle() - initialGyroAngle;
        double turnAngle = 0;

        if(angle < 0)
            turnAngle = Math.min(0.4, -angle * angleCorrectionIntensity);
        else
            turnAngle = Math.max(-0.4, -angle * angleCorrectionIntensity);
    	
    	Robot.drivetrain.driveRobot(-speed, turnAngle);
    	
        SmartDashboard.putNumber("Raw Angle Difference", angle);
        SmartDashboard.putNumber("Turn Angle", turnAngle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        return Robot.drivetrain.getInfaredVoltage() < maxInfraredVoltage;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Robot.drivetrain.driveRobot(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	Robot.drivetrain.driveRobot(0, 0);
    }
}
