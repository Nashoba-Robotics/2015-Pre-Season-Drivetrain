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
	private Fieldcentric coordinateSystem;
	
	private final double Kp = 0.3, Ka = 0.8/4, Kb = -0.15/4;
	
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

	double flip = 0.01;
	
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
		velocity = Math.min(velocity, 0.2);
		
		double turnVelocity = Ka * alpha + Kb * (beta - goalAngle);
		turnVelocity *= 1;
		
		Drive.getInstance().arcadeDrive(velocity, turnVelocity);
		
		SmartDashboard.putNumber("P", p);
		SmartDashboard.putNumber("Drive Position Velocity", velocity);
		SmartDashboard.putNumber("Delta Angle", (goalAngle - angle) +  (goalAngle - angle) * flip);
		flip *= -1;
		SmartDashboard.putNumber("Turn Velocity", turnVelocity);
		SmartDashboard.putNumber("Alpha", alpha);
		SmartDashboard.putNumber("Drive Position X", coordinateSystem.getX());
		SmartDashboard.putNumber("Drive Position Y", coordinateSystem.getY());
		SmartDashboard.putNumber("Drive Position Angle", coordinateSystem.getFieldCentricAngleRadians());
	}

	@Override
	protected boolean isFinished() 
	{
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		SmartDashboard.putNumber("dx", dx);
		SmartDashboard.putNumber("dy", dy);
		
		return (p < .5);
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
