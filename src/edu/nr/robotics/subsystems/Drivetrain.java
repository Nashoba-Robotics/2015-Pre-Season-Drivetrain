package edu.nr.robotics.subsystems;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commands.DrivetrainJoystickCommand;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivetrain extends Subsystem 
{
	Talon l1,l2,r1,r2;
	RobotDrive drive;
	Gyro gyro;
	AnalogInput infrared1;
	
	Counter enc1, enc2;
	
	BuiltInAccelerometer roboAccel = new BuiltInAccelerometer();

	public Drivetrain()
	{
		
		infrared1 = new AnalogInput(RobotMap.INFARED_1);
		l1 = new Talon(RobotMap.TALON_LEFT_1);
		l2 = new Talon(RobotMap.TALON_LEFT_2);
		r1 = new Talon(RobotMap.TALON_RIGHT_1);
		r2 = new Talon(RobotMap.TALON_RIGHT_2);
		drive =  new RobotDrive(l1, l2, r1, r2);
		drive.setSafetyEnabled(false);
		gyro = new Gyro(RobotMap.GYRO_1);
		
		gyro.reset();
		
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
    	if(enc1 != null && enc2 != null)
    		return (enc1.getDistance() + enc2.getDistance())/2d;
    	return 0;
    }
    
    public double getEncoder1DistanceInches()
    {
    	if(enc1 != null)
    		return enc1.getDistance();
    	return 0;
    }
    
    public double getEncoder2DistanceInches()
    {
    	if(enc2 != null)
    		return enc2.getDistance();
    	return 0;
    }
    
    public void resetEncoders()
    {
    	if(enc1 != null && enc2 != null)
    	{
    		enc1.reset();
        	enc2.reset();
    	}
    }
    
    public void resetGyro()
    {
    	gyro.reset();
    }
    
    public float getGyroRate()
    {
    	return (float) gyro.getRate();
    }
    
    public double getInfaredVoltage()
    {
    	return infrared1.getVoltage();
    }
    
    public void dashboardSensors()
    {
    	SmartDashboard.putNumber("Gyro", getAngle());
    	SmartDashboard.putNumber("Encoder 1", enc1.getDistance());
    	SmartDashboard.putNumber("Encoder 2", enc2.getDistance());
    	SmartDashboard.putNumber("Infrared Sensor", infrared1.getVoltage());
    	SmartDashboard.putNumber("Average Encoder Distance", getAverageEncoderDistance());
    	SmartDashboard.putNumber("Accel X", roboAccel.getX());
    	SmartDashboard.putNumber("Accel Y", roboAccel.getY());
    	SmartDashboard.putNumber("Accel Z", roboAccel.getZ());
    }
}

