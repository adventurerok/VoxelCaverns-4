/**
 * 
 */
package vc4.api.vector;


/**
 * @author paul
 *
 */
public class Vector4i implements Vector4<Vector4i> {

	public int x;
	public int y;
	public int z;
	public int w;
	
	public Vector4i(int x, int y, int z, int w) {
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
	public Vector4i add(Vector4i vec) {
		return new Vector4i(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector4i multiply(Vector4i vec) {
		return new Vector4i(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector4i subtract(Vector4i vec) {
		return new Vector4i(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector4i divide(Vector4i vec) {
		return new Vector4i(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector4i abs() {
		return new Vector4i(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector4i negate() {
		return new Vector4i(-x, -y, -z, -w);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Vector4i clone() {
		return new Vector4i(x, y, z, w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector4i vec) {
		int nx = x - vec.x;
		int ny = y - vec.y;
		int nz = z - vec.z;
		int nw = w - vec.w;
		
		return nx * nx + ny * ny + nz * nz + nw * nw;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector4i vec) {
		return Math.sqrt(distanceSquared(vec));
	}


	

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector4#toVector4f()
	 */
	@Override
	public Vector4f toVector4f() {
		return new Vector4f(x, y, z, w);
	}
	
	@Override
	public Vector4l toVector4l() {
		return new Vector4l(x, y, z, w);
	}
	
	@Override
	public Vector4i toVector4i() {
		return clone();
	}

	

}
