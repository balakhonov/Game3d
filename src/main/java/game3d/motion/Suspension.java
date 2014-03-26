package game3d.motion;

public class Suspension {
	private static double DEFAULT_ROTATE_SPEED = 400;

	private double rotateSpeed;

	public Suspension() {
		rotateSpeed = DEFAULT_ROTATE_SPEED;
	}

	public double getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(double rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}
}
