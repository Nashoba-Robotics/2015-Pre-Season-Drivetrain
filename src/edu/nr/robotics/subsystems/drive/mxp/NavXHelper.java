package edu.nr.robotics.subsystems.drive.mxp;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavXHelper 
{
	private SerialPort serial_port;
	private IMU imu;
	
	private NavXHelper()
	{
		try
		{
			serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
			
			byte update_rate_hz = 50;
			//imu = new IMU(serial_port,update_rate_hz);
			imu = new IMU(serial_port,update_rate_hz);
			SmartDashboard.putData("NavX", imu);
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
	
	
	//Singleton code
	private static NavXHelper singleton;
	public static NavXHelper getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new NavXHelper();
	}
}
