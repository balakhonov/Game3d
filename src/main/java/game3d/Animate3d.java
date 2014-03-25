package game3d;



public class Animate3d extends Object3d implements Rotation3D, Navigation3D {

	@Override
	public void rotateX(double angle) {
		rX += angle;
	}

	@Override
	public void rotateY(double angle) {
		rY += angle;
	}

	@Override
	public void rotateZ(double angle) {
		rZ += angle;
	}

	@Override
	public void moveX(double step) {
		pX -= Math.sin(rY) * step;
	}

	@Override
	public void moveY(double step) {
		pY += Math.sin(rX) * step;
	}

	@Override
	public void moveZ(double step) {
		pZ -= Math.cos(rY) * step;
	}
}
