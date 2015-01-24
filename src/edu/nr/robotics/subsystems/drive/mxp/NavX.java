package edu.nr.robotics.subsystems.drive.mxp;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX 
{
	private SerialPort serial_port;
	private IMU imu;
	
	private NavX()
	{
		try
		{
			serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
			
			byte update_rate_hz = 100;
			//imu = new IMU(serial_port,update_rate_hz);
			imu = new IMU(serial_port,update_rate_hz);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: An error occured while initializing the MXP Board");
		}
	}
	
	public double getYaw()
	{
		if(imu != null && imu.isConnected())
			return imu.getYaw();
		return 0;
	}
	
	public double getRoll()
	{
		if(imu != null && imu.isConnected())
			return imu.getRoll();
		return 0;
	}
	
	public double getPitch()
	{
		if(imu != null && imu.isConnected())
			return imu.getPitch();
		return 0;
	}
	
	public void resetAll()
	{
		if(imu != null && imu.isConnected())
		{
			imu.zeroYaw();
		}
	}
	
	
	//Singleton code
	private static NavX singleton;
	public static NavX getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new NavX();
	}
}
