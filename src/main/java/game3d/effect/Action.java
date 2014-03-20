package game3d.effect;

public interface Action extends Runnable {

	public void onAction();

	public void onExpired();

	public long getStartDate();

	public void setStartDate(long startDate);

	public long getEndDate();

	public void setEndDate(long endDate);

	public boolean isStopped();

	public void setStopped(boolean stopped);
}
