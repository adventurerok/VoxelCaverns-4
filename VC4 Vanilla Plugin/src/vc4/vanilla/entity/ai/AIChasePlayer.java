package vc4.vanilla.entity.ai;

import vc4.api.entity.EntityLiving;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.ai.AI;
import vc4.api.entity.ai.Movement;

public class AIChasePlayer extends AI {

	EntityPlayer target;
	double max;
	double speed;

	Movement move;

	public AIChasePlayer(EntityLiving owner, double maxRange, double speed) {
		super(owner);
		max = maxRange * maxRange;
		this.speed = speed;
	}

	@Override
	public int priority() {
		return 150;
	}

	@Override
	public int conflictId() {
		return 1;
	}

	@Override
	public boolean shouldStart() {
		double shortest = max;
		EntityPlayer close = null;
		for (EntityPlayer p : owner.world.getPlayers()) {
			double ds = p.position.distanceSquared(owner.position);
			if (ds < shortest) {
				shortest = ds;
				close = p;
			}
		}
		target = close;
		return close != null;
	}

	@Override
	public void start() {
		move = new Movement(target, speed, false);
		owner.getMoveHandler().setExecuting(move);
	}

	@Override
	public void stop() {
		if (owner.getMoveHandler().isExecuting(move)) owner.getMoveHandler().clearExecuting();
	}

	@Override
	public boolean update() {
		if (!owner.getMoveHandler().isExecuting(move)) owner.getMoveHandler().setExecuting(move);
		double ds = target.position.distanceSquared(owner.position);
		if (ds > max) {
			target = null;
			return false;
		}
		owner.lookTargetEntity(target);

		return true;
	}

}
