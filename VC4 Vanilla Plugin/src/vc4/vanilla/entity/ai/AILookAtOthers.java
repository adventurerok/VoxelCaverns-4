package vc4.vanilla.entity.ai;

import java.util.List;

import vc4.api.entity.Entity;
import vc4.api.entity.EntityLiving;
import vc4.api.entity.ai.AI;

public class AILookAtOthers extends AI {

	EntityLiving target;
	double max;
	int start;

	public AILookAtOthers(EntityLiving owner, double maxRange) {
		super(owner);
		max = maxRange * maxRange;
	}

	@Override
	public int conflictId() {
		return 1;
	}

	@Override
	public boolean shouldStart() {
		double shortest = max;
		EntityLiving close = null;
		List<Entity> titys = owner.world.getEntitiesInBoundsExcluding(owner.chunk, owner.bounds.expand(15, 15, 15), owner);
		for (int d = 0; d < titys.size(); ++d) {
			Entity ett = titys.get(d);
			if (!(ett instanceof EntityLiving)) continue;
			EntityLiving tar = (EntityLiving) ett;
			double ds = tar.position.distanceSquared(owner.position);
			if (ds < shortest) {
				shortest = ds;
				close = tar;
			}
		}
		target = close;
		if (close != null) {
			start = 180 + target.rand.nextInt(750);
		}
		return close != null;
	}

	@Override
	public void start() {

	}

	@Override
	public boolean update() {
		--start;
		if (start < 1) return false;
		owner.lookAtEntity(target);
		return true;
	}

	@Override
	public void stop() {
	}

}
