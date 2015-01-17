package edu.nr.robotics.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveJoystickCommand extends Command {
	private boolean reset = true;
	private double gyroDefaultAngle = 0;
	
	private double Kp = 0.3;
	
    public DriveJoystickCommand() {
        requires(DriveSubsystem.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double scaleDrive = 0.675 - (OI.getInstance().getJoyZ1()*0.325) + (OI.getInstance().getJoyZ2()*0.325);
    	double driveMagnitude = OI.getInstance().getJoyY1() * scaleDrive;
    	double turn;
    	if(OI.getInstance().getButton(6))
    	{
	    	if(reset)
	    	{
	    		reset = false;
	    		gyroDefaultAngle = DriveSubsystem.getInstance().getGyro().getAngle();
	    	}
	    	double currentGyroAngle = DriveSubsystem.getInstance().getGyro().getAngle();
	    	turn = (currentGyroAngle-gyroDefaultAngle)*Kp;
	    	if(turn<0)
	    		turn = Math.max(-0.4, turn);
	    	else
	    		turn = Math.min(0.4, turn);
    	}
    	else
    	{
    		turn = OI.getInstance().getJoyX2()*scaleDrive;
    		reset = true;
    	}
    	SmartDashboard.putNumber("JoyY1", OI.getInstance().getJoyY1());
    	SmartDashboard.putNumber("JoyX2", OI.getInstance().getJoyX2());
    	SmartDashboard.putNumber("Drive Magnitude", driveMagnitude);
    	SmartDashboard.putNumber("turn", turn);
    	//SmartDashboard.putNumber("leftTrigger", OI.getInstance().getJoyZ1());
    	//SmartDashboard.putNumber("rightTrigger", OI.getInstance().getJoyZ2());
    	SmartDashboard.putNumber("scaleDrive", scaleDrive);

    	DriveSubsystem.getInstance().drive(driveMagnitude, turn);

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
    }
}
