package game3d.effect;

import java.util.Date;


public abstract class DelayAction extends ActionImpl {
	private int period;
	private int index = 0;

	/**
	 * 
	 * @param period
	 */
	public DelayAction(int period) {
		this(0, period);
	}

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

	public abstract void action();

	@Override
	public final void onAction() {
		long currentTime = new Date().getTime();
		long step = (currentTime - getStartDate()) / period;
		if (index < step) {
			action();
			++index;
		}
	}

	@Override
	public void onExpired() {
		// TODO Auto-generated method stub
	}
}
