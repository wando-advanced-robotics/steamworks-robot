package org.usfirst.frc.team4533.robot;

import org.usfirst.frc.team4533.robot.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
public class OI {
	Joystick stick = new Joystick(RobotMap.JOYSTICK_PORT);
	private static OI INSTANCE;
	JoystickButton climbBtn = new JoystickButton(stick, RobotMap.A_BUTTON);
	JoystickButton flyBtn = new JoystickButton(stick, RobotMap.RIGHT_BUMPER);
	JoystickButton doorBtn = new JoystickButton(stick, RobotMap.RIGHT_TRIGGER);
	private OI() {
		//while held, the climb button makes the robot climb, and then sets the voltage to 0 when the button is released
		climbBtn.whileHeld(new ClimbCommand());
		climbBtn.whenReleased(new BrakeCommand());
		flyBtn.toggleWhenPressed(new ShootCommand());
		doorBtn.whileHeld(new DoorOpen());
	}
	public static OI getInstance(){
		return INSTANCE;
	}
	public static void initialize() {
		if (INSTANCE == null) {
			INSTANCE = new OI();
		}
	}
}
