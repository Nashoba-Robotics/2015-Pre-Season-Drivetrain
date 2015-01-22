package edu.nr.robotics.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveJoystickCommand extends Command {
	private boolean reset = true;
	private double gyroDefaultAngle = 0;
	
	//Angle correction intensity
	private double Kp = 0.01;
	
    public DriveJoystickCommand() 
    {
        requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    private final double deadZone = 0.05;
    
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
    	
    	
    	//Determine scale value based off trigger values. This will always be 1 for the logitech joystick.
    	double scaleDrive = OI.getInstance().getDefaultMaxValue() 
    			- (OI.getInstance().getDecreaseValue()*0.4) 
    			+ (OI.getInstance().getAmplifyValue()*0.4);
    	
    	double driveMagnitude = rawMoveValue * scaleDrive;
    	double turn;
    	
    	if(OI.getInstance().useGyroCorrection())
    	{
    		//Get a new initial gyro value if the button was just pressed
	    	if(reset)
	    	{
	    		reset = false;
	    		gyroDefaultAngle = Drive.getInstance().getAngle();
	    	}
	    	
	    	//Determine turn intensity based off of angle error
	    	double currentGyroAngle = Drive.getInstance().getAngle();
	    	turn = (currentGyroAngle-gyroDefaultAngle)*Kp;
	    	if(turn<0)
	    		turn = Math.max(-0.2, turn);
	    	else
	    		turn = Math.min(0.2, turn);
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
    		turn = rawTurn * scaleDrive;
    		
    		reset = true;
    	}
    	
    	SmartDashboard.putNumber("Drive Magnitude", driveMagnitude);
    	SmartDashboard.putNumber("Turn", turn);
    	SmartDashboard.putNumber("scaleDrive", scaleDrive);
    	

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
