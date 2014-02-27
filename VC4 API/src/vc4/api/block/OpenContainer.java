package vc4.api.block;

import vc4.api.entity.Entity;
import vc4.api.tileentity.TileEntity;
import vc4.api.tileentity.TileEntityContainer;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class OpenContainer {

	public Vector3l pos;
	public TileEntityContainer entity;
	public boolean valid = true;

	public OpenContainer(World world, Vector3l pos) {
		this.pos = pos;
		TileEntity e = world.getTileEntity(pos.x, pos.y, pos.z);
		if (e == null || !(e instanceof TileEntityContainer)) {
			valid = false;
			return;
		}
		entity = (TileEntityContainer) e;

	}

	public OpenContainer(TileEntityContainer c) {
		entity = c;
		pos = c.position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		OpenContainer other = (OpenContainer) obj;
		if (pos == null) {
			if (other.pos != null) return false;
		} else if (!pos.equals(other.pos)) return false;
		return true;
	}

	public boolean update(Entity player) {
		if (!valid) return false;
		World world = player.getWorld();
		if (entity == null || world.getTileEntity(pos.x, pos.y, pos.z) != entity) return valid = false;
		Vector3l p = player.getBlockPos();
		if (p.distanceSquared(pos) > 49) return valid = false;
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
