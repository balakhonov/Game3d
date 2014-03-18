package game3d.task;

import game3d.Animate3d;

public class StopMoveObjectTask implements Runnable {
	private Animate3d obj;

	public StopMoveObjectTask(Animate3d obj) {
		this.obj = obj;
	}

	@Override
	public void run() {
		System.out.println("StopMoveObjectTask: " + obj);
	}
}
