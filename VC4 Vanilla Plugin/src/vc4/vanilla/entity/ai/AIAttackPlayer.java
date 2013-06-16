package vc4.vanilla.entity.ai;

import vc4.api.entity.*;
import vc4.api.entity.ai.AI;
import vc4.api.entity.ai.Movement;

public class AIAttackPlayer extends AI {

	EntityPlayer target;
	double max;
	double speed;
	int dmg;
	int lastAttack = 0;
	
	Movement move;
	
	public AIAttackPlayer(EntityLiving owner, double range, double speed, int dmg) {
		super(owner);
		max = range * range;
		this.speed = speed;
		this.dmg = dmg;
	}
	
	@Override
	public int priority() {
		return 300;
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
		move = new Movement(target, speed, false);
		owner.getMoveHandler().setExecuting(move);
	}
	
	@Override
	public void stop(){
		if(owner.getMoveHandler().isExecuting(move)) owner.getMoveHandler().clearExecuting();
	}

	@Override
	public boolean update() {
		if(!owner.getMoveHandler().isExecuting(move)) owner.getMoveHandler().setExecuting(move);
		double ds = target.position.distanceSquared(owner.position);
		if(ds > max){
			target = null;
			return false;
		}
		owner.lookTargetEntity(target);
		if(ds < 0.75 && lastAttack == 0){
			target.damage(dmg, DamageSource.melee(owner, null));
			lastAttack = 15;
		}
		if(lastAttack > 0) lastAttack--;
		return true;
	}

}
