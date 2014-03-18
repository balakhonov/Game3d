package game3d.task;

import game3d.mapping.Tank;
import game3d.websocketserver.handler.TankHandler;
import org.apache.log4j.Logger;


public class InitTankTask implements Runnable {
	private static final Logger LOG = Logger.getLogger(InitTankTask.class);
	private static final int POSITION_STEP = 1;
	private Tank obj;


	public InitTankTask(Tank obj) {
		this.obj = obj;
	}


	@Override
	public void run() {
		System.out.println("MoveBackObjectTask: " + obj);

		LOG.warn("move back: " + obj);
		double newX = obj.getpX() + Math.sin(obj.getrY()) * POSITION_STEP;
		double newZ = obj.getpZ() + Math.cos(obj.getrY()) * POSITION_STEP;
		obj.setpX(newX);
		obj.setpZ(newZ);

		TankHandler.init(1, obj);
	}
}
