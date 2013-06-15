package vc4.api.path;

import vc4.api.entity.EntityLiving;
import vc4.api.path.astar.AStarMachine;
import vc4.api.path.astar.pathfinder.*;
import vc4.api.vector.Vector3d;

public class AStarNavigationStrategy extends AbstractPathStrategy {

	private static final AStarMachine<VectorNode, Path> ASTAR = AStarMachine.createWithDefaultStorage();

	private final Vector3d location;
	private final EntityLiving entity;
	private Path plan;
	private Vector3d vector;

	private static final float RANGE = 20;
	private static final float DISTANCE_MARGIN = 3;

	private static final VoxelCavernsBlockExaminer examiner = new VoxelCavernsBlockExaminer();

	public AStarNavigationStrategy(EntityLiving entity, Vector3d dest) {
		super(TargetType.LOCATION);
		this.entity = entity;
		this.location = dest;
		Vector3d eye = entity.position.clone();
		plan = ASTAR.runFully(new VectorGoal(dest.toVector3l(), DISTANCE_MARGIN), new VectorNode(eye.toVector3l(), new WorldBlockSource(entity.world), examiner), (int) (RANGE * 25));
		if (plan == null || plan.isComplete()) {
			setCancelReason(CancelReason.STUCK);
		} else {
			vector = plan.getCurrentVector().toVector3d();
		}
	}
	
	public Vector3d getLocation() {
		return location;
	}

	@Override
	public Vector3d getTargetAsVector() {
		return vector;
	}

	@Override
	public void stop() {
		plan = null;
	}

	@Override
	public boolean update() {
		if (getCancelReason() != null || plan == null || plan.isComplete()) return true;

		if (entity.position.distanceSquared(vector) <= DISTANCE_MARGIN) {
			plan.update(entity);
			if(plan.isComplete()) return true;
			vector = plan.getCurrentVector().toVector3d();
		}
		double dX = vector.x - entity.position.x;
		double dZ = vector.y - entity.position.y;
		double dY = vector.z - entity.position.z;
		double xzDistance = dX * dX + dZ * dZ;
		double distance = xzDistance + dY * dY;
		if (distance > 0 && dY > 0.4 && xzDistance <= 4.205) {
			// 2.75 -> 4.205 (allow for diagonal jumping)
			entity.jump();
		}
		entity.getMoveHandler().targetAstarVector(vector.x + 0.5, vector.y + 0.5, vector.z + 0.5, 1);
		return false;
	}

}
