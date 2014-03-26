package game3d.motion;

public class MotionController {

	private volatile boolean moveForwardFlag = false;
	private volatile boolean moveBackFlag = false;
	private volatile boolean turnLeftFlag = false;
	private volatile boolean turnRightFlag = false;

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

	public Movable getMovable() {
		return movable;
	}

	public boolean isMoving() {
		return (moveForwardFlag || moveBackFlag || turnLeftFlag || turnRightFlag);
	}
}
