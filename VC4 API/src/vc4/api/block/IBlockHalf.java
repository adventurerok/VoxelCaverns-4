package vc4.api.block;

import vc4.api.util.AABB;

public interface IBlockHalf {

	public static final AABB LOWER_HALF = AABB.getBoundingBox(0, 1, 0, 0.5, 0, 1);
	public static final AABB UPPER_HALF = AABB.getBoundingBox(0, 1, 0.5, 1, 0, 1);
}
