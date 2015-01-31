package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.I2C;

public class LaserRangingModule extends I2C {

	public LaserRangingModule(Port port, int deviceAddress) {
		super(port, deviceAddress);
	    write(0x00,0x00); // reset device to defaults for distance measurment
	    
	}

	public boolean read(int register, int length, byte[] buffer)
	{
		buffer = new byte[length];
		while(super.read(register, length, buffer))
		{
			try {
				wait(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
		
	}
	
	public boolean write(int register, int data)
	{
		while(super.write(register, data))
		{
			try {
				wait(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/*
	 * Get 2-byte distance from sensor and combine into single 16-bit int
	 */
	public int getDistance()
	{
		write(0xC4,0x04); // Write 0x04 to register 0x00 to start getting distance readings
		byte[] myArray = new byte[2]; // array to store bytes from read function
		read(0x8f,2,myArray); // Read 2 bytes from 0x8f
		int distance = (myArray[0] << 8) + myArray[1];  // Shift high byte [0] 8 to the left and add low byte [1] to create 16-bit int
		return distance;

	}
	
	/*
	 * Average readings of distance
	 * int numberOfReadings - the number of readings you want to average (0-9 are possible, 2-9 are reccomended)
	 */
	public int getDistanceAverage(int numberOfReadings){ 
	  if(numberOfReadings < 2){
	    numberOfReadings = 2; // If the number of readings to be taken is less than 2, default to 2 readings
	  }
	  int sum = 0; // Variable to store sum
	  for(int i = 0; i < numberOfReadings; i++){ 
	      sum = sum + getDistance(); // Add up all of the readings
	  }
	  sum = sum/numberOfReadings; // Divide the total by the number of readings to get the average
	  return(sum);
	}


}
