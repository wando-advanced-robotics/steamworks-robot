
package org.usfirst.frc.team4533.robot.subsystems;

import org.usfirst.frc.team4533.robot.Robot;
import org.usfirst.frc.team4533.robot.RobotMap;
import org.usfirst.frc.team4533.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team4533.robot.utils.SensorData;
import org.usfirst.frc.team4533.robot.utils.SensorUtilities;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSystem extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private static DriveSystem INSTANCE;

	CANTalon rightMaster;
	CANTalon leftMaster;
	CANTalon leftSlave;
	CANTalon rightSlave;
	static DigitalInput di;
	static AnalogInput usSensor;
	static SensorData data;
	static String name;
	static String value;
	static String unit;
	Joystick stick;
	

	/*
	 * 
	 * Note on all this master/slave mumbo jumbo- This works by initiating all
	 * four motors (right master & slave, left master & slave), then setting the
	 * two slave motors to follow input from their masters. The lingo is a
	 * little weird but basically, it just makes the slaves do everything the
	 * master does.
	 */
	public DriveSystem() {
		// this sets the right master motor channel
		rightMaster = new CANTalon(RobotMap.Motor_Right_Master);
		// this sets the left master motor channel
		leftMaster = new CANTalon(RobotMap.Motor_Left_Master);
		// sets the right slave motor channel
		rightSlave = new CANTalon(RobotMap.Motor_Right_Slave);
		// changes the control mode to follower of the master motor
		rightSlave.changeControlMode(TalonControlMode.Follower);
		// sets the master motor to the right master
		rightSlave.set(RobotMap.Motor_Right_Master);
		// sets the left slave motor channel
		leftSlave = new CANTalon(RobotMap.Motor_Left_Slave);
		// changes the control mode to be a follower of the master motor
		leftSlave.changeControlMode(TalonControlMode.Follower);
		// sets the master motor to the left master
		leftSlave.set(RobotMap.Motor_Left_Master);
		usSensor = new AnalogInput(RobotMap.FRONT_SENSOR_PORT);
		di = new DigitalInput(RobotMap.GEAR_SENSOR);
		stick = new Joystick(RobotMap.JOYSTICK_PORT);
		
		if(RobotMap.useRampedDriving){
			leftMaster.setVoltageRampRate(12);
			rightMaster.setVoltageRampRate(12);
		}
	}

	public static void initialize() {
		if (INSTANCE == null) {
			INSTANCE = new DriveSystem();
		}
	}

	public static DriveSystem getInstance() {
		return INSTANCE;
	}

	public void drive(double left, double right) {
		this.leftMaster.set(left);
		this.leftSlave.set(RobotMap.Motor_Left_Master); 
		this.rightMaster.set(-right);
		this.rightSlave.set(RobotMap.Motor_Right_Master);
	}

	public void DriveWithJoystick(Joystick driver) {
		
		double left = driver.getRawAxis(3);
		double right = driver.getY();
		
		if(RobotMap.useTurboTrigger){
			if(stick.getRawButton(RobotMap.LEFT_BUMPER)){
				this.drive(left * -1, right * -1);
			}else{
				this.drive(left * -0.5, right * -0.5);
			}
		}else{
			if(stick.getRawButton(RobotMap.LEFT_BUMPER)){
				this.drive(left * -0.5, right * -0.5);
			}else{
				this.drive(left * -1, right * -1);
			}
		}
		
	
		
	}

	public static double getControlSpeed(double x) {
		double control = 0.0;
		control = Math.copySign(1.0, x) * (Math.pow(Math.abs(x), Robot.seed));
		
		double scalingFactor = Robot.maxSpeed/100.0;
		return (control*scalingFactor);
	}
	public void forward(double value) {
		this.drive(value, value);
	}

	public void forward() {
		this.forward(1.0);
	}

	public void backward(double value) {
		this.drive(value, value);
	}

	public void backward() {
		this.backward(-1.0);
	}

	public void stop() {
		this.drive(0.0, 0.0);
	}

	public void turnLeft(double value1, double value2) {
		this.drive(value1, value2);
	}

	public void turnLeft() {
		this.turnLeft(.5, -.5);
	}

	public void turnRight(double value1, double value2) {
		this.drive(value1, value2);
	}

	public void turnRight() {
		this.turnRight(-.5, .5);
	}

	public static boolean hasGear() {
		return !(di.get());
	}

	public void initDefaultCommand() {
		this.setDefaultCommand(new DriveWithJoystick());
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
