package game3d.effect;

import game3d.mapping.Health;

import java.util.Date;

public class HealthBurnEffect extends Effect {
	private String name = "HealthBurnEffect";
	private int period;
	private int index = 0;

	private Health health;
	private float damage;

	/**
	 * 
	 * @param health
	 * @param duration
	 *            -milliseconds
	 * @param period
	 *            - seconds
	 */
	public HealthBurnEffect(Health health, float damage, long duration, int period) {
		super(duration);
		this.period = period * 1000;
		this.health = health;
		this.damage = damage;
	}

	@Override
	public void action() {
		long currentTime = new Date().getTime();
		long step = (currentTime - getStartDate()) / period;
		if (index < step) {
			health.setHealth(health.getHealth() - damage);
			System.out.println("HealthBurnEffect: " + health.getHealth());

			++index;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "HealthBurnEffect [name=" + name + ", period=" + period + "]";
	}

}
