package vc4.api.world;

import org.jnbt.CompoundTag;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3d;

public interface ThingImporter {

	public void importBlock(World world, long x, long y, long z, int id, int data);

	public void importSpecial(World world, Vector3d base, Direction dir, CompoundTag special);

}
