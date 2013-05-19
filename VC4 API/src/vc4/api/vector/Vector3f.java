/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 * 
 */
public class Vector3f implements Vector3<Vector3f> {

	public float x, y, z;

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	public Vector3f(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3f add(Vector3f vec) {
		return new Vector3f(x + vec.x, y + vec.y, z + vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector3f multiply(Vector3f vec) {
		return new Vector3f(x * vec.x, y * vec.y, z * vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector3f subtract(Vector3f vec) {
		return new Vector3f(x - vec.x, y - vec.y, z - vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector3f divide(Vector3f vec) {
		return new Vector3f(x / vec.x, y / vec.y, z / vec.z);
	}

	@Override
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector3f vec) {
		float nx = x - vec.x;
		float ny = y - vec.y;
		float nz = z - vec.z;

		return nx * nx + ny * ny + nz * nz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector3f vec) {
		return Math.sqrt(distanceSquared(vec));
	}

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
		result = prime * result + Float.floatToIntBits(z);
		return result;
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
		Vector3f other = (Vector3f) obj;
		if (!MathUtils.equals(x, other.x)) return false;
		if (!MathUtils.equals(y, other.y)) return false;
		if (!MathUtils.equals(z, other.z)) return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3f()
	 */
	@Override
	public Vector3f toVector3f() {
		return clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3d()
	 */
	@Override
	public Vector3d toVector3d() {
		return new Vector3d(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3l()
	 */
	@Override
	public Vector3l toVector3l() {
		return new Vector3l(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3i()
	 */
	@Override
	public Vector3i toVector3i() {
		return new Vector3i(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
	}

	public Vector3f cross(Vector3f in) {
		return new Vector3f(y * in.z - z * in.y, z * in.x - x * in.z, x * in.y - y * in.x);

	}

	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Vector3f normalize() {
		float len = (float) length();
		if (len > 1) return new Vector3f(x / len, y / len, z / len);
		else return clone();
	}

	public double innerProduct(Vector3f v) {
		return (x * v.x + y * v.y + z * v.z);
	}

	public Vector3f multiply(double val) {
		return new Vector3f((float) (x * val), (float) (y * val), (float) (z * val));
	}

	public Vector3f multiply(float val) {
		return new Vector3f(x * val, y * val, z * val);
	}

}
