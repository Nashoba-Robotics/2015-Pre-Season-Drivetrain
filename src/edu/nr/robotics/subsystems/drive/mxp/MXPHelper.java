package edu.nr.robotics.subsystems.drive.mxp;

import edu.wpi.first.wpilibj.SerialPort;

public class MXPHelper 
{
	private SerialPort serial_port;
	private IMU imu;
	
	private MXPHelper()
	{
		try
		{
			serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
			
			byte update_rate_hz = 50;
			//imu = new IMU(serial_port,update_rate_hz);
			imu = new IMU(serial_port,update_rate_hz);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: An error occured while initializing the MXP Board");
		}
	}
	
	
	//Singleton code
	private static MXPHelper singleton;
	public static MXPHelper getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new MXPHelper();
	}
}
