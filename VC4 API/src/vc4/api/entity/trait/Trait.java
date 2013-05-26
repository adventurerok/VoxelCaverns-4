package vc4.api.entity.trait;

import org.jnbt.CompoundTag;

import vc4.api.entity.Entity;

public abstract class Trait {

	Entity entity;
	
	public Trait(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
	
	public abstract void update();
	public abstract String name();
	public abstract boolean persistent();
	
	public void save(CompoundTag tag){
		
	}
	
	public void load(CompoundTag tag){
		
	}
	
	public void onDeath(){
		
	}
	
	public void onDamage(){
		
	}
}
