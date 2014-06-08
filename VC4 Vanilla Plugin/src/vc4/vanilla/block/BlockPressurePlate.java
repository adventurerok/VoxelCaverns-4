package vc4.vanilla.block;

import java.awt.Color;
import java.util.Random;

import vc4.api.block.*;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockPressurePlate extends BlockMultitexture {

	private static AABB up = AABB.getBoundingBox(1/16d, 15/16d, 0, 1/8d, 1/16d, 15/16d);
	private static AABB down = AABB.getBoundingBox(1/16d, 15/16d, 0, 1/16d, 1/16d, 15/16d);
	
	public BlockPressurePlate(int uid) {
		super(uid, 0, "logic");
		setLightOpacity(1);
		
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return new AABB[0];
	}
	
	@Override
	public AABB getRenderSize(ItemStack item) {
		return up;
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if(!world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) return;
		super.place(world, x, y, z, player, item);
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(dir != Direction.DOWN) return;
		if(world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) return;
		dropItems(world, x, y, z, null);
		if((world.getBlockData(x, y, z) & 1) == 1) world.notifyNear(x, y - 1, z);
		world.setBlockId(x, y, z, 0);
	}
	
	@Override
	public void onEntityTickInside(World world, long x, long y, long z, Entity entity) {
		byte data = world.getBlockData(x, y, z);
		if((data & 1) == 0){
			world.setBlockData(x, y, z, data | 1);
			world.notifyNear(x, y - 1, z);
			world.scheduleBlockUpdate(x, y, z, 2, 5);
		}
	}
	
	@Override
	public void onBlockExploded(World world, long x, long y, long z, Entity exploder) {
		if((world.getBlockData(x, y, z) & 1) == 1) world.notifyNear(x, y - 1, z);
	}
	
	@Override
	public void onBlockMined(World world, long x, long y, long z, ItemStack mined) {
		if((world.getBlockData(x, y, z) & 1) == 1) world.notifyNear(x, y - 1, z);
	}
	
	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		if(side != 5) return 0;
		byte data = world.getBlockData(x, y, z);
		if((data & 1) == 0) return 0;
		return 15;
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		if(buid != 5) return 0;
		if((data & 1) == 0) return 0;
		if(world.getEntitiesInBounds(down).size() < 1){
			world.setBlockData(x, y, z, data ^ 1);
			world.notifyNear(x, y - 1, z);
			return 0;
		}
		return 2;
	}
	
	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		return (world.getBlockData(x, y, z) & 1) == 1 ? down : up;
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return getRenderSize(world, x, y, z);
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return world.getBlockType(x, y - 1, z).getTextureIndex(world, x, y - 1, z, 4);
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		return BlockTexture.stone;
	}

	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return world.getBlockType(x, y - 1, z).getColor(world, x, y - 1, z, side);
	}
	
	@Override
	public boolean multitextureUsed(byte data, int side) {
		return false;
	}
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return ((IBlockMultitexture)world.getBlockType(x, y - 1, z)).getTextureIndexMultitexture(world, x, y -1 , z, 4);
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return ((IBlockMultitexture)world.getBlockType(x, y - 1, z)).getColorMultitexture(world, x, y -1 , z, 4);
	}
	
	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		Block below = world.getBlockType(x, y - 1, z);
		if(!(below instanceof IBlockMultitexture)) return false;
		return ((IBlockMultitexture)below).renderSideMultitexture(world, x, y - 1, z, 4);
	}
	
}
