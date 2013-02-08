package org.team3042.ultimateascent;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main Robot Class
 * @author jjkoletar
 */
public class EastviewRobot extends SimpleRobot {
    public void robotInit() {
        super.robotInit();
        System.out.println("This is the Competition Code used by Team 3042 for the FRC Ultimate Ascent Game");
        System.out.println("[INFO] Robot Enabled!");
    }

    public void autonomous() {
    }

    public void operatorControl() {
        System.out.println("[INFO] Entered Teleop");
        Bot.Hardware.DRIVETRAIN.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) { //Teleop Loop, executed while robot runs
            //Drivetrain
            if (Math.abs(Bot.Input.driver.getY()) < 0.1) {
                //Do a twist move
                SmartDashboard.putBoolean("Ran twist move", true);
                SmartDashboard.putBoolean("Ran travel move", false);
                Bot.Hardware.DRIVETRAIN.arcadeDrive(0.0, scale(Math.abs(Bot.Input.driver.getTwist()) > 0.3 ? -Bot.Input.driver.getTwist() : 0), true);
            } else {
                //Do only forward/back
                SmartDashboard.putBoolean("Ran twist move", false);
                SmartDashboard.putBoolean("Ran travel move", true);
                Bot.Hardware.DRIVETRAIN.arcadeDrive(scale(Bot.Input.driver.getY()), 0.0, true);
            }
            SmartDashboard.putNumber("Joy Y", scale(Bot.Input.driver.getY()));
            SmartDashboard.putNumber("Scale Value", ((-Bot.Input.driver.getThrottle() + 1) / 2));
            SmartDashboard.putNumber("Twist", Bot.Input.driver.getTwist());
            //Actuators
            SmartDashboard.putNumber("Actuator In", Bot.Input.gunner.getY());
            SmartDashboard.putNumber("Actuator Scaled", scaleActuators(Bot.Input.gunner.getY()));
            SmartDashboard.putBoolean("Actuator Limit Switch", Bot.Input.ACTUATOR_LIMIT_SWITCH.get());
            if (!Bot.Input.ACTUATOR_LIMIT_SWITCH.get()) {
               // Bot.Hardware.LEFT_ACTUATOR.set(scaleActuators(Bot.Input.gunner.getY()));
                Bot.Hardware.LEFT_ACTUATOR.set(0.0);
                Bot.Hardware.RIGHT_ACTUATOR.set(scaleActuators(Bot.Input.gunner.getY()));
            } else {
                Bot.Hardware.LEFT_ACTUATOR.set(0.0);
                Bot.Hardware.RIGHT_ACTUATOR.set(0.0);
            }
            Timer.delay(0.01);
        }
    }

    private double scale(double in) {
        //Throttle value is 1.0 through -1.0, so we gotta scale it to be between 1.0 and 0.0
        return ((-Bot.Input.driver.getThrottle() + 1) / 2) * in;
    }

    private double scaleActuators(double in) {
        return in * Bot.Constants.ACTUATOR_COEFFICIENT;
    }

    public void test() {
        System.out.println("[INFO] Entered Test");
        while (isTest() && isEnabled()) {
            Bot.Hardware.DRIVETRAIN.arcadeDrive(0.5, 0.0);
            Bot.Hardware.LEFT_JAG.set(0.5);
            Bot.Hardware.RIGHT_JAG.set(0.5);
            Timer.delay(0.01);
        }
        Bot.Hardware.LEFT_JAG.set(0);
        Bot.Hardware.RIGHT_JAG.set(0);
    }
}
