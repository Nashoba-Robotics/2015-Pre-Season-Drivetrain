package edu.nr.robotics.subsystems.drive;

import edu.nr.robotics.OI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveForwardCommand extends Command 
{
	private double speed;
	private boolean useGyroCorrection;
	private GyroCorrectionUtil gyroCorrection;
	
    public DriveForwardCommand(double speed, boolean useGyroCorrection)
    {
        requires(Drive.getInstance());
        this.speed = speed;
        this.useGyroCorrection = useGyroCorrection;
        gyroCorrection = new GyroCorrectionUtil();
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double turn = 0;
    	
    	if(useGyroCorrection)
    	{
    		turn = gyroCorrection.getTurnValue();
    	}
    	
    	Drive.getInstance().arcadeDrive(SmartDashboard.getNumber("Forward Speed"), turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	gyroCorrection.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	//Cleanup is the same whether ending peacefully or not (in this case)
    	end();
    }
}
