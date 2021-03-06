package org.team3042.ultimateascent;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Class full of various constants
 * @author jjkoletar
 */
public final class Bot {
    private Bot() {} //no instances
    public static final class Hardware {
        private Hardware() {}
        public static final Jaguar LEFT_JAG = new Jaguar(1);
        public static final Jaguar RIGHT_JAG = new Jaguar(2);
        public static final RobotDrive DRIVETRAIN = new RobotDrive(LEFT_JAG, RIGHT_JAG);
        public static final Jaguar LEFT_ACTUATOR = new Jaguar(4);
        public static final Jaguar RIGHT_ACTUATOR = new Jaguar(3);
        public static final Relay LEFT_LIGHTS = new Relay(5);
        public static final Relay RIGHT_LIGHTS = new Relay(8);
        public static final Relay BAR_ASSIST = new Relay(7);
    }
    public static final class Input {
        private Input() {}
        public static final Joystick driver = new Joystick(1);
        public static final Joystick gunner = new Joystick(2);
        //A_L_S is true when circuit is closed, and switch is not triggered, false when switch triggered or circuit open
        public static final DigitalInput RIGHT_ACTUATOR_LIMIT_SWITCH = new DigitalInput(14);
        public static final DigitalInput LEFT_ACTUATOR_LIMIT_SWITCH = new DigitalInput(13);
    }
    public static final class Constants {
        private Constants() {}
        public static final double ACTUATOR_COEFFICIENT = 1.0;
        public static final int LEFT_ACTUATOR_OVERRIDE_BUTTON = 8;
        public static final int RIGHT_ACTUATOR_OVERRIDE_BUTTON = 9;
    }
}
