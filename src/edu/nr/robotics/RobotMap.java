package edu.nr.robotics;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static int leftFront = 2;
	public static int rightFront = 0;
	public static int leftBack = 3;
	public static int rightBack = 1;
	
	
	public static int pneumaticsModule = 1;
	public static int doubleSolenoidForward = 0;
	public static int doubleSolenoidReverse = 1;
	
	public static int joystick = 0;

	public static int gyro = 0;
	
	public static int IRSensor = 1;
}
