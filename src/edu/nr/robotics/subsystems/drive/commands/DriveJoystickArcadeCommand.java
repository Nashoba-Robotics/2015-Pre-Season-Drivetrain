package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrectionUtil;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveJoystickArcadeCommand extends Command 
{
	AngleGyroCorrectionUtil gyroCorrection;
	
    public DriveJoystickArcadeCommand() 
    {
        requires(Drive.getInstance());
        gyroCorrection = new AngleGyroCorrectionUtil();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    private final double deadZone = 0.1;
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double rawMoveValue = OI.getInstance().getArcadeMoveValue();
    	if(rawMoveValue < deadZone && rawMoveValue > -deadZone)
    	{
    		rawMoveValue = 0;
    	}
    	else if(rawMoveValue > 0)
    	{
    		rawMoveValue -= deadZone;
    		rawMoveValue *= (1 / (1 - deadZone));
    	}
    	else
    	{
    		rawMoveValue += deadZone;
    		rawMoveValue *= (1 / (1 - deadZone));
    	}
    	
    	double driveMagnitude = rawMoveValue/2 * OI.getInstance().getAmplifyMultiplyer();
    	double turn;
    	
    	if(OI.getInstance().useGyroCorrection())
    	{
    		turn = gyroCorrection.getTurnValue();
    	}
    	else
    	{
    		//Use the joystick to get turn value
    		double rawTurn = OI.getInstance().getArcadeTurnValue();
    		if(rawTurn < deadZone && rawTurn > -deadZone)
        	{
    			rawTurn = 0;
        	}
        	else if(rawTurn > 0)
        	{
        		rawTurn -= deadZone;
        		rawTurn *= (1 / (1 - deadZone));
        	}
        	else
        	{
        		rawTurn += deadZone;
        		rawTurn *= (1 / (1 - deadZone));
        	}
    		turn = rawTurn /2 * OI.getInstance().getAmplifyMultiplyer();
    		
    		gyroCorrection.clearInitialValue();
    	}
    	
    	SmartDashboard.putNumber("Drive Magnitude", driveMagnitude);
    	SmartDashboard.putNumber("Turn", turn);    	

    	Drive.getInstance().arcadeDrive(driveMagnitude, turn, false);
    }

    //Always return false for a default command
    protected boolean isFinished() 
    {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	gyroCorrection.clearInitialValue();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	//Cleanup is the same whether ending peacefully or not (in this case)
    	end();
    }
}
