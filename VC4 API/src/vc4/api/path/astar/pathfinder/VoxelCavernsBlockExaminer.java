package vc4.api.path.astar.pathfinder;

import vc4.api.block.Block;
import vc4.api.block.BlockFluid;
import vc4.api.vector.Vector3l;

public class VoxelCavernsBlockExaminer implements BlockExaminer {

	@Override
	public float getCost(BlockSource source, PathPoint point) {
		Vector3l pos = point.getVector();
		int above = source.getMaterialAt(pos.add(UP));
		int below = source.getMaterialAt(pos.add(DOWN));
		int in = source.getMaterialAt(pos);
		// if (above == Material.WEB || in == Material.WEB)
		// return 1F;
		// if (below == Material.SOUL_SAND || below == Material.ICE)
		// return 1F;
		if (isLiquid(above, below, in)) return 0.5F;
		return 0.5F; // TODO: add light level-specific costs
	}

	private boolean isLiquid(int... materials) {
		for (int i : materials) {
			if (Block.byId(i) instanceof BlockFluid) return true;
		}
		return false;
	}

	@Override
	public boolean isPassable(BlockSource source, PathPoint point) {
		Vector3l pos = point.getVector();
		int above = source.getMaterialAt(pos.add(UP));
		int below = source.getMaterialAt(pos.add(DOWN));
		int in = source.getMaterialAt(pos);
		if (!canStandOn(below)) { return false; }
		if (!canStandIn(above) || !canStandIn(in)) { return false; }
		return true;
	}

	private static final Vector3l DOWN = new Vector3l(0, -1, 0);

	private static final Vector3l UP = new Vector3l(0, 1, 0);

	public static boolean canStandIn(int mat) {
		return Block.byId(mat).canStandIn();
	}

	// public static boolean canStandOn(Vector3l pos) {
	// Block up = block.getRelative(BlockFace.UP);
	// return canStandOn(block.getType()) && canStandIn(up.getType())
	// && canStandIn(up.getRelative(BlockFace.UP).getType());
	// }

	public static boolean canStandOn(int mat) {
		return Block.byId(mat).canStandOn();
	}
}