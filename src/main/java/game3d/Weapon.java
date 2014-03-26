package game3d;

public class Weapon {
	/**
	 * shot per second
	 */
	private double shotFreq = 0.25;
	private double damage = 30;

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getShotFreq() {
		return shotFreq;
	}

	public void setShotFreq(double shotFreq) {
		this.shotFreq = shotFreq;
	}
}
