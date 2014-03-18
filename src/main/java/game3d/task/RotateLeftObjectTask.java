package game3d.task;

import game3d.Animate3d;
import game3d.websocketserver.handler.ObjectHandler;


public class RotateLeftObjectTask implements Runnable {
	private Animate3d obj;


	public RotateLeftObjectTask(Animate3d obj) {
		this.obj = obj;
	}


	@Override
	public void run() {
		double step = 3.14 / obj.getRotateSpeed();

		obj.setrY(obj.getrY() + step);
		ObjectHandler.updateRotation(1, obj);
	}
}
