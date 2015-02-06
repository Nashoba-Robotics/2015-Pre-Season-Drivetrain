package edu.nr.robotics;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class EmptyCommand extends Command
{
	public EmptyCommand()
	{
		
	}
	public EmptyCommand(Subsystem requires)
	{
		this.requires(requires);
	}
	
	@Override
	protected void initialize() 
	{
	}

	@Override
	protected void execute() 
	{
		
	}

	@Override
	protected boolean isFinished() 
	{
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
