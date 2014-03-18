package game3d.effect;

import java.util.Date;

public abstract class Effect implements Runnable {
	private long startDate;
	private long endDate;
	private boolean stopped;

	public Effect(long duration) {
		if (duration < 0) {
			throw new IllegalStateException("duration should be > 0");
		}

		this.startDate = new Date().getTime();
		this.endDate = startDate + duration;
	}

	protected abstract void action();

	@Override
	public void run() {
		if (new Date().getTime() >= endDate) {
			stopped = true;
		}
		action();
	}

	public Effect(long startDate, long endDate) {
		if (startDate < 0) {
			throw new IllegalStateException("startDate should be > 0");
		}
		if (endDate < 0) {
			throw new IllegalStateException("endDate should be > 0");
		}

		this.endDate = endDate;
		this.startDate = startDate;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
