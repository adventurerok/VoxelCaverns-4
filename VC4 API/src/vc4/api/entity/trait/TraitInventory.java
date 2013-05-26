package vc4.api.entity.trait;

import org.jnbt.CompoundTag;

import vc4.api.container.ContainerItems;
import vc4.api.entity.Entity;

public class TraitInventory extends Trait {

	ContainerItems inventory;
	
	public TraitInventory(Entity entity) {
		super(entity);
	}

	@Override
	public void update() {
		// TASK Auto-generated method stub
		
	}

	@Override
	public String name() {
		return "inventory";
	}

	@Override
	public boolean persistent() {
		return true;
	}
	
	@Override
	public void save(CompoundTag tag) {
		
	}
	
	@Override
	public void load(CompoundTag tag) {
		
	}

}
