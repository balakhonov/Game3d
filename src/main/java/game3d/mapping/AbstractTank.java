package game3d.mapping;

import game3d.Animate3d;
import game3d.Tower;
import game3d.Weapon;
import game3d.motion.Engine;
import game3d.motion.Movable;
import game3d.motion.Suspension;
import game3d.timeout.Connection;

import java.io.Serializable;


public class AbstractTank extends Animate3d implements Health, Connection, Serializable, Movable {
	private static final long serialVersionUID = -4960448845412350167L;

	private String userId;
	private int tankType;
	private String objName;
	private double totalHealth;
	private double health;

	private Suspension suspension;
	private Engine engine;
	private Tower tower;
	private Weapon weapon;

	private boolean connected;

	public AbstractTank(String userId, double health, Suspension suspension, Engine engine,
			Weapon weapon) {
		this(userId, health, suspension, engine, weapon, null);
	}

	public AbstractTank(String userId, double health, Suspension suspension, Engine engine,
			Weapon weapon, Tower tower) {
		this.userId = userId;
		this.totalHealth = health;
		this.health = health;
		this.suspension = suspension;
		this.engine = engine;
		this.setWeapon(weapon);
		this.tower = tower;

		this.connected = true;
	}

	public Suspension getSuspension() {
		return suspension;
	}

	public void setSuspension(Suspension suspension) {
		this.suspension = suspension;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
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

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public void moveForward() {
		moveX(engine.getForwardSpeed());
		moveY(engine.getForwardSpeed());
		moveZ(engine.getForwardSpeed());
	}

	@Override
	public void moveBack() {
		moveX(engine.getBackSpeed());
		moveY(engine.getBackSpeed());
		moveZ(engine.getBackSpeed());
	}

	@Override
	public void turnLeft(boolean inversion) {
		double step = Math.PI / suspension.getRotateSpeed();
		rotateY((inversion) ? -step : step);
	}

	@Override
	public void turnRight(boolean inversion) {
		double step = Math.PI / suspension.getRotateSpeed();
		rotateY((inversion) ? step : -step);
	}

	@Override
	public boolean hasTower() {
		return (tower != null);
	}

	@Override
	public Tower getTower() {
		return tower;
	}

	@Override
	public void turnTowerLeft() {
		double step = Math.PI / tower.getRotateSpeed();
		tower.rotateY(step);
	}

	@Override
	public void turnTowerRight() {
		double step = Math.PI / tower.getRotateSpeed();
		tower.rotateY(-step);
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public void setTotalHealth(double totalHealth) {
		this.totalHealth = totalHealth;
	}

	@Override
	public double getTotalHealth() {
		return totalHealth;
	}
}
