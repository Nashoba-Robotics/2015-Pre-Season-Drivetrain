package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.Fieldcentric;
import edu.wpi.first.wpilibj.command.Command;

public class ResetFieldcentricCommand extends Command
{
	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() 
	{
		Fieldcentric.getRobotInstance().reset();
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
