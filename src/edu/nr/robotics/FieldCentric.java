package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FieldCentric 
{
    private static double x = 0, y = 0, lastEncoderDistance = 0;
    
    private static long lastUpdateTime;
    private static NetworkTable table;
    private static boolean initialized = false;
    
    public static void update()
    {
        if(!initialized)
        {
            initialized = true;
            table = NetworkTable.getTable("FieldCentric");
        }
        else if(System.currentTimeMillis() - lastUpdateTime > 200)
        {
            System.err.println("WARNING: FieldCentric not being called often enough: (" + ((System.currentTimeMillis() - lastUpdateTime)/1000f) + "s)");
        }
        
        double angle = Drive.getInstance().getAngle();
        angle *= (Math.PI / 180); //Convert to radians
        
        double ave = Drive.getInstance().getEncoderAve();
        double delta_x_r = (ave-lastEncoderDistance);
        double deltax = delta_x_r * Math.cos(-angle);
        double deltay = delta_x_r * Math.sin(-angle);
        x += deltax;
        y += deltay;
        
        lastEncoderDistance = ave;
        
        SmartDashboard.putNumber("Location x", x);
        SmartDashboard.putNumber("Location y", y);
        SmartDashboard.putNumber("Field Angle", angle * 180 / Math.PI);
        table.putNumber("x", x);
        table.putNumber("y", y);
        table.putNumber("angle", angle);
        
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public static void reset()
    {
    	x = 0;
    	y = 0;
    	lastEncoderDistance = Drive.getInstance().getEncoderAve();
    	Drive.getInstance().resetGyro();
    }
}