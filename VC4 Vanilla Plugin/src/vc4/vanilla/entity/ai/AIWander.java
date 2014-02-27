package vc4.vanilla.entity.ai;

import vc4.api.entity.EntityLiving;
import vc4.api.entity.ai.AI;
import vc4.api.entity.ai.Movement;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;

public class AIWander extends AI {

	double speed;
	double range;
	int ir;

	public AIWander(EntityLiving owner, double speed, double range) {
		super(owner);
		this.speed = speed;
		this.range = range;
		ir = (int) MathUtils.floor(range);
	}

	@Override
	public int conflictId() {
		return 1;
	}

	@Override
	public boolean shouldStart() {
		return true;
	}

	@Override
	public void start() {

	}

	@Override
	public boolean update() {
		if (owner.getMoveHandler().getExecuting() == null || owner.position.horizontalDistanceSquared(owner.getMoveHandler().getExecuting().getPosition()) < 1) {
			double dist = rand();
			double ang = owner.rand.nextDouble() * 2 * Math.PI;
			Vector3d n = new Vector3d();
			n.y = owner.position.y - ir + owner.rand.nextInt(ir * 2) + owner.rand.nextDouble();
			n.x = MathUtils.cos((float) ang) * dist;
			n.z = MathUtils.sin((float) ang) * dist;
			Movement move = new Movement(n, speed, false);
			owner.getMoveHandler().setExecuting(move);
		}
		return true;
	}

	public double rand() {
		double r = owner.rand.nextDouble();
		r *= range;
		if (r < 2) return range;
		return r;
	}

	@Override
	public void stop() {

	}

}
