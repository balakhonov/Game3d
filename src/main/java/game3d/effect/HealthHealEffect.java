package game3d.effect;

import game3d.mapping.Health;

import java.util.Date;

public class HealthHealEffect extends Effect {
	private String name = "HealthHealEffect";
	private int period;
	private int index = 0;

	private Health health;
	private float points;

	/**
	 * 
	 * @param health
	 * @param duration
	 *            -milliseconds
	 * @param period
	 *            - seconds
	 */
	public HealthHealEffect(Health health, float points, long duration, int period) {
		super(duration);
		this.period = period * 1000;
		this.health = health;
		this.points = points;
	}

	@Override
	public void action() {
		long currentTime = new Date().getTime();
		long step = (currentTime - getStartDate()) / period;
		if (index < step) {
			health.setHealth(health.getHealth() + points);
			System.out.println("HealthHealEffect: " + health.getHealth());

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
