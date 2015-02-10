package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.subsystems.drive.I2C;
import edu.nr.robotics.subsystems.drive.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LIDAR
{
	private I2C i2c;
	private byte[] distance;
	private Thread updater;
	
	private final int LIDAR_ADDR = 0x62;
	private final int LIDAR_CONFIG_REGISTER = 0x00;
	private final int LIDAR_DISTANCE_REGISTER = 0x8f;
	
	//Laser updates twice as fast as the command loop, so we average the previous two values
	private int[] savedValues = {-1, -1};
	private int savedValuesIndex = 0;
	
	public LIDAR(Port port) 
	{
		i2c = new I2C(port, LIDAR_ADDR);
		
		distance = new byte[2];
	}
	
	double conversionToInches = 0.393701; //inches/cm
	public double getDistanceInches()
	{
		return getDistanceCentimeters() * conversionToInches;
	}
	
	public double getDistanceFeet()
	{
		return getDistanceInches() / 12d;
	}
	
	// Distance in cm
	public double getDistanceCentimeters() 
	{
		double sumCount = 0;
		double sum = 0;
		for(int i = 0; i < savedValues.length; i++)
		{
			//If a value is -1, then that array spot has not received a value yet
			if(savedValues[i] != -1)
			{
				sum += savedValues[i];
				sumCount++;
			}
		}
		
		if(sumCount != 0)
			return sum/sumCount;
		else
			return 0;
	}

	// Start polling
	public void start()
	{
		if(updater == null)
			updater = new Thread(new LIDARUpdater());
		
		if(!updater.isAlive() && !updater.isInterrupted())
			updater.start();
	}
	
	// Start polling for period in milliseconds
	public void start(int period) 
	{
		updater = new Thread(new LIDARUpdater(period));
		start();
	}
	
	public void stop() 
	{
		if(updater != null)
			updater.interrupt();
		updater = null; //Need to make a new thread when we start again since we just interrupted the last one
	}
	
	boolean previousWriteSuccess = false;
	
	private int writeErrors = 0, readErrors = 0;
	private int readSuccess = 0;
	// Update distance variable
	public void update()
	{
		if(previousWriteSuccess)
		{
			boolean aborted = i2c.read(LIDAR_DISTANCE_REGISTER, 2, distance); // Read in measurement
			SmartDashboard.putBoolean("Laser Read Success", !aborted);
			if(aborted)
			{
				readErrors++;
			}
			else
			{
				readSuccess++;
			}
			
			int newValue = (int)Integer.toUnsignedLong(distance[0] << 8) + Byte.toUnsignedInt(distance[1]);
			savedValues[savedValuesIndex % 4] = newValue;
			savedValuesIndex++;
			
			
			SmartDashboard.putNumber("Laser Read Errors", readErrors);
			SmartDashboard.putNumber("Laser Read Success Num", readSuccess);
		}
		
		previousWriteSuccess = !i2c.write(LIDAR_CONFIG_REGISTER, 0x04);
		if(!previousWriteSuccess)
			writeErrors++;
		SmartDashboard.putBoolean("Laser Write Success", previousWriteSuccess);
		SmartDashboard.putNumber("Laser Write Errors", writeErrors);
	}
	
	// Timer task to keep distance updated
	private class LIDARUpdater implements Runnable 
	{
		private int period = 1000/100; //Default of 100Hz
		
		public LIDARUpdater() //Provides a default value for period
		{
			
		}
		
		public LIDARUpdater(int period)
		{
			this.period = period;
		}
		
		@Override
		public void run() 
		{
			while(true)
			{
				update();
				try 
				{
					Thread.sleep(period);
				} 
				catch (InterruptedException e)
				{
					break;
				}
			}
			System.out.println("Laser Polling Stopped");
		}
	}
}