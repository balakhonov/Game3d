package game3d.mapping;

import game3d.Animate3d;

import java.io.Serializable;


public class Tank extends Animate3d implements Health, Serializable {
	private String userId;
	private double health;


	public Tank(String userId, double health) {
		this.userId = userId;
		this.health = health;
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

}
