package vc4.api.block;

import vc4.api.entity.Entity;
import vc4.api.tileentity.TileEntity;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class OpenContainer {

	public Vector3l pos;
	public TileEntity entity;
	public boolean valid = true;
	
	public OpenContainer(World world, Vector3l pos) {
		this.pos = pos;
		entity = world.getTileEntity(pos.x, pos.y, pos.z);
		if(entity == null) valid = false;
	}
	
	public boolean update(Entity player){
		World world = player.getWorld();
		if(entity == null || world.getTileEntity(pos.x, pos.y, pos.z) != entity) return valid = false;
		Vector3l p = player.getBlockPos();
		if(p.distanceSquared(pos) > 25) return valid = false;
		return true;
	}
	
	public TileEntity getEntity() {
		return entity;
	}
	
	public Vector3l getPos() {
		return pos;
	}
	
	public boolean isValid() {
		return valid;
	}
}
