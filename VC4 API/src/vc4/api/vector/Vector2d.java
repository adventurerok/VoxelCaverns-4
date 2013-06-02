/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 *
 */
public class Vector2d implements Vector2<Vector2d>{

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2d other = (Vector2d) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) return false;
		return true;
	}

	public double x, y;
	
	public Vector2d(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double dot(Vector2d other) {
		return (x * other.x + y * other.y);
	}

	public double angle(Vector2d other) {
		return Math.atan2(other.y - y, other.x - x);
	}

	public double lengthSquared() {
		return x * x + y * y;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	@Override
	public Vector2d add(Vector2d vec) {
		return new Vector2d(x + vec.x, y + vec.y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector2d multiply(Vector2d vec) {
		return new Vector2d(x * vec.x, y * vec.y);
	}
	
	@Override
	public Vector2d clone(){
		return new Vector2d(x, y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector2d subtract(Vector2d vec) {
		return new Vector2d(x - vec.x, y - vec.y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector2d divide(Vector2d vec) {
		return new Vector2d(x / vec.x, y / vec.y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector2d abs() {
		return new Vector2d(Math.abs(x), Math.abs(y));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector2d negate() {
		return new Vector2d(-x, -y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector2d vec) {
		double nx = vec.x - x;
		double ny = vec.y - y;
		return nx * nx + ny * ny;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector2d vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector2#toVector2f()
	 */
	@Override
	public Vector2f toVector2f() {
		return new Vector2f((float)x, (float)y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector2#toVector2i()
	 */
	@Override
	public Vector2i toVector2i() {
		return new Vector2i((int)MathUtils.floor(x), (int)MathUtils.floor(y));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector2#toVector2l()
	 */
	@Override
	public Vector2l toVector2l() {
		return new Vector2l(MathUtils.floor(x), MathUtils.floor(y));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector2#toVector2d()
	 */
	@Override
	public Vector2d toVector2d() {
		return clone();
	}

	

}
