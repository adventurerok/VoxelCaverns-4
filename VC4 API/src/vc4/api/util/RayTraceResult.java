package vc4.api.util;

import vc4.api.entity.Entity;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

public class RayTraceResult {

	public long x;
	public long y;
	public long z;
	public int side;
	public Vector3d vector;
	public boolean isEntity = false;
	public Entity entity;
	
	public RayTraceResult(long x, long y, long z, int side, Vector3d vector) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.side = side;
		this.vector = vector;
	}
	
	public Vector3l getBlockPos(){
		return new Vector3l(x, y, z);
	}

	public RayTraceResult(Entity pointing, Vector3d vector) {
		entity = pointing;
		isEntity = true;
		this.vector = vector;
		x = MathUtils.floor(vector.x);
		y = MathUtils.floor(vector.y);
		z = MathUtils.floor(vector.z);
	}
}
