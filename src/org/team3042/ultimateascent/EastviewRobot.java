package org.team3042.ultimateascent;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main Robot Class
 *
 * @author jjkoletar
 */
public class EastviewRobot extends SimpleRobot {
    private boolean leftBlinkInputLock = false;
    private double leftBlinkTimer = -1;
    private boolean rightBlinkInputLock = false;
    private double rightBlinkTimer = -1;
    private int leftLimitSignal = -1;
    private int rightLimitSignal = -1;

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
            SmartDashboard.putBoolean("Right Actuator Limit Switch", Bot.Input.RIGHT_ACTUATOR_LIMIT_SWITCH.get());
            SmartDashboard.putBoolean("Left Actuator Limit Switch", Bot.Input.LEFT_ACTUATOR_LIMIT_SWITCH.get());
            if (Bot.Input.gunner.getRawButton(Bot.Constants.LEFT_ACTUATOR_OVERRIDE_BUTTON)) {
                SmartDashboard.putString("Actuator Style", "Left");
                Bot.Hardware.LEFT_ACTUATOR.set(limitSwitch(Bot.Input.LEFT_ACTUATOR_LIMIT_SWITCH, scaleActuators(Bot.Input.gunner.getY())));
                Bot.Hardware.RIGHT_ACTUATOR.set(0.0);
            } else if (Bot.Input.gunner.getRawButton(Bot.Constants.RIGHT_ACTUATOR_OVERRIDE_BUTTON)) {
                SmartDashboard.putString("Actuator Style", "Right");
                Bot.Hardware.LEFT_ACTUATOR.set(0.0);
                Bot.Hardware.RIGHT_ACTUATOR.set(limitSwitch(Bot.Input.RIGHT_ACTUATOR_LIMIT_SWITCH, scaleActuators(Bot.Input.gunner.getY())));
            } else {
                SmartDashboard.putString("Actuator Style", "Both");
                Bot.Hardware.LEFT_ACTUATOR.set(limitSwitch(Bot.Input.LEFT_ACTUATOR_LIMIT_SWITCH, scaleActuators(Bot.Input.gunner.getY())));
                Bot.Hardware.RIGHT_ACTUATOR.set(limitSwitch(Bot.Input.RIGHT_ACTUATOR_LIMIT_SWITCH, scaleActuators(Bot.Input.gunner.getY())));
            }
            if (!leftBlinkInputLock && Bot.Input.gunner.getRawButton(11)) {
                leftBlinkTimer = leftBlinkTimer >= 0 ? -1 : 0;
                leftBlinkInputLock = true;
            }
            if (leftBlinkInputLock && !Bot.Input.gunner.getRawButton(11)) {
                leftBlinkInputLock = false;
            }
            if (!rightBlinkInputLock && Bot.Input.gunner.getRawButton(6)) {
                rightBlinkTimer = rightBlinkTimer >= 0 ? -1 : 0;
                rightBlinkInputLock = true;
            }
            if (rightBlinkInputLock && !Bot.Input.gunner.getRawButton(6)) {
                rightBlinkInputLock = false;
            }
            if (leftBlinkTimer >= 0) {
                leftBlinkTimer += 0.01;
                if (leftBlinkTimer >= 0.8) {
                    toggle(Bot.Hardware.LEFT_LIGHTS);
                    leftBlinkTimer = 0;
                }
            }
            if (Bot.Input.gunner.getRawButton(10)) {
                Bot.Hardware.LEFT_LIGHTS.set(Relay.Value.kForward);
            } else if (leftBlinkTimer < 0) {
                Bot.Hardware.LEFT_LIGHTS.set(Relay.Value.kOff);
            }
            if (rightBlinkTimer >= 0) {
                rightBlinkTimer += 0.01;
                if (rightBlinkTimer >= 0.8) {
                    toggle(Bot.Hardware.RIGHT_LIGHTS);
                    rightBlinkTimer = 0;
                }
            }
            if (Bot.Input.gunner.getRawButton(7)) {
                Bot.Hardware.RIGHT_LIGHTS.set(Relay.Value.kForward);
            } else if (rightBlinkTimer < 0) {
                Bot.Hardware.RIGHT_LIGHTS.set(Relay.Value.kOff);
            }
            if (Bot.Input.RIGHT_ACTUATOR_LIMIT_SWITCH.get()) {
                leftLimitSignal++;
                if (leftLimitSignal == 30) {
                    toggle(Bot.Hardware.LEFT_LIGHTS);
                    leftLimitSignal = 0;
                }
            } else {
                if (leftBlinkTimer == -1 && !Bot.Input.gunner.getRawButton(10)) {
                    Bot.Hardware.LEFT_LIGHTS.set(Relay.Value.kOff);
                }
            }
            if (Bot.Input.LEFT_ACTUATOR_LIMIT_SWITCH.get()) {
                rightLimitSignal++;
                if (rightLimitSignal == 30) {
                    toggle(Bot.Hardware.RIGHT_LIGHTS);
                    rightLimitSignal = 0;
                }
            } else {
                if (rightBlinkTimer == -1 && !Bot.Input.gunner.getRawButton(7)) {
                    Bot.Hardware.RIGHT_LIGHTS.set(Relay.Value.kOff);
                }
            }
            if (Bot.Input.gunner.getRawButton(3)) {
                Bot.Hardware.BAR_ASSIST.set(Relay.Value.kForward);
            } else if (Bot.Input.gunner.getRawButton(2)) {
                Bot.Hardware.BAR_ASSIST.set(Relay.Value.kReverse);
            } else {
                Bot.Hardware.BAR_ASSIST.set(Relay.Value.kOff);
            }
            Timer.delay(0.01);
        }
    }

    private void toggle(Relay light) {
        light.set(light.get().equals(Relay.Value.kOff) ? Relay.Value.kForward : Relay.Value.kOff);
    }

    private double limitSwitch(DigitalInput theSwitch, double value) {
        return value; //TODO: Disabled for KBII
        //return theSwitch.get() ? (value < 0 ? value : 0) : value;
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