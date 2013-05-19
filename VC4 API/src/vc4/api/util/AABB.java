package vc4.api.util;

import java.util.List;

import vc4.api.vector.Vector3d;


public class AABB {


	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;

	public static AABB getBoundingBox(double mix, double max,
			double miy, double may, double miz, double maz) {
		return new AABB(mix, miy, miz, max, may, maz);
	}
	

	public static List<AABB> addAABBsToList(List<AABB> list, AABB...aabbs){
		if(aabbs == null || list == null) return list;
		for(int dofor = 0; dofor < aabbs.length; ++dofor){
			if(aabbs[dofor] == null) continue;
			list.add(aabbs[dofor]);
		}
		return list;
	}

	

	private AABB(double minX, double minY, double minZ, double maxX,
			double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(maxX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxZ);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minZ);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	public double averageX(){
		return (minX + maxX) / 2;
	}
	public double averageY(){
		return (minY + maxY) / 2;
	}
	public double averageZ(){
		return (minZ + maxZ) / 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AABB other = (AABB) obj;
		if (Double.doubleToLongBits(maxX) != Double
				.doubleToLongBits(other.maxX))
			return false;
		if (Double.doubleToLongBits(maxY) != Double
				.doubleToLongBits(other.maxY))
			return false;
		if (Double.doubleToLongBits(maxZ) != Double
				.doubleToLongBits(other.maxZ))
			return false;
		if (Double.doubleToLongBits(minX) != Double
				.doubleToLongBits(other.minX))
			return false;
		if (Double.doubleToLongBits(minY) != Double
				.doubleToLongBits(other.minY))
			return false;
		if (Double.doubleToLongBits(minZ) != Double
				.doubleToLongBits(other.minZ))
			return false;
		return true;
	}

	public AABB setBounds(double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		return this;
	}

	public AABB include(double x, double y, double z) {
		double mix = minX;
		double miy = minY;
		double miz = minZ;
		double max = maxX;
		double may = maxY;
		double maz = maxZ;

		if (x < 0.0D) mix += x;
		else if (x > 0.0D) max += x;
		if (y < 0.0D) miy += y;
		else if (y > 0.0D) may += y;
		if (z < 0.0D) miz += z;
		else if (z > 0.0D) maz += z;

		return getBoundingBox(mix, max, miy, may, miz, maz);
	}


	public AABB add(double x, double y, double z){
		minX += x;
		maxX += x;
		minY += y;
		maxY += y;
		minZ += z;
		maxZ += z;
		return this;
	}
	
	
	
	public AABB expand(double x, double y, double z) {
		double nlx = minX - x;
		double nly = minY - y;
		double nlz = minZ - z;
		double nhx = maxX + x;
		double nhy = maxY + y;
		double nhz = maxZ + z;
		return getBoundingBox(nlx, nhx, nly, nhy, nlz, nhz);
	}

	public AABB getOffsetBoundingBox(double x, double y, double z) {
		return getBoundingBox(minX + x, maxX + x, minY + y, maxY + y, minZ + z, maxZ + z);
	}
	public double calculateXOffset(AABB aabb, double x) {
		if (aabb.maxY <= minY || aabb.minY >= maxY) {
			return x;
		}

		if (aabb.maxZ <= minZ || aabb.minZ >= maxZ) {
			return x;
		}
		
		if (x > 0.0D && aabb.maxX <= minX) {
			double d = minX - aabb.maxX;

			if (d < x) {
				x = d;
			}
		}

		if (x < 0.0D && aabb.minX >= maxX) {
			double d1 = maxX - aabb.minX;

			if (d1 > x) {
				x = d1;
			}
		}
		

		return x;
	}

	public double calculateYOffset(AABB aabb, double y) {
		if (aabb.maxX <= minX || aabb.minX >= maxX) {
			return y;
		}

		if (aabb.maxZ <= minZ || aabb.minZ >= maxZ) {
			return y;
		}
		
		if (y > 0.0D && aabb.maxY <= minY) {
			double d = minY - aabb.maxY;

			if (d < y) {
				y = d;
			}
		}

		if (y < 0.0D && aabb.minY >= maxY) {
			double d1 = maxY - aabb.minY;

			if (d1 > y) {
				y = d1;
			}
		}
		

		return y;
	}

	public double calculateZOffset(AABB aabb, double z) {
		if (aabb.maxX <= minX || aabb.minX >= maxX) {
			return z;
		}

		if (aabb.maxY <= minY || aabb.minY >= maxY) {
			return z;
		}
		
		if (z > 0.0D && aabb.maxZ <= minZ) {
			double zDist = minZ - aabb.maxZ;

			if (zDist < z) {
				z = zDist;
			}
		}

		if (z < 0.0D && aabb.minZ >= maxZ) {
			double zDist2 = maxZ - aabb.minZ;

			if (zDist2 > z) {
				z = zDist2;
			}
		}
		

		return z;
	}

	public boolean intersectsWith(AABB aabb) {
		if(aabb == null) return false;
		if (aabb.maxX <= minX || aabb.minX >= maxX) {
			return false;
		}

		if (aabb.maxY <= minY || aabb.minY >= maxY) {
			return false;
		}

		return aabb.maxZ > minZ && aabb.minZ < maxZ;
	}

	

	public boolean isVecInside(Vector3d vec) {
		if (vec.x <= minX || vec.x >= maxX) {
			return false;
		}
		if (vec.y <= minY || vec.y >= maxY) {
			return false;
		}
		return vec.z > minZ && vec.z < maxZ;
	}

	public double getAverageEdgeLength() {
		double d = maxX - minX;
		double d1 = maxY - minY;
		double d2 = maxZ - minZ;
		return (d + d1 + d2) / 3f;
	}

	public AABB contract(double x, double y, double z) {
		double nlx = minX + x;
		double nly = minY + y;
		double nlz = minZ + z;
		double nhx = maxX - x;
		double nhy = maxY - y;
		double nhz = maxZ - z;
		return getBoundingBox(nlx, nhx, nly, nhy, nlz, nhz);
	}

	@Override
	public AABB clone() {
		return getBoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
	}

	// public MovingObjectPosition calculateIntercept(Vec3D par1Vec3D, Vec3D
	// par2Vec3D)
	// {
	// Vec3D vec3d = par1Vec3D.getIntermediateWithXValue(par2Vec3D, minX);
	// Vec3D vec3d1 = par1Vec3D.getIntermediateWithXValue(par2Vec3D, maxX);
	// Vec3D vec3d2 = par1Vec3D.getIntermediateWithYValue(par2Vec3D, minY);
	// Vec3D vec3d3 = par1Vec3D.getIntermediateWithYValue(par2Vec3D, maxY);
	// Vec3D vec3d4 = par1Vec3D.getIntermediateWithZValue(par2Vec3D, minZ);
	// Vec3D vec3d5 = par1Vec3D.getIntermediateWithZValue(par2Vec3D, maxZ);
	//
	// if (!isVecInYZ(vec3d))
	// {
	// vec3d = null;
	// }
	//
	// if (!isVecInYZ(vec3d1))
	// {
	// vec3d1 = null;
	// }
	//
	// if (!isVecInXZ(vec3d2))
	// {
	// vec3d2 = null;
	// }
	//
	// if (!isVecInXZ(vec3d3))
	// {
	// vec3d3 = null;
	// }
	//
	// if (!isVecInXY(vec3d4))
	// {
	// vec3d4 = null;
	// }
	//
	// if (!isVecInXY(vec3d5))
	// {
	// vec3d5 = null;
	// }
	//
	// Vec3D vec3d6 = null;
	//
	// if (vec3d != null && (vec3d6 == null || par1Vec3D.squareDistanceTo(vec3d)
	// < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d;
	// }
	//
	// if (vec3d1 != null && (vec3d6 == null ||
	// par1Vec3D.squareDistanceTo(vec3d1) < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d1;
	// }
	//
	// if (vec3d2 != null && (vec3d6 == null ||
	// par1Vec3D.squareDistanceTo(vec3d2) < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d2;
	// }
	//
	// if (vec3d3 != null && (vec3d6 == null ||
	// par1Vec3D.squareDistanceTo(vec3d3) < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d3;
	// }
	//
	// if (vec3d4 != null && (vec3d6 == null ||
	// par1Vec3D.squareDistanceTo(vec3d4) < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d4;
	// }
	//
	// if (vec3d5 != null && (vec3d6 == null ||
	// par1Vec3D.squareDistanceTo(vec3d5) < par1Vec3D.squareDistanceTo(vec3d6)))
	// {
	// vec3d6 = vec3d5;
	// }
	//
	// if (vec3d6 == null)
	// {
	// return null;
	// }
	//
	// byte byte0 = -1;
	//
	// if (vec3d6 == vec3d)
	// {
	// byte0 = 4;
	// }
	//
	// if (vec3d6 == vec3d1)
	// {
	// byte0 = 5;
	// }
	//
	// if (vec3d6 == vec3d2)
	// {
	// byte0 = 0;
	// }
	//
	// if (vec3d6 == vec3d3)
	// {
	// byte0 = 1;
	// }
	//
	// if (vec3d6 == vec3d4)
	// {
	// byte0 = 2;
	// }
	//
	// if (vec3d6 == vec3d5)
	// {
	// byte0 = 3;
	// }
	//
	// return new MovingObjectPosition(0, 0, 0, byte0, vec3d6);
	// }

	public boolean isVecInYZ(Vector3d vec) {
		if (vec == null) {
			return false;
		} else {
			return vec.y >= minY && vec.y <= maxY && vec.z >= minZ
					&& vec.z <= maxZ;
		}
	}

	public boolean isVecInXZ(Vector3d vec) {
		if (vec == null) {
			return false;
		} else {
			return vec.x >= minX && vec.x <= maxX && vec.z >= minZ
					&& vec.z <= maxZ;
		}
	}

	public boolean isVecInXY(Vector3d vec) {
		if (vec == null) {
			return false;
		} else {
			return vec.x >= minX && vec.x <= maxX && vec.y >= minY
					&& vec.y <= maxY;
		}
	}

	public void setBB(AABB aabb) {
		minX = aabb.minX;
		minY = aabb.minY;
		minZ = aabb.minZ;
		maxX = aabb.maxX;
		maxY = aabb.maxY;
		maxZ = aabb.maxZ;
	}

	@Override
	public String toString() {
		return (new StringBuilder()).append("AABB[").append(minX).append(", ")
				.append(minY).append(", ").append(minZ).append(" -> ")
				.append(maxX).append(", ").append(maxY).append(", ")
				.append(maxZ).append("]").toString();
	}

	public AABB correctMinMax() {
		double mix = Math.min(minX, maxX);
		double miy = Math.min(minY, maxY);
		double miz = Math.min(minZ, maxZ);
		double max = Math.max(minX, maxX);
		double may = Math.max(minY, maxY);
		double maz = Math.max(minZ, maxZ);
		return setBounds(mix, miy, miz, max, may, maz);
	}

	public RayTraceResult calculateIntercept(Vector3d start, Vector3d end) {
		Vector3d minX = start.getXIntermediate(end, this.minX);
		Vector3d maxX = start.getXIntermediate(end, this.maxX);
		Vector3d minY = start.getYIntermediate(end, this.minY);
		Vector3d maxY = start.getYIntermediate(end, this.maxY);
		Vector3d minZ = start.getZIntermediate(end, this.minZ);
		Vector3d maxZ = start.getZIntermediate(end, this.maxZ);
		if(!isVecInYZ(minX)) minX = null;
		if(!isVecInYZ(maxX)) maxX = null;
		if(!isVecInXZ(minY)) minY = null;
		if(!isVecInXZ(maxY)) maxY = null;
		if(!isVecInXY(minZ)) minZ = null;
		if(!isVecInXY(maxZ)) maxZ = null;
		Vector3d using = null;
		if(minX != null && (using == null || start.floatDistanceSquared(minX) < start.floatDistanceSquared(using))) using = minX;
		if(maxX != null && (using == null || start.floatDistanceSquared(maxX) < start.floatDistanceSquared(using))) using = maxX;
		if(minY != null && (using == null || start.floatDistanceSquared(minY) < start.floatDistanceSquared(using))) using = minY;
		if(maxY != null && (using == null || start.floatDistanceSquared(maxY) < start.floatDistanceSquared(using))) using = maxY;
		if(minZ != null && (using == null || start.floatDistanceSquared(minZ) < start.floatDistanceSquared(using))) using = minZ;
		if(maxZ != null && (using == null || start.floatDistanceSquared(maxZ) < start.floatDistanceSquared(using))) using = maxZ;
		if(using == null) return null;
		byte b = -1;
		if(using == minX)  b = 2;
		if(using == maxX)b = 0;
		if(using == minY)b = 5;
		if(using == maxY)b = 4;
		if(using == minZ)b = 3;
		if(using == maxZ)b = 1;
		minX = null;
		maxX = null;
		minY = null;
		maxY = null;
		minZ = null;
		maxZ = null;
		return new RayTraceResult(0, 0, 0, b, using);
	}

	public AABB scale(float scale) {
		minX *= scale;
		minY *= scale;
		minZ *= scale;
		maxX *= scale;
		maxY *= scale;
		maxZ *= scale;
		return this;
	}
	
	public Vector3d getVertexP(Vector3d normal) {

		Vector3d res = new Vector3d(minX, minY, minZ);

		if (normal.x > 0)
			res.x += maxX - minX;

		if (normal.y > 0)
			res.y += maxY - minY;

		if (normal.z > 0)
			res.z += maxZ - minZ;

		return(res);
	}

	public Vector3d getVertexN(Vector3d normal) {

		Vector3d res = new Vector3d(minX, minY, minZ);

		if (normal.x < 0)
			res.x += maxX - minX;

		if (normal.y < 0)
			res.y += maxY - minY;

		if (normal.z < 0)
			res.z += maxZ - minZ;

		return(res);
	}
}
