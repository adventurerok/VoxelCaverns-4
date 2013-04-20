package vc4.api.entity;

import vc4.api.world.World;

public class EntityLiving extends Entity {

	
	public EntityLiving(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}

	boolean sneaking;
	
	public boolean isSneaking() {
		return sneaking;
	}
}
