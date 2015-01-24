
package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DriveIdleCommand;
import edu.nr.robotics.subsystems.drive.ResetFieldcentricCommand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    Command autonomousCommand;
    PowerDistributionPanel pdp;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
    	pdp = new PowerDistributionPanel();
		OI.init();
		Drive.init();
		
		SmartDashboard.putData("Drive Constant Speed", new DriveForwardCommand(0, false));
		SmartDashboard.putData("Reset Field Values", new ResetFieldcentricCommand());
		
        // instantiate the command used for the autonomous period
        autonomousCommand = new DriveIdleCommand();
    }
	
	public void disabledPeriodic() 
	{
		Scheduler.getInstance().run();
		FieldCentric.update();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
        Scheduler.getInstance().run();
        FieldCentric.update();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    boolean ultrasonicFlip = true;
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
        Drive.getInstance().sendEncoderInfo();
        
        FieldCentric.update();
        SmartDashboard.putNumber("PDP Voltage", pdp.getVoltage());
        
    	/*double newReading = 0;//Drive.getInstance().getUltrasonicValue();
    	if(newReading > 0 && newReading < 765)
    	{
    		SmartDashboard.putNumber("Ultrasonic Reading", newReading);
    	}
        ultrasonicFlip = !ultrasonicFlip;*/
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
