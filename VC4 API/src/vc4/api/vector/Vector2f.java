/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 * 
 */
public class Vector2f implements Vector2<Vector2f> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	public float dot(Vector2f other) {
		return (x * other.x + y * other.y);
	}

	public float angle(Vector2f other) {
		float dls = dot(other) / length() * other.length();
		if (dls < -1.0F) dls = -1.0F;
		else if (dls > 1.0F) dls = 1.0F;
		return (float) Math.acos(dls);
	}

	public float lengthSquared() {
		return x * x + y * y;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		return true;
	}

	public float x, y;

	public Vector2f(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	@Override
	public Vector2f add(Vector2f vec) {
		return new Vector2f(x + vec.x, y + vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector2f multiply(Vector2f vec) {
		return new Vector2f(x * vec.x, y * vec.y);
	}

	@Override
	public Vector2f clone() {
		return new Vector2f(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector2f subtract(Vector2f vec) {
		return new Vector2f(x - vec.x, y - vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector2f divide(Vector2f vec) {
		return new Vector2f(x / vec.x, y / vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector2f negate() {
		return new Vector2f(-x, -y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector2f vec) {
		float nx = vec.x - x;
		float ny = vec.y - y;
		return nx * nx + ny * ny;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector2f vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2f()
	 */
	@Override
	public Vector2f toVector2f() {
		return clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2i()
	 */
	@Override
	public Vector2i toVector2i() {
		return new Vector2i(MathUtils.floor(x), MathUtils.floor(y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2l()
	 */
	@Override
	public Vector2l toVector2l() {
		return new Vector2l(MathUtils.floor(x), MathUtils.floor(y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2d()
	 */
	@Override
	public Vector2d toVector2d() {
		return new Vector2d(x, y);
	}

}
