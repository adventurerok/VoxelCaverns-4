package vc4.api.entity.ai;

import vc4.api.entity.EntityLiving;


public abstract class AI {

	EntityLiving owner;
	boolean isRunning;

	public AI(EntityLiving owner) {
		super();
		this.owner = owner;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public abstract boolean shouldStart();
	
	public abstract void start();
	
	public abstract boolean update();
}
