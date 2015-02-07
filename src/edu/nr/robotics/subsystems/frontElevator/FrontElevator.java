package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.I2C;
import edu.nr.robotics.subsystems.drive.LIDAR;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FrontElevator extends PIDSubsystem {

	public static FrontElevator singleton;
	/*
Front Elevator: (Controlled with laser/petentiometer)
1) Height: Adjust Tote #1
2) Height: Pick up Tote #2, release bin, grab bin, go to waiting height
3) Height: Big red button: Pick up Tote #2, go to waiting height
4) Height: Pick up Tote #2, go to score height
4) Height: Pick up Tote #1, go to score height
5) SCORE!
	Release bin
	Elevator down, once it touches bottom, down at 5%
	Go backward
6) Toggle top gripper
7) Elevator all the way down

-) Manual control
	 */
	
	//These need to be found
	//All heights in feet
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0;
	public static final double HEIGHT_WAITING = 0;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0;
	public static final double HEIGHT_SCORING = 0;
	public static final double HEIGHT_MAX = 5;
	
	public static final boolean USING_LASER = false;


	AnalogPotentiometer potentiometer;
	LIDAR laser;
    CANTalon talon;
    public FrontElevator(double p, double i, double d, double f) {
    	super("Front Elevator", p, i, d, f);
    	
    	talon = new CANTalon(RobotMap.frontElevatorTalon);
    	
		laser = new LIDAR(I2C.Port.kMXP);
		laser.start(); //Start polling
		
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_FRONT_ELEVATOR, HEIGHT_MAX, -HEIGHT_MAX/2);
		
        enable();
    }
    
    public static FrontElevator getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new FrontElevator(0, 0, 0, 0);
			SmartDashboard.putData("Front Elevator Subsystem", singleton);
		}
	}

    
    public void initDefaultCommand() {
        setDefaultCommand(new FrontElevatorIdleCommand());
    }
    
    protected double returnPIDInput() {
        if(USING_LASER)
        {
        	return laser.getDistance();
        }
        else
        {
        	return potentiometer.get();
        }
    }
    
    protected void usePIDOutput(double output) {
        talon.set(output);
    }
}
