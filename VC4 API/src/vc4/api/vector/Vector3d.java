/**
 * 
 */
package vc4.api.vector;

import vc4.api.math.MathUtils;

/**
 * @author paul
 *
 */
public class Vector3d implements Vector3<Vector3d> {

	public double x, y, z;
	
	public Vector3d() {
		// TASK Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	public Vector3d(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3d add(Vector3d vec) {
		return new Vector3d(x + vec.x, y + vec.y, z + vec.z);
	}
	
	public Vector3d add(double ax, double ay, double az) {
		return new Vector3d(x + ax, y + ay, z + az);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector3d multiply(Vector3d vec) {
		return new Vector3d(x * vec.x, y * vec.y, z * vec.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector3d subtract(Vector3d vec) {
		return new Vector3d(x - vec.x, y - vec.y, z - vec.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector3d divide(Vector3d vec) {
		return new Vector3d(x / vec.x, y / vec.y, z / vec.z);
	}
	
	@Override
	public Vector3d clone(){
		return new Vector3d(x, y, z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector3d abs() {
		return new Vector3d(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector3d negate() {
		return new Vector3d(-x, -y, -z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector3d vec) {
		double nx = x - vec.x;
		double ny = y - vec.y;
		double nz = z - vec.z;
		
		return nx * nx + ny * ny + nz * nz;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector3d vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		long result = 1;
		result = prime * result + Double.doubleToLongBits(x);
		result = prime * result + Double.doubleToLongBits(y);
		result = prime * result + Double.doubleToLongBits(z);
		return (int) result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3d other = (Vector3d) obj;
		if (!MathUtils.equals(x, other.x)) return false;
		if (!MathUtils.equals(y, other.y)) return false;
		if (!MathUtils.equals(z, other.z)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3f()
	 */
	@Override
	public Vector3f toVector3f() {
		return new Vector3f((float)x, (float)y, (float)z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3d()
	 */
	@Override
	public Vector3d toVector3d() {
		return clone();
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3l()
	 */
	@Override
	public Vector3l toVector3l() {
		return new Vector3l(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
	}

	/* (non-Javadoc)
	 * @see vc4.api.vector.Vector3#toVector3i()
	 */
	@Override
	public Vector3i toVector3i() {
		return new Vector3i((int)MathUtils.floor(x), (int)MathUtils.floor(y), (int)MathUtils.floor(z));
	}

	
	public Vector3d getXIntermediate(Vector3d end, double side) {
		double xDist = end.x - x;
		double yDist = end.y - y;
		double zDist = end.z - z;

		if (xDist * xDist < 1.0000000116860974E-007D)
			return null;
		double inter = (side - x) / xDist;
		if (inter < 0.0D || inter > 1.0D)
			return null;
		else
			return new Vector3d(x + xDist * inter, y + yDist * inter, z + zDist * inter);
	}

	public Vector3d getYIntermediate(Vector3d end, double side) {
		double xDist = end.x - x;
		double yDist = end.y - y;
		double zDist = end.z - z;

		if (yDist * yDist < 1.0000000116860974E-007D)
			return null;
		double inter = (side - y) / yDist;
		if (inter < 0.0D || inter > 1.0D)
			return null;
		else
			return new Vector3d(x + xDist * inter, y + yDist * inter, z + zDist * inter);
	}

	public Vector3d getZIntermediate(Vector3d end, double side) {
		double xDist = end.x - x;
		double yDist = end.y - y;
		double zDist = end.z - z;

		if (zDist * zDist < 1.0000000116860974E-007D)
			return null;
		double inter = (side - z) / zDist;
		if (inter < 0.0D || inter > 1.0D)
			return null;
		else
			return new Vector3d(x + xDist * inter, y + yDist * inter, z + zDist * inter);
	}
	
	public double floatDistanceTo(Vector3d other) {
		double nx = other.x - x;
		double ny = other.y - y;
		double nz = other.z - z;
		return (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
	}
	
	public double floatDistanceSquared(Vector3d other) {
		double nx = other.x - x;
		double ny = other.y - y;
		double nz = other.z - z;
		return (float) nx * nx + ny * ny + nz * nz;
	}
	
	public Vector3d cross(Vector3d in) {
		return new Vector3d(y * in.z - z * in.y, z * in.x - x * in.z, x * in.y - y * in.x);

	}

	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Vector3d normalize() {
		double len = length();
		if (len != 0 && len != 1) return new Vector3d(x / len, y / len, z / len);
		else return clone();
	}

	public double innerProduct(Vector3d v) {
		return (x * v.x + y * v.y + z * v.z);
	}

	public Vector3d multiply(double val) {
		return new Vector3d(x * val, y * val, z * val);
	}

	public double horizontalDistance(Vector3d vec) {
		return Math.sqrt(horizontalDistanceSquared(vec));
	}

	public double horizontalDistanceSquared(Vector3d vec) {
		double nx = x - vec.x;
		double nz = z - vec.z;
		
		return nx * nx + nz * nz; 
	}
	

}
