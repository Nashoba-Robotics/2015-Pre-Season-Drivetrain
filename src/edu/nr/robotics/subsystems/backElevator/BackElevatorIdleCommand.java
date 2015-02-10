package edu.nr.robotics.subsystems.backElevator;

import edu.nr.robotics.OI;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BackElevatorIdleCommand extends Command {

    private static final double JOYSTICK_ADJUSTMENT_VALUE = 0.2;

	public BackElevatorIdleCommand() {
        requires(BackElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Math.abs(OI.getInstance().getBackElevatorJoy()) > 0.05)
    	{
    		BackElevator.getInstance().setSetpoint(BackElevator.getInstance().getSetpoint() + OI.getInstance().getBackElevatorJoy()*JOYSTICK_ADJUSTMENT_VALUE );
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
