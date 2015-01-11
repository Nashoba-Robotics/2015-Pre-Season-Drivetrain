package edu.nr.robotics.commands;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.nr.robotics.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A command that uses PID to drive the robot a certain distance at a defined max speed
 */
public class DriveDistanceCommand extends Command
{
    private int count = 0;
    private float distance, speed;
    private double initialGyroAngle, initialEncoderDistance;
    private boolean goingForward;
    
    private DriveDistanceCommand()
    {
    	
    }
    
    public DriveDistanceCommand(float distance, float speed)
    {
        super("Drive Distance Command");
        this.speed = speed;
        this.requires(Robot.drivetrain);
        this.distance = distance;
        
        if(distance > 0)
        {
        	goingForward = true;
        }
        else if(distance < 0)
        {
        	goingForward = false;
        	
        	//IMPORTANT- Flip this only if the encoders are non-directional (can't tell which direction they are turning in)
        	if(!usingQuadEncoder)
        		distance = -distance; 
        }
        else
        {
        	cancel();
        }
    }
    
    
    private boolean usingQuadEncoder = false;
    
    protected void initialize() 
    {
        initialEncoderDistance = Robot.drivetrain.getAverageEncoderDistance();
        
        initialGyroAngle = Robot.drivetrain.getAngle();
    }

    private final float angleCorrectionIntensity = 0.10f;
    
    protected void execute() 
    {
        //Use the gyroscope to correct our angle if we have started to turn slightly
        double angle = Robot.drivetrain.getAngle() - initialGyroAngle;
        double turnAngle = 0;

        if(angle < 0)
            turnAngle = Math.min(0.4, -angle * angleCorrectionIntensity);
        else
            turnAngle = Math.max(-0.4, -angle * angleCorrectionIntensity);

        double ave = Robot.drivetrain.getAverageEncoderDistance() - initialEncoderDistance;
        SmartDashboard.putNumber("Encoder Average Driving Distance", ave);

        //Do the math in positive
        double err = Math.abs(distance - ave);

        double proportionalStopDistance = 10;
        double proportionalSpeed = ((1/(proportionalStopDistance)) * err) * speed;
        
        if(proportionalSpeed < speed)
        	count++;
        double integralSpeed = count * speed/Math.abs(speed) * 0.003;
        double newSpeed = Math.min(speed, proportionalSpeed + integralSpeed);

        // Reverse the speed if we are going backwards
        newSpeed *= ((goingForward)?-1:1); 

        SmartDashboard.putNumber("I Value", integralSpeed);
        SmartDashboard.putNumber("Proportional Speed", proportionalSpeed);
        SmartDashboard.putNumber("Driving Speed", newSpeed);
        SmartDashboard.putNumber("Driving Angle", turnAngle);
        SmartDashboard.putNumber("Raw Angle Difference", angle);
    	
        Robot.drivetrain.driveRobot(newSpeed, turnAngle);
    }

    protected boolean isFinished() 
    {
        if(goingForward)
            return (Robot.drivetrain.getAverageEncoderDistance() - initialEncoderDistance >= distance);
        else
        {
        	if(usingQuadEncoder)
        		return (Robot.drivetrain.getAverageEncoderDistance() - initialEncoderDistance <= distance);
        	else
        		return (Robot.drivetrain.getAverageEncoderDistance() - initialEncoderDistance >= distance);
        }
    }

    protected void end() 
    {
    	cleanup();
    }

    protected void interrupted() 
    {
    		cleanup();
    }
    
    private void cleanup()
    {
    	Robot.drivetrain.driveRobot(0, 0);
    	count = 0;
    }
    
    
    private static float testDrivingSpeed = 0.7f;
    public static DriveDistanceCommand getTestingCommand()
    {
    	return new DriveDistanceCommand(25.5f, testDrivingSpeed);
    }
    
    public static DriveDistanceCommand getTestingReverseCommand()
    {
    	return new DriveDistanceCommand(25.5f, -testDrivingSpeed);
    }
}
