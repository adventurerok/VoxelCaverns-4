package vc4.vanilla.entity.ai;

import vc4.api.entity.EntityLiving;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.ai.AI;
import vc4.api.math.MathUtils;
import vc4.api.util.AABB;

public class AIChasePlayer extends AI {

	EntityPlayer target;
	double max;
	double speed;
	
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
		for(EntityPlayer p : owner.world.getPlayers()){
			double ds = p.position.distanceSquared(owner.position);
			if(ds < shortest){
				shortest = ds;
				close = p;
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
		double ds = target.position.distanceSquared(owner.position);
		if(ds > max){
			target = null;
			return false;
		}
		owner.lookTargetEntity(target);
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
