package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockCactus extends Block {

	AABB bounds;
	AABB top;
	
	public BlockCactus(int uid) {
		super(uid, 0, "cactus");
		double l = 0.0625D;
		double m = 1 - l;
		bounds = AABB.getBoundingBox(l, m, 0, 1, l, m);
		top = AABB.getBoundingBox(l, m, 0, 1 - 0.015265, l, m);
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		if(world.getBlockId(x, y + 1, z) != uid) return new AABB[]{top};
		return new AABB[]{bounds};
	}
	
	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		return bounds;
	}
	
	@Override
	public AABB getRenderSize(ItemStack item) {
		return bounds;
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return bounds;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if(side < 4) return BlockTexture.cactusSide;
		if(side == 4) return BlockTexture.cactusTop;
		else return BlockTexture.cactusBottom;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if(side < 4) return BlockTexture.cactusSide;
		if(side == 4) return BlockTexture.cactusTop;
		else return BlockTexture.cactusBottom;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		if(side < 4) return true;
		boolean sol = world.getNearbyBlockType(x, y, z, Direction.getDirection(side)).isSolid(world, x, y, z, Direction.getDirection(side).opposite().getId());
		boolean cact = world.getNearbyBlockId(x, y, z, Direction.getDirection(side)) == uid;
		return !sol && !cact;
	}

}
