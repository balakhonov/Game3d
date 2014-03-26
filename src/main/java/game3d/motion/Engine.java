package game3d.motion;

public class Engine {
	private static final double DEFAULT_FORWARD_SPEED = 0.05;
	private static final double DEFAULT_BACK_SPEED = -0.03;

	/**
	 * 1 point per 1000ms = 1s
	 */
	private double forwardSpeed;
	private double backSpeed;

	public Engine() {
		forwardSpeed = DEFAULT_FORWARD_SPEED;
		backSpeed = DEFAULT_BACK_SPEED;
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
}
