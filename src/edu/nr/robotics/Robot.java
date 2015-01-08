
package org.usfirst.frc.team1768.robot;



import org.usfirst.frc.team1768.robot.commands.DriveDistanceCommand;
import org.usfirst.frc.team1768.robot.commands.ResetEncodersCommand;
import org.usfirst.frc.team1768.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot 
{
	public static Drivetrain drivetrain;
	
	public static OI oi;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
		oi = new OI();
		drivetrain = new Drivetrain();
		SmartDashboard.putData("Drive 10 feet", new DriveDistanceCommand(40, .8f));
		SmartDashboard.putData(new ResetEncodersCommand());
    }
	
    public void autonomousInit() 
    {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    public void teleopInit() {
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
        updateDashboard();
    }
    

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit()
    {

    }
    
    public void disabledPeriodic() 
    {
		Scheduler.getInstance().run();
		updateDashboard();
	}
    
    private void updateDashboard()
    {
    	SmartDashboard.putNumber("Encoder 1", drivetrain.getEncoder1DistanceInches());
    	SmartDashboard.putNumber("Encoder 2", drivetrain.getEncoder2DistanceInches());
    	SmartDashboard.putNumber("Average Encoder Distance", drivetrain.getAverageEncoderDistance());
    	SmartDashboard.putNumber("Gyro", drivetrain.getAngle());
    }
}
