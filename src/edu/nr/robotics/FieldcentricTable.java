package edu.nr.robotics;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class FieldcentricTable 
{
	private static NetworkTable table;
	
	public static void init()
	{
		table = NetworkTable.getTable("FieldCentric");
	}
	
	public static void update()
	{
		table.putNumber("x", Fieldcentric.getRobotInstance().getX());
        table.putNumber("y", Fieldcentric.getRobotInstance().getY());
        table.putNumber("angle", Fieldcentric.getRobotInstance().getFieldCentricAngleRadians());
	}
}
