package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.mxp.NavX;
import edu.wpi.first.wpilibj.command.Command;

public class ZeroNavXCommand extends Command
{
	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute()
	{
		NavX.getInstance().resetAll();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
