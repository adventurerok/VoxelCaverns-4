package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.*;
import vc4.api.item.ItemStack;
import vc4.api.util.*;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.block.render.BlockRendererLogicGate;

public class BlockLogicGate extends BlockMultitexture implements IBlockLogicGate {

	AABB bounds = AABB.getBoundingBox(0.25, 0.75, 0.25, 0.75, 0.25, 0.75);
	
	public BlockLogicGate(int uid) {
		super(uid, BlockTexture.wire, "wire");
		renderer = new BlockRendererLogicGate();
		blockOpacity[uid] = 1;
		blockLight[uid] = 10;
	}
	
	@Override
	public boolean render3d(byte data) {
		return false;
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(uid, 0, 1)};
	}

	@Override
	public boolean joinTo(World world, long x, long y, long z, int side) {
		Direction dir = Direction.getDirection(side);
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		return world.getBlockType(x, y, z).takesSignalInput(world, x, y, z, dir.opposite().id());
	}
	
	@Override
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		return true;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	Color max = new Color(99, 0, 255);
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return Colors.gray;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return Colors.gray;
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		boolean sides[] = PipeUtils.getPipeAttachedSides(world, x, y, z);
		if (sides == null) return new AABB[0];
		double bot = getDistanceSideToBlock(world.getBlockData(x, y, z));
		double top = 1 - bot;
		if (BooleanUtils.areAllFalse(sides)) { return new AABB[] { AABB.getBoundingBox(bot, top, bot, top, bot, top) }; }
		AABB ns = null;
		AABB ew = null;
		AABB tb = null;

		if (sides[0] || sides[2]) {
			ns = AABB.getBoundingBox(sides[2] ? 0 : bot, sides[1] ? 1 : top, bot, top, bot, top);
		}
		if (sides[1] || sides[3]) {
			ew = AABB.getBoundingBox(bot, top, bot, top, sides[3] ? 0 : bot, sides[1] ? 1 : top);
		}
		if (sides[4] || sides[5]) {
			tb = AABB.getBoundingBox(bot, top, sides[5] ? 0 : bot, sides[4] ? 1 : top, bot, top);
		}

		int amount = ns == null ? 0 : 1;
		if (ew != null) ++amount;
		if (tb != null) ++amount;

		AABB[] result = new AABB[amount];
		int at = 0;
		if (ns != null) result[at++] = ns;
		if (ew != null) result[at++] = ew;
		if (tb != null) result[at++] = tb;

		return result;
	}
	
	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		return 0;
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
//		boolean sides[] = PipeUtils.getPipeAttachedSides(world, x, y, z);
//		if (sides == null) return null;
//		double bot = 0.25;
//		double top = 1 - bot;
//		if (BooleanUtils.areAllFalse(sides)) { return bounds; }
//		AABB result = bounds.clone();
//		if (sides[0] || sides[2]) {
//			result.minX = sides[2] ? 0 : bot;
//			result.maxX = sides[0] ? 1 : top;
//		}
//		if (sides[1] || sides[3]) {
//			result.minZ = sides[3] ? 0 : bot;
//			result.maxZ = sides[1] ? 1 : top;
//		}
//		if (sides[4] || sides[5]) {
//			result.minY = sides[5] ? 0 : bot;
//			result.maxY = sides[4] ? 1 : top;
//		}
//
//		return result;
		return bounds;
	}

	@Override
	public float getDistanceSideToBlock(byte data) {
		return 0.375f;
	}

	@Override
	public AABB getGateBounds(World world, long x, long y, long z) {
		return bounds;
	}

	@Override
	public int getTextureIndexGate(World world, long x, long y, long z, int side) {
		return 0;
	}

	@Override
	public int getTextureIndexGate(ItemStack item, int side) {
		return 0;
	}

	@Override
	public Color getColorGate(World world, long x, long y, long z, int side) {
		return Color.white;
	}

	@Override
	public Color getColorGate(ItemStack item, int side) {
		return Color.white;
	}
	
	@Override
	public boolean updatesRandomly() {
		return false;
	}
	
	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public boolean multitextureUsed(byte data, int side) {
		return true;
	}

}
