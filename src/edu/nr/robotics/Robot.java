
package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.AutonomousCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveAngleCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveIdleCommand;
import edu.nr.robotics.subsystems.drive.commands.DrivePositionCommand;
import edu.nr.robotics.subsystems.drive.commands.ResetEncoderCommand;
import edu.nr.robotics.subsystems.drive.commands.ResetFieldcentricCommand;
import edu.nr.robotics.subsystems.drive.commands.SetTalonProperties;
import edu.nr.robotics.subsystems.drive.commands.ZeroNavXCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{

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
		FrontElevator.init();
		
		SmartDashboard.putData("Drive at SmartDashboard Speed", new DriveForwardCommand(false));
		SmartDashboard.putData("Reset Field Values", new ResetFieldcentricCommand());
		SmartDashboard.putData("Drive 15 degrees", new DriveAngleCommand(15 * Math.PI/180));
		SmartDashboard.putData("Manual Drive Position", new DrivePositionCommand(false));
		SmartDashboard.putData("RoboRealms Drive Position", new DrivePositionCommand(true));
		SmartDashboard.putData(new DriveDistanceCommand(10, 0.3));
		SmartDashboard.putData(new SetTalonProperties());
		SmartDashboard.putData(new ResetEncoderCommand());
		SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData(new ZeroNavXCommand());
		
        // instantiate the command used for the autonomous period
        autonomousCommand = new DriveIdleCommand();
    }
	
    public void autonomousInit() 
    {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
        Scheduler.getInstance().run();
        
        //Update SmartDashboard info after the scheduler runs our command(s)
        putSubsystemDashInfo();
    }

    public void teleopInit() 
    {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
        Scheduler.getInstance().run();
        
        //Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
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
		//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
		Scheduler.getInstance().run();
		
		//Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
	}

    private void putSubsystemDashInfo()
    {
    	Drive.getInstance().putSmartDashboardInfo();
    	FrontElevator.getInstance().putSmartDashboardInfo();
    }
}
