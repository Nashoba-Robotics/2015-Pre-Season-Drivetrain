package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.Fieldcentric;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePositionCommand extends Command
{
	private boolean reset = true;
	private final boolean useRoboRealm;
	
	private double goalX, goalY, goalAngle;
	private Fieldcentric coordinateSystem;
	
	private final double Kp = 0.3, Ka = 1.2, Kb = -0.3;
	
	public DrivePositionCommand(boolean useRoboRealm)
	{
		this.requires(Drive.getInstance());
		
		this.useRoboRealm = useRoboRealm;
		
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
			this.goalX = SmartDashboard.getNumber((useRoboRealm)?"ToteX":"Goal X");
			this.goalY = SmartDashboard.getNumber((useRoboRealm)?"ToteY":"Goal Y");
			this.goalAngle = -SmartDashboard.getNumber((useRoboRealm)?"ToteAngle":"Goal Angle");
			coordinateSystem.reset();
			
			//Center of rotation correction
			final double P = Drive.CENTER_OF_ROTATION_RELATIVE_TO_CAMERA_FEET;
			
			double deltaX = P - P * Math.cos(goalAngle);
			double deltaY = P * Math.sin(goalAngle) ;
			
			this.goalX += deltaX;
			this.goalY += deltaY;
			
			reset = false;
		}
		coordinateSystem.update();
		
		//Velocity calculations
		double dx = goalX - coordinateSystem.getX();
		double dy = goalY - coordinateSystem.getY();
		double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		double velocity = Kp * p;
		velocity = Math.min(velocity, 0.7);
		
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
		
		double deltaAngle = (-goalAngle - coordinateSystem.getFieldCentricAngleRadians());
		
		if(p < .2)
		{
			new DriveAngleCommand(-deltaAngle).start();
			return true;
		}
		return false;
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
