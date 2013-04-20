package vc4.api.util;

import vc4.api.vector.Vector3d;

public class RayTraceResult {

	public long x;
	public long y;
	public long z;
	public int side;
	public Vector3d vector;
	public boolean isEntity = false;
	//public Entity entity;
	
	public RayTraceResult(long x, long y, long z, int side, Vector3d vector) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.side = side;
		this.vector = vector;
	}

//	public RayTraceResult(Entity pointing, Vector3d vector) {
//		entity = pointing;
//		isEntity = true;
//		this.vector = vector;
//		x = MathHelper.floorDoubleToLong(vector.x);
//		y = MathHelper.floorDoubleToLong(vector.y);
//		z = MathHelper.floorDoubleToLong(vector.z);
//	}
}
