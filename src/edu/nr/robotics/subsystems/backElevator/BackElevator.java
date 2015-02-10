package edu.nr.robotics.subsystems.backElevator;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevator extends PIDSubsystem {

    static BackElevator singleton;
    
    /*
     * Rear Elevator: (Controlled with petentiometer)
     * 1) Height: Hold
     * 2) Height: Obtain off step
     * 3) Height: Obtain off floor / Put bin down
     * 4) Height: Closed
     */
    
	AnalogPotentiometer potentiometer;
    CANTalon talon;
    
    public BackElevator() {
    	super("Back Elevator", 0, 0, 0);
        // setSetpoint() -  Sets where the PID controller should move the system to
        enable();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
    	return 0.0;
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    }
	public static BackElevator getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new BackElevator();
			SmartDashboard.putData("Back Elevator Subsystem", singleton);
		}
	}

	
}
