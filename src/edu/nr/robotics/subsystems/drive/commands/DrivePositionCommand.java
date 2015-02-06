package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.Fieldcentric;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePositionCommand extends Command
{
	private boolean reset = true;
	
	private double goalX, goalY, goalAngle;
	private Fieldcentric coordinateSystem;
	
	private final double Kp = 0.3, Ka = 0.8, Kb = -0.2;
	
	public DrivePositionCommand(double posX, double posY, double finalAngle)
	{
		this.requires(Drive.getInstance());
		
		/*this.goalX = posX;
		this.goalY = posY;
		this.goalAngle = finalAngle;*/
		
		coordinateSystem = new Fieldcentric(0);
	}
	
	@Override
	protected void initialize() 
	{
	}

	int iCount = 0;
	
	@Override
	protected void execute()
	{
		if(reset)
		{
			Drive.getInstance().setDriveP(1);
			this.goalX = SmartDashboard.getNumber("Goal X");
			this.goalY = SmartDashboard.getNumber("Goal Y");
			this.goalAngle = -SmartDashboard.getNumber("Goal Angle");
			coordinateSystem.reset();
			reset = false;
		}
		coordinateSystem.update();
		
		//Velocity calculations
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		double velocity = Kp * p;
		velocity = Math.min(velocity, 0.6);
		
		//Turn Velocity Calculations
		double angle = coordinateSystem.getFieldCentricAngleRadians();
		double alpha = Math.atan2(dy, dx) - angle;
		double beta = -angle - alpha;
		double turnVelocity = Ka * alpha + Kb * (beta - goalAngle);
		
		SmartDashboard.putNumber("Alpha Turn Term", Ka * alpha);
		SmartDashboard.putNumber("Beta Turn Term", Kb * (beta - goalAngle));
		
		if(p < 1)
		{
			iCount++;
			velocity += (0.001 * iCount) * Math.signum(velocity);
			turnVelocity += (0.001 * iCount) * Math.signum(turnVelocity);
			turnVelocity = Math.min(Math.abs(turnVelocity), 0.15) * Math.signum(turnVelocity);
		}
		
		Drive.getInstance().arcadeDrive(velocity, turnVelocity);
		
		SmartDashboard.putNumber("P", p);
		SmartDashboard.putNumber("Drive Position Velocity", velocity);
		SmartDashboard.putNumber("Delta Angle", (-goalAngle - angle));
		SmartDashboard.putNumber("Turn Velocity", turnVelocity);
		SmartDashboard.putNumber("Alpha", alpha);
		SmartDashboard.putNumber("Angle", angle);
		SmartDashboard.putNumber("Drive Position X", coordinateSystem.getX());
		SmartDashboard.putNumber("Drive Position Y", coordinateSystem.getY());
	}

	@Override
	protected boolean isFinished() 
	{
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		SmartDashboard.putNumber("dx", dx);
		SmartDashboard.putNumber("dy", dy);
		
		return (p < .25);
	}

	@Override
	protected void end() 
	{
		reset = true;
		Drive.getInstance().setDriveP(Drive.JOYSTICK_DRIVE_P);
	}

	@Override
	protected void interrupted() 
	{
		end();
	}

}
