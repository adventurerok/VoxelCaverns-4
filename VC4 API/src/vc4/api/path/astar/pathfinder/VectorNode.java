package vc4.api.path.astar.pathfinder;

import java.util.ArrayList;
import java.util.List;

import vc4.api.path.astar.AStarNode;
import vc4.api.path.astar.Plan;
import vc4.api.vector.Vector3l;

public class VectorNode extends AStarNode implements PathPoint {
	private float blockCost = -1;
	final BlockSource blockSource;
	List<PathCallback> callbacks;
	private final BlockExaminer[] examiners;
	final Vector3l location;

	public VectorNode(Vector3l location, BlockSource source, BlockExaminer... examiners) {
		this.location = location;
		this.blockSource = source;
		this.examiners = examiners == null ? new BlockExaminer[] {} : examiners;
	}

	@Override
	public void addCallback(PathCallback callback) {
		if (callbacks == null) callbacks = new ArrayList<>();
		callbacks.add(callback);
	}

	@Override
	public Plan buildPlan() {
		Iterable<VectorNode> parents = getParents();
		return new Path(parents);
	}

	public float distance(VectorNode to) {
		return (float) location.distance(to.location);
	}

	private float getBlockCost() {
		if (blockCost == -1) {
			blockCost = 0;
			for (BlockExaminer examiner : examiners) {
				blockCost += examiner.getCost(blockSource, this);
			}
		}
		return blockCost;
	}

	@Override
	public Iterable<AStarNode> getNeighbours() {
		List<AStarNode> nodes = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && y == 0 && z == 0) continue;
					Vector3l mod = location.add(new Vector3l(x, y, z));
					if (mod.equals(location)) continue;
					VectorNode sub = getNewNode(mod);
					if (!isPassable(sub)) continue;
					nodes.add(sub);
				}
			}
		}
		return nodes;
	}

	private VectorNode getNewNode(Vector3l mod) {
		return new VectorNode(mod, blockSource, examiners);
	}

	@Override
	public Vector3l getVector() {
		return location;
	}

	public float heuristicDistance(Vector3l goal) {
		return (float) (location.distance(goal) + getBlockCost()) * TIEBREAKER;
	}

	private boolean isPassable(PathPoint mod) {
		for (BlockExaminer examiner : examiners) {
			if (!examiner.isPassable(blockSource, mod)) return false;
		}
		return true;
	}

	private static final float TIEBREAKER = 1.00001f;
}