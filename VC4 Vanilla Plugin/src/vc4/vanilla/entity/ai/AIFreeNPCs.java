package vc4.vanilla.entity.ai;

import java.util.List;

import vc4.api.entity.Entity;
import vc4.api.entity.EntityLiving;
import vc4.api.entity.ai.AI;
import vc4.api.entity.ai.Movement;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.entity.EntityNpc.NpcState;

public class AIFreeNPCs extends AI {

	EntityNpc target;
	double max;
	double speed;
	
	Movement move;
	
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
		List<Entity> titys = owner.world.getEntitiesInBoundsExcluding(owner.chunk, owner.bounds.expand(15, 15, 15), owner);
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
		if(target != null) owner.getMoveHandler().setExecuting(move = new Movement(target, 1, true));
		return close != null;
	}

	@Override
	public void start() {
		
	}

	@Override
	public boolean update() {
		if(target.getState() != NpcState.TRAPPED){
			target = null;
			owner.getMoveHandler().clearExecuting();
			return false;
		}
		double ds = target.position.distanceSquared(owner.position);
		if(ds > max){
			target = null;
			owner.getMoveHandler().clearExecuting();
			return false;
		} else if(ds < 2){
			target.free();
			target = null;
			owner.getMoveHandler().clearExecuting();
			return false;
		}
		if(!owner.getMoveHandler().isExecuting(move)) owner.getMoveHandler().setExecuting(move);
		return true;
//		owner.lookTargetEntity(target);
//		boolean b = false;
//		if(ds < 0.05){
//			owner.setHorizontalVelocity(0, 0);
//			if(target.position.y > owner.position.y) owner.jump();
//		} else {
//			owner.walk(speed, 0);
//		}
//		if(owner.collisionHorizontal && target.position.y > (owner.position.y + 0.43F) && !b){
//			owner.jump();
//		} else{
//			AABB[] nb = owner.world.getBlockType(MathUtils.floor(owner.position.x), MathUtils.floor(owner.bounds.minY - 0.2D), MathUtils.floor(owner.position.z)).getCollisionBounds(owner.world, MathUtils.floor(owner.position.x), MathUtils.floor(owner.bounds.minY - 0.2D), MathUtils.floor(owner.position.z));
//			 if(nb != null && nb.length != 0 && nb[0] != null) b = true;
//			 if(target.position.y < (owner.position.y - 0.93F)) b = true;
//			 if(!b){
//				 owner.jump();
//			 }
//		}
//		
//		return true;
	}

	@Override
	public void stop() {
	}

}
