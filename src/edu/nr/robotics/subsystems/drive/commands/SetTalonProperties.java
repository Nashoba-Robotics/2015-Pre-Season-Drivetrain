package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;

public class SetTalonProperties extends Command
{

	@Override
	protected void initialize()
	{
		
	}

	@Override
	protected void execute() 
	{
		Drive.getInstance().setTalonProperties();
	}

	@Override
	protected boolean isFinished() 
	{
		return true;
	}

	@Override
	protected void end() 
	{
		
	}

	@Override
	protected void interrupted() 
	{
		
	}
	
}
