package game3d.motion;

public interface Movable {
	public void moveForward();

	public void moveBack();

	public void turnLeft(boolean inversion);

	public void turnRight(boolean inversion);
}
