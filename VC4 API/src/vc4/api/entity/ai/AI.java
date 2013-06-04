package vc4.api.entity.ai;

import vc4.api.entity.EntityLiving;


public abstract class AI {

	protected EntityLiving owner;
	boolean isRunning;
	private boolean disabled;

	public AI(EntityLiving owner) {
		super();
		this.owner = owner;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int conflictId(){
		return 0;
	}
	
	public int priority(){
		return 100;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public abstract boolean shouldStart();
	
	public abstract void start();
	
	public abstract boolean update();

	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
