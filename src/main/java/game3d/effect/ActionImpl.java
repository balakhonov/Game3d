package game3d.effect;

import java.util.Date;


public abstract class ActionImpl implements Action {
	private long duration;
	private long startDate;
	private long endDate;
	private boolean stopped;

	/**
	 * 
	 */
	public ActionImpl() {
		this(0);
	}

	/**
	 * 
	 * @param duration
	 */
	public ActionImpl(long duration) {
		if (duration < 0) {
			throw new IllegalStateException("duration should be > 0");
		}

		this.duration = duration;
		this.startDate = new Date().getTime();
		this.endDate = startDate + duration;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public ActionImpl(long startDate, long endDate) {
		if (startDate < 0) {
			throw new IllegalStateException("startDate should be > 0");
		}
		if (endDate < 0) {
			throw new IllegalStateException("endDate should be > 0");
		}

		this.endDate = endDate;
		this.startDate = startDate;
	}

	@Override
	public void run() {
		if (stopped) {
			return;
		}

		if (duration != 0 && new Date().getTime() >= endDate) {
			stopped = true;
			onExpired();
		} else {
			onAction();
		}
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
