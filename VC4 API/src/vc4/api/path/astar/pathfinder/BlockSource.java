package vc4.api.path.astar.pathfinder;

import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

public abstract class BlockSource {
	public abstract int getBlockTypeIdAt(long x, long y, long z);

	public int getMaterialAt(long x, long y, long z) {
		return getBlockTypeIdAt(x, y, z);
	}

	public int getMaterialAt(Vector3l pos) {
		return getMaterialAt(pos.x, pos.y, pos.z);
	}

	public int getMaterialAt(Vector3d pos) {
		return getMaterialAt(MathUtils.floor(pos.x), MathUtils.floor(pos.y), MathUtils.floor(pos.z));
	}

}