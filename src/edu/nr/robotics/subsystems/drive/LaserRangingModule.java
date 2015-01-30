package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.I2C;

public class LaserRangingModule extends I2C {

	public LaserRangingModule(Port port, int deviceAddress) {
		super(port, deviceAddress);
	}

	public byte[] read()
	{
		byte[] buffer = new byte[7];
		while(super.read(0xC5, buffer.length, buffer))
		{
			try {
				wait(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return buffer;
		
	}
	
	public void write(int data)
	{
		while(super.write(0xC4, data))
		{
			try {
				wait(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
