
package edu.nr.robotics;



import edu.nr.robotics.commands.ClearStickyFaultsCommand;
import edu.nr.robotics.commands.DriveDistanceCommand;
import edu.nr.robotics.commands.ResetEncodersCommand;
import edu.nr.robotics.commands.ResetGyroCommand;
import edu.nr.robotics.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
	public static PowerDistributionPanel pdp;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
		drivetrain = new Drivetrain();
		oi = new OI();
		
		SmartDashboard.putData("Drive 40 feet", DriveDistanceCommand.getTestingCommand());
		SmartDashboard.putData("Drive -40 feet", new DriveDistanceCommand(-25.5f, .4f));
		SmartDashboard.putData(new ResetEncodersCommand());
		SmartDashboard.putData(new ResetGyroCommand());
		SmartDashboard.putData("Clear PDP Faults", new ClearStickyFaultsCommand());
		
		drivetrain.resetGyro();
		
		pdp = new PowerDistributionPanel();
		
    }
	
    public void autonomousInit() 
    {
    }

    /**
     * Andrew was here
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
    	drivetrain.dashboardSensors();
    	updatePowerStats();
    }
    
    private void updatePowerStats()
    {
    	SmartDashboard.putNumber("PDP Voltage", pdp.getVoltage());
    	SmartDashboard.putNumber("PDP Channel 2", pdp.getCurrent(2));
    	SmartDashboard.putNumber("PDP Channel 3", pdp.getCurrent(3));
    	SmartDashboard.putNumber("PDP Channel 12", pdp.getCurrent(12));
    	SmartDashboard.putNumber("PDP Channel 13", pdp.getCurrent(13));
    }
}
