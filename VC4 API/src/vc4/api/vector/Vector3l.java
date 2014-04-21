/**
 * 
 */
package vc4.api.vector;

import vc4.api.util.Direction;

/**
 * @author paul
 * 
 */
public class Vector3l implements Vector3<Vector3l> {

	public long x, y, z;

	public Vector3l() {
		// TASK Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	public Vector3l(long x, long y, long z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//1 move
	public Vector3l move(long amount, Direction dir) {
		return new Vector3l(x + amount * dir.getX(), y + amount * dir.getY(), z + amount * dir.getZ());
	}
	
	//2 moves
	public Vector3l move(long amount1, Direction dir1, long amount2, Direction dir2) {
		return new Vector3l(x + amount1 * dir1.getX() + amount2 * dir2.getX(), y + amount1 * dir1.getY() + amount2 * dir2.getY(), z + amount1 * dir1.getZ() + amount2 * dir2.getZ());
	}
	
	//3 moves
	public Vector3l move(long amount1, Direction dir1, long amount2, Direction dir2, long amount3, Direction dir3) {
		return new Vector3l(x + (amount1 * dir1.getX()) + (amount2 * dir2.getX()) + (amount3 * dir3.getX()), y + (amount1 * dir1.getY()) + (amount2 * dir2.getY()) + (amount3 * dir3.getY()), z + (amount1 * dir1.getZ()) + (amount2 * dir2.getZ()) + (amount3 * dir3.getZ()));
	}

	@Override
	public Vector3l add(Vector3l vec) {
		return new Vector3l(x + vec.x, y + vec.y, z + vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector3l multiply(Vector3l vec) {
		return new Vector3l(x * vec.x, y * vec.y, z * vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector3l subtract(Vector3l vec) {
		return new Vector3l(x - vec.x, y - vec.y, z - vec.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector3l divide(Vector3l vec) {
		return new Vector3l(x / vec.x, y / vec.y, z / vec.z);
	}

	@Override
	public Vector3l clone() {
		return new Vector3l(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector3l abs() {
		return new Vector3l(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector3l negate() {
		return new Vector3l(-x, -y, -z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector3l vec) {
		long nx = x - vec.x;
		long ny = y - vec.y;
		long nz = z - vec.z;

		return nx * nx + ny * ny + nz * nz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector3l vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3f()
	 */
	@Override
	public Vector3f toVector3f() {
		return new Vector3f(x, y, z);
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
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		result = prime * result + (int) (z ^ (z >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "V3l[" + x + "," + y + "," + z + "]";
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
		Vector3l other = (Vector3l) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		if (z != other.z) return false;
		return true;
	}

	public boolean horizontalEquals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3l other = (Vector3l) obj;
		if (x != other.x) return false;
		if (z != other.z) return false;
		return true;
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
		return clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector3#toVector3i()
	 */
	@Override
	public Vector3i toVector3i() {
		return new Vector3i((int) x, (int) y, (int) z);
	}

}
