package game3d.effect;

import game3d.mapping.Health;


public class HealthBurnEffect extends DelayAction {
	private String name = "HealthBurnEffect";

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
		super(duration, period);
		this.health = health;
		this.damage = damage;
	}

	@Override
	public void delayAction() {
		health.setHealth(health.getHealth() - damage);
		System.out.println("HealthBurnEffect: " + health.getHealth());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "HealthBurnEffect [name=" + name + ", health=" + health + ", damage=" + damage + "]";
	}

}
