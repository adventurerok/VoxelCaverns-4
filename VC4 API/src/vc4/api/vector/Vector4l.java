/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 * 
 */
public class Vector4l implements Vector4<Vector4l> {

	public long x;
	public long y;
	public long z;
	public long w;

	public Vector4l(long x, long y, long z, long w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	@Override
	public Vector4l add(Vector4l vec) {
		return new Vector4l(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector4l multiply(Vector4l vec) {
		return new Vector4l(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector4l subtract(Vector4l vec) {
		return new Vector4l(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector4l divide(Vector4l vec) {
		return new Vector4l(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector4l abs() {
		return new Vector4l(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector4l negate() {
		return new Vector4l(-x, -y, -z, -w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Vector4l clone() {
		return new Vector4l(x, y, z, w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector4l vec) {
		long nx = x - vec.x;
		long ny = y - vec.y;
		long nz = z - vec.z;
		long nw = w - vec.w;

		return nx * nx + ny * ny + nz * nz + nw * nw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector4l vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector4#toVector4f()
	 */
	@Override
	public Vector4f toVector4f() {
		return new Vector4f(x, y, z, w);
	}

	@Override
	public Vector4l toVector4l() {
		return clone();
	}

	@Override
	public Vector4i toVector4i() {
		return new Vector4i((int) x, (int) y, (int) z, (int) w);
	}

	@Override
	public Vector4d toVector4d() {
		return new Vector4d(x, y, z, w);
	}

}
