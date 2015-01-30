package edu.nr.robotics.subsystems.drive.commands;

import com.ni.vision.NIVision.CoordinateSystem;

import edu.nr.robotics.Fieldcentric;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePositionCommand extends Command
{
	private boolean reset = true;
	
	private double goalX, goalY, goalAngle;
	private double initialFieldAngle;
	private Fieldcentric coordinateSystem;
	
	private final double Kp = 0.3, Ka = 0.8, Kb = -0.15;
	
	public DrivePositionCommand(double posX, double posY, double finalAngle)
	{
		/*this.goalX = posX;
		this.goalY = posY;
		this.goalAngle = finalAngle;*/
		
		coordinateSystem = new Fieldcentric(0);
	}
	
	@Override
	protected void initialize() 
	{
	}

	@Override
	protected void execute()
	{
		if(reset)
		{
			Drive.getInstance().setDriveP(4);
			this.goalX = SmartDashboard.getNumber("Goal X");
			this.goalY = SmartDashboard.getNumber("Goal Y");
			this.goalAngle = SmartDashboard.getNumber("Goal Angle");
			coordinateSystem.reset();
			reset = false;
		}
		coordinateSystem.update();
		
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		double angle = coordinateSystem.getFieldCentricAngleRadians();
		double alpha = Math.atan2(dy, dx) - angle;
		double beta = -angle - alpha;
		
		double velocity = Kp * p;
		velocity = Math.min(Math.abs(velocity), 0.4) * Math.signum(velocity);
		
		double turnVelocity = Ka * alpha + Kb * (beta - goalAngle);
		
		Drive.getInstance().arcadeDrive(velocity, turnVelocity);
		
		SmartDashboard.putNumber("P", p);
		SmartDashboard.putNumber("Drive Position Velocity", velocity);
		SmartDashboard.putNumber("Delta Angle", goalAngle - angle);
		SmartDashboard.putNumber("Turn Velocity", turnVelocity);
		SmartDashboard.putNumber("Alpha", alpha);
	}

	@Override
	protected boolean isFinished() 
	{
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		return (p < .25);
	}

	@Override
	protected void end() 
	{
		reset = true;
		Drive.getInstance().setDriveP(0.5);
	}

	@Override
	protected void interrupted() 
	{
		end();
	}

}
