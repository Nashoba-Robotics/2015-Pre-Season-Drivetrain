package edu.nr.robotics;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap 
{
	public static final int leftFrontTalon = 5;
	public static final int leftBackTalon = 4;
	
	public static final int rightFrontTalon = 2;
	public static final int rightBackTalon = 3;
	
	public static final int frontElevatorTalon = 87;//This needs to be set.
	
	public static final int pneumaticsModule = 1;
	public static final int doubleSolenoidForward = 0;
	public static final int doubleSolenoidReverse = 1;
	
	public static final int IRSensor = 1, IRSensor2 = 2;
	
	public static final int ENCODER1_A = 0, ENCODER1_B = 1;
	public static final int ENCODER2_A = 2, ENCODER2_B = 3;
	
	public static final int VEX_LEFT_ULTRASONIC_PING = 6, VEX_LEFT_ULTRASONIC_ECHO = 9;
	public static final int VEX_RIGHT_ULTRASONIC_PING = 4, VEX_RIGHT_ULTRASONIC_ECHO = 5;
	
	public static final int BUMPER_BUTTON_1 = 7, BUMPER_BUTTON_2 = 8;
	
	public static final int LASER_RANGING_MODULE = 0x62; //Provided by documentation.

	//SET THIS
	public static final int POTENTIOMETER_FRONT_ELEVATOR = 0;
}
