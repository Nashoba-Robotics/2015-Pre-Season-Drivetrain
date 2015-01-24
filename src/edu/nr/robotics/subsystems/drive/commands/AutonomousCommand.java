package edu.nr.robotics.subsystems.drive.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousCommand extends CommandGroup
{
	private long startTime;
	private boolean reset = true;
	
	public AutonomousCommand()
	{
		this.addSequential(new AutoDriveDistance(-20, 0.4));
		this.addSequential(new WaitCommand(2));
		this.addSequential(new DriveDistanceCommand(8, 0.3));
	}
	
	@Override
	public void execute()
	{
		if(reset)
		{
			startTime = System.currentTimeMillis();
			reset = false;
		}
		
		super.execute();
	}
	
	@Override
	public void end()
	{
		super.end();
		reset = true;
		SmartDashboard.putNumber("Autonomous Total Time", (System.currentTimeMillis() - startTime) / 1000f);
	}
	
	@Override
	public void interrupted()
	{
		super.interrupted();
		reset = true;
		SmartDashboard.putNumber("Autonomous Total Time", (System.currentTimeMillis() - startTime) / 1000f);
	}
}
