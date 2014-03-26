package game3d;

public class Tower extends Animate3d {
	public static final double DEFAULT_ROTATE_SPEED = 400;

	private double rotateSpeed;

	public Tower() {
		setRotateSpeed(DEFAULT_ROTATE_SPEED);
	}

	public double getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(double rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}
}
