package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Plant;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockFarmland extends Block {
	
	int grassType = Plant.getTypeId("grass");
	int cropType = Plant.getPlantData("grass").getVariantId("crop");

	public BlockFarmland(int uid) {
		super(uid, BlockTexture.farmland, "dirt");
	}
	
	AABB cBounds = AABB.getBoundingBox(0, 1, 0, 0.96875, 0, 1);
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return new AABB[]{cBounds};
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(Vanilla.dirt.uid, 0, 1)};
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return cBounds;
	}
	
	@Override
	public AABB getRenderSize(ItemStack item) {
		return cBounds;
	}
	
	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		return cBounds;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		return side == 4 ? BlockTexture.farmland : BlockTexture.dirt;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return side == 4 ? BlockTexture.farmland : BlockTexture.dirt;
	}
	
	@Override
	public boolean canGrowPlant(Plant plant) {
		return plant.getTypeId() == grassType && plant.getVariantId() == cropType;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return side == 5;
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		Direction d = Direction.getDirection(side);
		long ox = x + d.getX();
		long oy = y + d.getY();
		long oz = z + d.getZ();
		if (world.getBlockType(ox, oy, oz).isSolid(world, ox, oy, oz, d.opposite().id())) return false;
		if(side < 4 && world.getBlockId(ox, oy, oz) == uid) return false;
		return true;
	}

}
