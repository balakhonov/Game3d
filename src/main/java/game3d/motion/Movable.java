package game3d.motion;

import game3d.Tower;

public interface Movable {
	public void moveForward();

	public void moveBack();

	public void turnLeft(boolean inversion);

	public void turnRight(boolean inversion);
	
	public boolean hasTower();
	
	public Tower getTower();

	public void turnTowerLeft();

	public void turnTowerRight();
}
