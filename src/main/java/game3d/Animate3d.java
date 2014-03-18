package game3d;

public class Animate3d extends Object3d {
	private double forwardSpeed = 0.05; // 1 point per 1000ms = 1s
	private double backSpeed = 0.3;
	private double rotateSpeed = 400;

	private volatile boolean moveForwardFlag = false;
	private volatile boolean moveBackFlag = false;
	private volatile boolean turnLeftFlag = false;
	private volatile boolean turnRightFlag = false;

	public void moveForward() {
		this.setpX(this.getpX() - Math.sin(this.getrY()) * this.getForwardSpeed());
		this.setpZ(this.getpZ() - Math.cos(this.getrY()) * this.getForwardSpeed());
	}

	public void moveBack() {
		this.setpX(this.getpX() + Math.sin(this.getrY()) * this.getForwardSpeed());
		this.setpZ(this.getpZ() + Math.cos(this.getrY()) * this.getForwardSpeed());
	}

	public void turnLeft() {
		double step = Math.PI/ this.getRotateSpeed();
		if (isMoveBackFlag()) {
			this.setrY(this.getrY() - step);
		} else {
			this.setrY(this.getrY() + step);
		}
	}

	public void turnRight() {
		double step = Math.PI / this.getRotateSpeed();
		if (isMoveBackFlag()) {
			this.setrY(this.getrY() + step);
		} else {
			this.setrY(this.getrY() - step);
		}
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
