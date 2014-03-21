package game3d.mapping;

import game3d.Animate3d;
import game3d.timeout.Connection;

import java.io.Serializable;


public class Tank extends Animate3d implements Health, Connection, Serializable {
	private static final long serialVersionUID = -4960448845412350167L;

	private String userId;
	private int tankType;
	private String objName;
	private double health;

	// private TimeoutHandler cth;
	private boolean connected;

	public Tank(String userId, double health) {
		this.userId = userId;
		this.health = health;
		this.connected = true;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	@Override
	public double getHealth() {
		return health;
	}

	public String getSessionId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public int getTankType() {
		return tankType;
	}

	public void setTankType(int tankType) {
		this.tankType = tankType;
	}

	// @Override
	// public void onExpired() {
	// System.err.println("Connection timeout");
	//
	// TankHandler.remove(1, this);
	// }
	//
	// @Override
	// public TimeoutHandler getTimeoutHandler() {
	// return cth;
	// }
	//
	// @Override
	// public void setTimeoutHandler(TimeoutHandler cth) {
	// this.cth = cth;
	// }

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
