package game3d.motion;

public class MotionController {

	private volatile boolean moveForwardFlag = false;
	private volatile boolean moveBackFlag = false;
	private volatile boolean turnLeftFlag = false;
	private volatile boolean turnRightFlag = false;
	private volatile boolean turnTowerLeftFlag = false;
	private volatile boolean turnTowerRightFlag = false;

	private Movable movable;

	public MotionController(Movable movable) {
		this.movable = movable;
	}

	public void move() {
		if (moveForwardFlag) {
			movable.moveForward();
		} else if (moveBackFlag) {
			movable.moveBack();
		}

		if (turnLeftFlag) {
			movable.turnLeft(moveBackFlag);
		} else if (turnRightFlag) {
			movable.turnRight(moveBackFlag);
		}

		if (movable.hasTower()) {
			if (turnTowerLeftFlag) {
				movable.turnTowerLeft();
			} else if (turnTowerRightFlag) {
				movable.turnTowerRight();
			}
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

	public boolean isTurnTowerLeftFlag() {
		return turnTowerLeftFlag;
	}

	public void setTurnTowerLeftFlag(boolean turnTowerLeftFlag) {
		this.turnTowerLeftFlag = turnTowerLeftFlag;
	}

	public boolean isTurnTowerRightFlag() {
		return turnTowerRightFlag;
	}

	public void setTurnTowerRightFlag(boolean turnTowerRightFlag) {
		this.turnTowerRightFlag = turnTowerRightFlag;
	}

	public Movable getMovable() {
		return movable;
	}

	private boolean isTowerMoving() {
		return (movable.hasTower() && (turnTowerLeftFlag || turnTowerRightFlag));
	}

	public boolean isMoving() {
		return (moveForwardFlag || moveBackFlag || turnLeftFlag || turnRightFlag || isTowerMoving());
	}
}
