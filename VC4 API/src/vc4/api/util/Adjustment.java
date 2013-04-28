package vc4.api.util;

import vc4.api.vector.Vector3l;

public class Adjustment {

	public long forward, sideways, up;

	public Adjustment(long forward, long sideways, long up) {
		super();
		this.forward = forward;
		this.sideways = sideways;
		this.up = up;
	}
	
	@Override
	public Adjustment clone() {
		return new Adjustment(forward, sideways, up);
	}
	
	public Adjustment move(long forward, long sideways, long up){
		return new Adjustment(this.forward + forward, this.sideways + sideways, this.up + up);
	}
	
	public Vector3l adjust(Vector3l to, Direction dir){
		long x = to.x;
		long y = to.y;
		long z = to.z;
		x += dir.getX() * forward;
		z += dir.getZ() * forward;
		Direction right = dir.clockwise();
		x += right.getX() * sideways;
		z += right.getZ() * sideways;
		y += up;
		return new Vector3l(x, y, z);
	}
}
