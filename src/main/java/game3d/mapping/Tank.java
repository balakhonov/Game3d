package game3d.mapping;

import game3d.Animate3d;
import game3d.timeout.Connection;

import java.io.Serializable;


public class Tank extends Animate3d implements Health, Connection, Serializable {
	private static final long serialVersionUID = -4960448845412350167L;

	private double forwardSpeed = 0.05; // 1 point per 1000ms = 1s
	private double backSpeed = -0.03;
	private double rotateSpeed = 400;

	private volatile boolean moveForwardFlag = false;
	private volatile boolean moveBackFlag = false;
	private volatile boolean turnLeftFlag = false;
	private volatile boolean turnRightFlag = false;

	private String userId;
	private int tankType;
	private String objName;
	private double health;

	private boolean connected;

	public Tank(String userId, double health) {
		this.userId = userId;
		this.health = health;
		this.connected = true;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	@Override
	public double getHealth() {
		return health;
	}

	public String getSessionId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public int getTankType() {
		return tankType;
	}

	public void setTankType(int tankType) {
		this.tankType = tankType;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void moveForward() {
		moveX(this.getForwardSpeed());
		moveY(this.getForwardSpeed());
		moveZ(this.getForwardSpeed());
	}

	public void moveBack() {
		moveX(this.getBackSpeed());
		moveY(this.getBackSpeed());
		moveZ(this.getBackSpeed());
	}

	public void turnLeft() {
		double step = Math.PI / this.getRotateSpeed();

		rotateY((isMoveBackFlag()) ? -step : step);
	}

	public void turnRight() {
		double step = Math.PI / this.getRotateSpeed();
		rotateY((isMoveBackFlag()) ? step : -step);
	}

	public boolean isTurnRightFlag() {
		return turnRightFlag;
	}

	public void setTurnRightFlag(boolean turnRightFlag) {
		this.turnRightFlag = turnRightFlag;
	}

	public boolean isTurnLeftFlag() {
		return turnLeftFlag;
	}

	public void setTurnLeftFlag(boolean turnLeftFlag) {
		this.turnLeftFlag = turnLeftFlag;
	}

	public boolean isMoveForwardFlag() {
		return moveForwardFlag;
	}

	public void setMoveForwardFlag(boolean moveForwardFlag) {
		this.moveForwardFlag = moveForwardFlag;
	}

	public boolean isMoveBackFlag() {
		return moveBackFlag;
	}

	public void setMoveBackFlag(boolean moveBackFlag) {
		this.moveBackFlag = moveBackFlag;
	}

	public double getForwardSpeed() {
		return forwardSpeed;
	}

	public void setForwardSpeed(double forwardSpeed) {
		this.forwardSpeed = forwardSpeed;
	}

	public double getBackSpeed() {
		return backSpeed;
	}

	public void setBackSpeed(double backSpeed) {
		this.backSpeed = backSpeed;
	}

	public double getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(double rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}
}
