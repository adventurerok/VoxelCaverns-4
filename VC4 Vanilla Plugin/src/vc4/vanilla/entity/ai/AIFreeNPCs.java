package vc4.vanilla.entity.ai;

import vc4.api.entity.*;
import vc4.api.entity.ai.AI;
import vc4.api.math.MathUtils;
import vc4.api.util.AABB;
import vc4.api.util.EntityList;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.entity.EntityNpc.NpcState;

public class AIFreeNPCs extends AI {

	EntityNpc target;
	double max;
	double speed;
	
	public AIFreeNPCs(EntityLiving owner, double maxRange, double speed) {
		super(owner);
		max = maxRange * maxRange;
		this.speed = speed;
	}
	
	@Override
	public int conflictId() {
		return 1;
	}
	
	@Override
	public int priority() {
		return 250;
	}

	@Override
	public boolean shouldStart() {
		double shortest = max;
		EntityNpc close = null;
		EntityList titys = owner.world.getEntitiesInBoundsExcluding(owner.bounds.expand(15, 15, 15), owner);
		for(int d = 0; d < titys.size(); ++d){
			Entity ett = titys.get(d);
			if(!(ett instanceof EntityNpc)) continue;
			EntityNpc tar = (EntityNpc) ett;
			if(tar.getState() != NpcState.TRAPPED) continue;
			double ds = tar.position.distanceSquared(owner.position);
			if(ds < shortest){
				shortest = ds;
				close = tar;
			}
		}
		target = close;
		return close != null;
	}

	@Override
	public void start() {
		
	}

	@Override
	public boolean update() {
		if(target.getState() != NpcState.TRAPPED){
			target = null;
			return false;
		}
		double ds = target.position.distanceSquared(owner.position);
		if(ds > max){
			target = null;
			return false;
		} else if(ds < 2){
			target.free();
			target = null;
			return false;
		}
		owner.lookAtEntity(target);
		boolean b = false;
		if(ds < 0.05){
			owner.setHorizontalVelocity(0, 0);
			if(target.position.y > owner.position.y) owner.jump();
		} else {
			owner.walk(speed, 0);
		}
		if(owner.collisionHorizontal && target.position.y > (owner.position.y + 0.43F) && !b){
			owner.jump();
		} else{
			AABB[] nb = owner.world.getBlockType(MathUtils.floor(owner.position.x), MathUtils.floor(owner.bounds.minY - 0.2D), MathUtils.floor(owner.position.z)).getCollisionBounds(owner.world, MathUtils.floor(owner.position.x), MathUtils.floor(owner.bounds.minY - 0.2D), MathUtils.floor(owner.position.z));
			 if(nb != null && nb.length != 0 && nb[0] != null) b = true;
			 if(target.position.y < (owner.position.y - 0.93F)) b = true;
			 if(!b){
				 owner.jump();
			 }
		}
		
		return true;
	}

}
