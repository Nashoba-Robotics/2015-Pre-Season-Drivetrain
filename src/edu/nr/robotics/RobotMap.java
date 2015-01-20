package edu.nr.robotics;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap 
{
	public static final int leftFrontTalon = 2;
	public static final int rightFrontVictor = 0;
	public static final int leftBackTalon = 3;
	public static final int rightBackVictor = 1;
	
	
	public static final int pneumaticsModule = 1;
	public static final int doubleSolenoidForward = 0;
	public static final int doubleSolenoidReverse = 1;
	
	public static final int gyro = 0;
	
	public static final int IRSensor = 1, IRSensor2 = 2;
	
	public static final int ENCODER1_A = 0, ENCODER1_B = 1;
	public static final int ENCODER2_A = 2, ENCODER2_B = 3;
	
	public static final int BUMPER_BUTTON_1 = 7, BUMPER_BUTTON_2 = 8;
}
