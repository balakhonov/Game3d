package game3d.task;

import game3d.Animate3d;
import game3d.websocketserver.handler.ObjectHandler;


public class MoveForwardObjectTask implements Runnable {
	private Animate3d obj;


	public MoveForwardObjectTask(Animate3d obj) {
		this.obj = obj;
	}


	@Override
	public void run() {
		double newX = obj.getpX() - Math.sin(obj.getrY()) * obj.getForwardSpeed();
		double newZ = obj.getpZ() - Math.cos(obj.getrY()) * obj.getForwardSpeed();
		obj.setpX(newX);
		obj.setpZ(newZ);
		ObjectHandler.updatePosition(1, obj);
	}
}
