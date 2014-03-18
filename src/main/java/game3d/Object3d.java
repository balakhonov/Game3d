package game3d;

public class Object3d {
	private volatile static long idGen = 100000;
	protected volatile long id = ++idGen;
	protected double pX = 0;
	protected double pY = 0;
	protected double pZ = 0;
	protected double rX = 0;
	protected volatile double rY = 0;
	protected double rZ = 0;


	public double getpX() {
		return pX;
	}


	public void setpX(double pX) {
		this.pX = pX;
	}


	public double getpY() {
		return pY;
	}


	public void setpY(double pY) {
		this.pY = pY;
	}


	public double getpZ() {
		return pZ;
	}


	public void setpZ(double pZ) {
		this.pZ = pZ;
	}


	public double getrX() {
		return rX;
	}


	public void setrX(double rX) {
		this.rX = rX;
	}


	public double getrY() {
		return rY;
	}


	public void setrY(double rY) {
		this.rY = rY;
	}


	public double getrZ() {
		return rZ;
	}


	public void setrZ(double rZ) {
		this.rZ = rZ;
	}


	public long getId() {
		return id;
	}
}
