package game3d.mapping;

import game3d.Animate3d;
import game3d.ConnectionTimeOut;
import game3d.ConnectionTimeoutHandler;
import game3d.websocketserver.handler.TankHandler;

import java.io.Serializable;


public class Tank extends Animate3d implements Health, ConnectionTimeOut, Serializable {
	private String userId;
	private int tankType;
	private String objName;
	private double health;

	private ConnectionTimeoutHandler cth;

	public Tank(String userId, double health) {
		this.userId = userId;
		this.health = health;

		cth = new ConnectionTimeoutHandler(this);
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	@Override
	public double getHealth() {
		return health;
	}

	public String getUserId() {
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

	@Override
	public void onConnectionTimeout() {
		System.err.println("Connection timeout");

		TankHandler.remove(1, this);
	}

	@Override
	public ConnectionTimeoutHandler getConnectionTimeoutHandler() {
		return cth;
	}

	@Override
	public void setConnectionTimeoutHandler(ConnectionTimeoutHandler cth) {
		this.cth = cth;
	}
}
