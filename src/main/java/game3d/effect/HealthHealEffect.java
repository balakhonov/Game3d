package game3d.effect;

import game3d.mapping.Health;


public class HealthHealEffect extends DelayAction {
	private String name = "HealthHealEffect";

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
		super(duration, period);
		this.health = health;
		this.points = points;
	}

	@Override
	public void delayAction() {
		health.setHealth(health.getHealth() + points);
		System.out.println("HealthHealEffect: " + health.getHealth());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "HealthHealEffect [name=" + name + ", health=" + health + ", points=" + points + "]";
	}

}
