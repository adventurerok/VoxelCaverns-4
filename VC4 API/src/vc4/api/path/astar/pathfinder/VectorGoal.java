package vc4.api.path.astar.pathfinder;

import vc4.api.path.astar.AStarGoal;
import vc4.api.vector.Vector3l;

public class VectorGoal implements AStarGoal<VectorNode> {
	private final Vector3l goal;
	private final float leeway;

	public VectorGoal(Vector3l goal, float range) {
		this.goal = goal;
		this.leeway = range;
	}

	@Override
	public float g(VectorNode from, VectorNode to) {
		return from.distance(to);
	}

	@Override
	public float getInitialCost(VectorNode node) {
		return node.heuristicDistance(goal);
	}

	@Override
	public float h(VectorNode from) {
		return from.heuristicDistance(goal);
	}

	@Override
	public boolean isFinished(VectorNode node) {
		if (node.getVector().distanceSquared(goal) <= leeway) { return true; }
		return false;
	}
}