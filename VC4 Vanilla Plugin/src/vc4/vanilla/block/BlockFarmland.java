package vc4.vanilla.block;

import java.util.Random;

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
		blockOpacity[uid] = 1;
	}
	
	AABB cBounds = AABB.getBoundingBox(0, 1, 0, 0.96875, 0, 1);
	
	public boolean hasWater(World world, long x, long y, long z){
		for(int ax = -4; ax <= 4; ++ax){
			for(int az = -4; az <= 4; ++az){
				if(ax == 0 && az == 0) continue;
				for(int ay = 0; ay <= 1; ++ay){
					if(world.getBlockId(ax + x, ay + y, az + z) == Vanilla.water.uid) return true;
				}
			}
		}
		return false;
	}
	
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
		return side == 4 ? (item.getDamage() == 0 ? BlockTexture.farmland : BlockTexture.wetFarmland) : BlockTexture.dirt;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return side == 4 ? (world.getBlockData(x, y, z) == 0 ? BlockTexture.farmland : BlockTexture.wetFarmland) : BlockTexture.dirt;
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		boolean water = hasWater(world, x, y, z);
		if(data == 0 && water) world.setBlockData(x, y, z, 1);
		else if(data == 1 && !water) world.setBlockData(x, y, z, 0);
		return 0;
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
