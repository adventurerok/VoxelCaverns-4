/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 *
 */
public class Vector4d implements Vector4<Vector4d> {

	public double x;
	public double y;
	public double z;
	public double w;
	
	public Vector4d(double x, double y, double z, double w) {
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
	public Vector4d add(Vector4d vec) {
		return new Vector4d(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector4d multiply(Vector4d vec) {
		return new Vector4d(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector4d subtract(Vector4d vec) {
		return new Vector4d(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector4d divide(Vector4d vec) {
		return new Vector4d(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector4d abs() {
		return new Vector4d(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector4d negate() {
		return new Vector4d(-x, -y, -z, -w);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Vector4d clone() {
		return new Vector4d(x, y, z, w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector4d vec) {
		double nx = x - vec.x;
		double ny = y - vec.y;
		double nz = z - vec.z;
		double nw = w - vec.w;
		
		return nx * nx + ny * ny + nz * nz + nw * nw;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector4d vec) {
		return Math.sqrt(distanceSquared(vec));
	}


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(w);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
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
		Vector4d other = (Vector4d) obj;
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
		return new Vector4f((float)x, (float)y, (float)z, (float)w);
	}
	
	@Override
	public Vector4l toVector4l() {
		return new Vector4l(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z), MathUtils.floor(w));
	}

	@Override
	public Vector4d toVector4d() {
		return clone();
	}

	@Override
	public Vector4i toVector4i() {
		return new Vector4i((int)x, (int)y, (int)z, (int)w);
	}

	

}
