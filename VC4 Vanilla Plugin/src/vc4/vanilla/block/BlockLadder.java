package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererFace;
import vc4.api.entity.*;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.tool.ToolType;
import vc4.api.util.AABB;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockLadder extends Block {

	Color color = new Color(0x492100);
	int ench = 0;
	
	public BlockLadder(int uid) {
		super(uid, BlockTexture.ladder, "ladder");
		renderer = new BlockRendererFace();
		mineData = new MiningData().setRequired(ToolType.axe).setTimes(0.5, 0.03, 0.25).setPowers(0, 1, 25);
	}

	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return side == (world.getBlockData(x, y, z) & 3);
	}

	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}

	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		int data = (world.getBlockData(x, y, z) & 3);
		switch (data) {
			case 1:
				return new AABB[] { AABB.getBoundingBox(0.0F, 1.0F, 0.0f, 1.0F, 1.0F - 0.125, 1.0F) };
			case 3:
				return new AABB[] { AABB.getBoundingBox(0.0F, 1.0f, 0.0f, 1.0f, 0.0f, 0.125f) };
			case 0:
				return new AABB[] { AABB.getBoundingBox(1.0F - 0.125, 1.0f, 0.0F, 1.0F, 0.0F, 1.0F) };
			case 2:
				return new AABB[] { AABB.getBoundingBox(0.0F, 0.125F, 0.0F, 1f, 0f, 1.0F) };
			case -1:
				return new AABB[] { AABB.getBoundingBox(0.0f, 1.0f, 1.0F - 0.125, 1.0F, 0.0F, 1.0F) };
		}

		return null;
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		switch (world.getBlockData(x, y, z) & 3) {
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
	public boolean render3d(byte data) {
		return false;
	}

	@Override
	public boolean canStandIn() {
		return true;
	}

	@Override
	public boolean canStandOn() {
		return false;
	}
	
	public double getClimbSpeed(World world, long x, long y, long z){
		return 0.2 * (getEnchantmentLevel(world, x, y, z) * 0.5 + 1);
	}
	
	public double getFallSpeed(World world, long x, long y, long z){
		return 0.3 * (getEnchantmentLevel(world, x, y, z) * 0.1 + 1);
	}
	
	public int getEnchantmentLevel(World world, long x, long y, long z){
		return ench + ((world.getBlockData(x, y, z) >> 2) & 3);
	}

	@Override
	public void onEntityTickInside(World world, long x, long y, long z, Entity entity) {
		if (entity.isDead) return;
		EntityLiving creature = (EntityLiving) entity;
		if (creature.isSneaking()) creature.motionY = 0;
		creature.fallDistance = 0;
		if (creature.motionY < -getFallSpeed(world, x, y, z)) creature.motionY = -getFallSpeed(world, x, y, z);
		//if (creature.getMaxFallSpeed() > getFallSpeed(world, x, y, z) && MathUtils.equals(creature.motionY, -creature.getMaxFallSpeed())) creature.motionY = -getFallSpeed(world, x, y, z);
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(uid, world.getBlockData(x, y, z) & 12, 1)};
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		world.setBlockIdData(x, y, z, uid, (item.getData() & 12) + player.getSimpleFacing());
		item.decrementAmount();
	}

	@Override
	public Color getColor(ItemStack current, int side) {
		return color;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return color;
	}
	
	@Override
	public void onEntityCollideHorizontal(World world, long x, long y, long z, Entity entity) {
		if (entity.isDead) return;
		EntityLiving creature = (EntityLiving) entity;
		creature.fallDistance = 0;
		creature.motionY = getClimbSpeed(world, x, y, z);
	}

}
