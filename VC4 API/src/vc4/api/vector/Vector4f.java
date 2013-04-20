/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 *
 */
public class Vector4f implements Vector4<Vector4f> {

	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4f(float x, float y, float z, float w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	@Override
	public Vector4f add(Vector4f vec) {
		return new Vector4f(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector4f multiply(Vector4f vec) {
		return new Vector4f(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector4f subtract(Vector4f vec) {
		return new Vector4f(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector4f divide(Vector4f vec) {
		return new Vector4f(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector4f abs() {
		return new Vector4f(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector4f negate() {
		return new Vector4f(-x, -y, -z, -w);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Vector4f clone() {
		return new Vector4f(x, y, z, w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector4f vec) {
		float nx = x - vec.x;
		float ny = y - vec.y;
		float nz = z - vec.z;
		float nw = w - vec.w;
		
		return nx * nx + ny * ny + nz * nz + nw * nw;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector4f vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vector4f other = (Vector4f) obj;
		if (!MathUtils.equals(x, other.x)) return false;
		if (!MathUtils.equals(y, other.y)) return false;
		if (!MathUtils.equals(z, other.z)) return false;
		if (!MathUtils.equals(w, other.w)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector4#toVector4f()
	 */
	@Override
	public Vector4f toVector4f() {
		return clone();
	}

	

}
