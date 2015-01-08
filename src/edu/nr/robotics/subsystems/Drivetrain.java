package org.usfirst.frc.team1768.robot.subsystems;

import org.usfirst.frc.team1768.robot.RobotMap;
import org.usfirst.frc.team1768.robot.commands.DrivetrainJoystickCommand;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
    
	Talon l1,l2,r1,r2;
	RobotDrive drive;
	Gyro gyro;
	
	Counter enc1, enc2;

	public Drivetrain()
	{
		l1 = new Talon(RobotMap.TALON_1);
		l2 = new Talon(RobotMap.TALON_2);
		r1 = new Talon(RobotMap.TALON_3);
		r2 = new Talon(RobotMap.TALON_4);
		drive =  new RobotDrive(l1, l2, r1, r2);
		drive.setSafetyEnabled(false);
		gyro = new Gyro(RobotMap.GYRO_1);
		
		enc1 = new Counter(0);
		enc1.setDistancePerPulse(.096 / 12f); //feet
		
		enc2 = new Counter(1);
		enc2.setDistancePerPulse(.096 / 12f); //feet
	}
	
	public void driveRobot(double moveValue, double rotateValue)
	{
		drive.arcadeDrive(-moveValue, -rotateValue);
	}
	
    public void initDefaultCommand() 
    {
    	setDefaultCommand(new DrivetrainJoystickCommand());
    }
    
    
    public double getAngle()
    {
    	if(gyro != null)
    		return gyro.getAngle();
    	return 0;
    }
    
    public double getAverageEncoderDistance()
    {
    	return (enc1.getDistance() + enc2.getDistance())/2d;
    }
    
    public double getEncoder1DistanceInches()
    {
    	return enc1.getDistance();
    }
    
    public double getEncoder2DistanceInches()
    {
    	return enc2.getDistance();
    }
    
    public void resetEncoders()
    {
    	enc1.reset();
    	enc2.reset();
    }
}

