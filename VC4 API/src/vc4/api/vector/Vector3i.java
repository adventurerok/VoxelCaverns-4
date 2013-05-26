/**
 * 
 */
package vc4.api.vector;


/**
 * @author paul
 *
 */
public class Vector3i implements Vector3<Vector3i> {

	public int x, y, z;
	
	
	public Vector3i() {
		// TASK Auto-generated constructor stub
	}
	
	
	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	public Vector3i(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i add(Vector3i vec) {
		return new Vector3i(x + vec.x, y + vec.y, z + vec.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector3i multiply(Vector3i vec) {
		return new Vector3i(x * vec.x, y * vec.y, z * vec.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector3i subtract(Vector3i vec) {
		return new Vector3i(x - vec.x, y - vec.y, z - vec.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector3i divide(Vector3i vec) {
		return new Vector3i(x / vec.x, y / vec.y, z / vec.z);
	}
	
	@Override
	public Vector3i clone(){
		return new Vector3i(x, y, z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector3i abs() {
		return new Vector3i(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector3i negate() {
		return new Vector3i(-x, -y, -z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector3i vec) {
		int nx = x - vec.x;
		int ny = y - vec.y;
		int nz = z - vec.z;
		
		return nx * nx + ny * ny + nz * nz;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector3i vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	
	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3f()
	 */
	@Override
	public Vector3f toVector3f() {
		return new Vector3f(x, y, z);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		result = prime * result + (int) (z ^ (z >>> 32));
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
		Vector3i other = (Vector3i) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		if (z != other.z) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3d()
	 */
	@Override
	public Vector3d toVector3d() {
		return new Vector3d(x, y, z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3l()
	 */
	@Override
	public Vector3l toVector3l() {
		return new Vector3l(x, y, z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3i()
	 */
	@Override
	public Vector3i toVector3i() {
		return clone();
	}

	

}
