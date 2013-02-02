package org.team3042.ultimateascent;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * Main Robot Class
 * @author jjkoletar
 */
public class EastviewRobot extends SimpleRobot {
    public void autonomous() {

    }

    public void operatorControl() {
        Bot.Hardware.DRIVETRAIN.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) { //Teleop Loop, executed while robot runs
            //Drivetrain
            Bot.Hardware.DRIVETRAIN.arcadeDrive(scale(Bot.Input.driver.getY()), scale(Bot.Input.driver.getTwist()));

            Timer.delay(0.01);
        }
    }

    private double scale(double in) {
        //Throttle value is 1.0 through -1.0, so we gotta scale it to be between 1.0 and 0.0
        return ((Bot.Input.driver.getThrottle() + 1) / 2) * in;
    }

    public void test() {

    }
}
