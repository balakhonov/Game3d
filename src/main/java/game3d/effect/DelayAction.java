package game3d.effect;

import java.util.Date;


public abstract class DelayAction extends Action {
	private int period;
	private int index = 0;

	/**
	 * 
	 * @param duration
	 *            -milliseconds
	 * @param period
	 *            - seconds
	 */
	public DelayAction(long duration, int period) {
		super(duration);
		this.period = period * 1000;
	}

	public abstract void delayAction();

	@Override
	public final void action() {
		long currentTime = new Date().getTime();
		long step = (currentTime - getStartDate()) / period;
		if (index < step) {
			delayAction();
			++index;
		}
	}
}
