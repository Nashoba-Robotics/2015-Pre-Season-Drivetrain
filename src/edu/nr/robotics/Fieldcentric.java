package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;

public class Fieldcentric 
{
	private static Fieldcentric robotInstance;
	public static Fieldcentric getRobotInstance()
	{
		if(robotInstance == null)
			robotInstance = new Fieldcentric(Math.PI/2);
		return robotInstance;
	}
	
	private final double initialTheta;
	public Fieldcentric(double initialTheta)
	{
		this.initialTheta = initialTheta;
	}
	
    private double x = 0, y = 0, lastEncoderDistance = 0;
    
    private double initialGyro = 0;
    
    private long lastUpdateTime;
    
    public void update()
    {
        if(System.currentTimeMillis() - lastUpdateTime > 300)
        {
            //System.err.println("WARNING: FieldCentric not being called often enough: (" + ((System.currentTimeMillis() - lastUpdateTime)/1000f) + "s)");
        }
        
        double angle = Drive.getInstance().getAngleDegrees() - initialGyro;
        angle *= (Math.PI / 180); //Convert to radians
        angle *= -1; //Gyro is reversed (clockwise causes an increase in the angle)
        angle += initialTheta; //Make the initial position be facing north
        
        double ave = Drive.getInstance().getEncoderAve();
        double delta_x_r = (ave-lastEncoderDistance);
        double deltax = delta_x_r * Math.cos(angle);
        double deltay = delta_x_r * Math.sin(angle);
        x += deltax;
        y += deltay;
        
        lastEncoderDistance = ave;
        
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public double getY()
    {
    	return y;
    }
    
    public double getX()
    {
    	return x;
    }
    
    //The angle used for current coordinate calculations
    public double getFieldCentricAngleRadians()
    {
    	return (Drive.getInstance().getAngleDegrees() - initialGyro) * (-Math.PI / 180) + initialTheta;
    }
    
    public void reset()
    {
    	x = 0;
    	y = 0;
    	lastEncoderDistance = Drive.getInstance().getEncoderAve();
    	initialGyro = Drive.getInstance().getAngleDegrees();
    }
}