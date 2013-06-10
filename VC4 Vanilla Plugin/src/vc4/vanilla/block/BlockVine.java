package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererFace;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.tool.ToolType;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class BlockVine extends Block {

	public BlockVine(int uid, int texture, String material) {
		super(uid, texture, material);
		renderer = new BlockRendererFace();
		mineData = new MiningData().setRequired(ToolType.axe).setTimes(0.5, 0.03, 0.25).setPowers(0, 1, 25);
	}
	
	@Override
	public boolean canStandIn() {
		return true;
	}
	
	@Override
	public boolean canStandOn() {
		return false;
	}

	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}

	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
//		byte data = world.getBlockData(x, y, z);
//		switch (data & 3) {
//			case 1:
//				return new AABB[] { AABB.getBoundingBox(0.0F, 1.0F, 0.0f, 1.0F, 1.0F - 0.125, 1.0F) };
//			case 3:
//				return new AABB[] { AABB.getBoundingBox(0.0F, 1.0f, 0.0f, 1.0f, 0.0f, 0.125f) };
//			case 0:
//				return new AABB[] { AABB.getBoundingBox(1.0F - 0.125, 1.0f, 0.0F, 1.0F, 0.0F, 1.0F) };
//			case 2:
//				return new AABB[] { AABB.getBoundingBox(0.0F, 0.125F, 0.0F, 1f, 0f, 1.0F) };
//		}

		return null;
	}
	
	@Override
	public boolean render3d(byte data) {
		return false;
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		byte data = world.getBlockData(x, y, z);
		int num = 0;
		int last = -1;
		for(int d = 0; d < 4; ++d){
			if(((data >> d) & 1) != 0){
				++num;
				last = d;
			}
		}
		if(num > 1) return square;
		switch (last) {
			case 1:
				return AABB.getBoundingBox(0.0F, 1.0F, 0.0f, 1.0F, 1.0F - 0.125, 1.0F);
			case 3:
				return AABB.getBoundingBox(0.0F, 1.0f, 0.0f, 1.0f, 0.0f, 0.125f);
			case 0:
				return AABB.getBoundingBox(1.0F - 0.125, 1.0f, 0.0F, 1.0F, 0.0F, 1.0F);
			case 2:
				return AABB.getBoundingBox(0.0F, 0.125F, 0.0F, 1f, 0f, 1.0F);
			case -1:
				return AABB.getBoundingBox(0.0f, 1.0f, 1.0F - 0.125, 1.0F, 0.0F, 1.0F);
		}

		return null;
	}

	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		byte data = world.getBlockData(x, y, z);
		int num = 0;
		int last = -1;
		for(int d = 0; d < 4; ++d){
			if(((data >> d) & 1) != 0){
				++num;
				last = d;
			}
		}
		if(num > 1) return square;
		switch (last) {
			case 1:
				return AABB.getBoundingBox(0.0F, 1.0F, 0.0f, 1.0F, 1.0F - 0.125, 1.0F);
			case 3:
				return AABB.getBoundingBox(0.0F, 1.0f, 0.0f, 1.0f, 0.0f, 0.125f);
			case 0:
				return AABB.getBoundingBox(1.0F - 0.125, 1.0f, 0.0F, 1.0F, 0.0F, 1.0F);
			case 2:
				return AABB.getBoundingBox(0.0F, 0.125F, 0.0F, 1f, 0f, 1.0F);
			case -1:
				return AABB.getBoundingBox(0.0f, 1.0f, 1.0F - 0.125, 1.0F, 0.0F, 1.0F);
		}

		return null;
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		boolean ret = true;
		if (world.getBlockId(x, y + 1, z) == uid) ret = false;
		for (int d = 0; d < 4 && ret == true; ++d) {
			if (checkNearby(world, x, y, z, d)) ret = false;
		}
		if (ret) return;
		int dat = 0;
		for (int d = 0; d < 4; ++d) {
			if (checkNearby(world, x, y, z, d)){
				dat |= 1 << d;
			}
		}
		if(world.getBlockId(x, y + 1, z) == uid){
			dat |= world.getBlockData(x, y + 1, z);
		}
		world.setBlockIdData(x, y, z, uid, dat);
		item.decrementAmount();
	}

	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		if(side == 5) return false;
		if (side > 3) {
			return checkNearby(world, x, y, z, side);
		}
		int data = world.getBlockData(x, y, z);
		if (((data >> side) & 1) != 0) return true;
		return false;
	}
	
	public boolean checkNearby(World world, long x, long y, long z, int side){
		Block b = world.getNearbyBlockType(x, y, z, Direction.getDirection(side));
		return b.uid == Vanilla.leaf.uid || b.isSolid(world, x, y, z, Direction.getOpposite(side).getId());
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		boolean ret = true;
		if (world.getBlockId(x, y + 1, z) == uid) ret = false;
		for (int d = 0; d < 4 && ret == true; ++d) {
			if (checkNearby(world, x, y, z, d)) ret = false;
		}
		if (ret){
			world.setBlockId(x, y, z, 0);
			return;
		}
		int dat = 0;
		for (int d = 0; d < 4; ++d) {
			if (checkNearby(world, x, y, z, d)){
				dat |= 1 << d;
			}
		}
		if(world.getBlockId(x, y + 1, z) == uid){
			dat |= world.getBlockData(x, y + 1, z);
		}
		if(dat != world.getBlockData(x, y, z)) world.setBlockData(x, y, z, dat);
	}

	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

	@Override
	public Color getColor(ItemStack current, int side) {
		return BlockTallGrass.grass;
	}

	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		Biome bio = world.getBiome(x, z);
		if (bio != null) return bio.plantColor;
		else return BlockTallGrass.grass;
	}
	
	@Override
	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return bid != uid && bid != 0;
	}
	
	@Override
	public boolean canBeReplaced(int id, byte data) {
		return id != uid && id != 0;
	}

}
