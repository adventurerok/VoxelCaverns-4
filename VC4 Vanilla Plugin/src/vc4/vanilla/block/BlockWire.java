package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.IBlockJoinable;
import vc4.api.block.render.BlockRendererJoinable;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.*;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockWire extends Block implements IBlockJoinable {

	Color max = new Color(99, 0, 255);
	Color min = new Color(22, 0, 64);

	public BlockWire(int uid) {
		super(uid, BlockTexture.wire, "wire");
		renderer = new BlockRendererJoinable();
		blockOpacity[uid] = 1;
	}

	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[] { new ItemStack(uid, 0, 1) };
	}

	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		return world.getBlockData(x, y, z);
	}

	@Override
	public boolean render3d(byte data) {
		return false;
	}

	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
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
	public float getDistanceSideToBlock(byte data) {
		return 0.375f;
	}

	@Override
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		return true;
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
	public Color getColor(World world, long x, long y, long z, int side) {
		float amount = world.getBlockData(x, y, z) / 15F;
		return ColorUtils.differColors(min, max, amount);
	}

	@Override
	public Color getColor(ItemStack current, int side) {
		return max;
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		super.place(world, x, y, z, player, item);
		updateSignal(world, x, y, z);
	}

	public void updateSignal(World world, long x, long y, long z) {
		byte l_0 = (byte) (getSignalLevelAt(world, x + 1, y, z, 2) - 1);
		byte l_1 = (byte) (getSignalLevelAt(world, x, y, z + 1, 3) - 1);
		byte l_2 = (byte) (getSignalLevelAt(world, x - 1, y, z, 0) - 1);
		byte l_3 = (byte) (getSignalLevelAt(world, x, y, z - 1, 1) - 1);
		byte l_4 = (byte) (getSignalLevelAt(world, x, y + 1, z, 5) - 1);
		byte l_5 = (byte) (getSignalLevelAt(world, x, y - 1, z, 4) - 1);
		byte power = 0;
		if (l_0 > power) power = l_0;
		if (l_1 > power) power = l_1;
		if (l_2 > power) power = l_2;
		if (l_3 > power) power = l_3;
		if (l_4 > power) power = l_4;
		if (l_5 > power) power = l_5;
		if (power != world.getBlockData(x, y, z)) {
			world.setBlockData(x, y, z, power);
		}
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		updateSignal(world, x, y, z);
	}

	byte getSignalLevelAt(World world, long x, long y, long z, int side) {
		if (world.getBlockType(x, y, z) instanceof BlockWire) return world.getBlockData(x, y, z);
		return (byte) (world.getBlockType(x, y, z).getProvidingSignal(world, x, y, z, side) + 1);
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		boolean sides[] = PipeUtils.getPipeAttachedSides(world, x, y, z);
		if (sides == null) return null;
		byte data = world.getBlockData(x, y, z);
		double bot = getDistanceSideToBlock(data);
		double top = 1 - bot;
		if (BooleanUtils.areAllFalse(sides)) { return AABB.getBoundingBox(bot, top, bot, top, bot, top); }
		AABB result = AABB.getBoundingBox(bot, top, bot, top, bot, top);
		if (sides[0] || sides[2]) {
			result.minX = sides[2] ? 0 : bot;
			result.maxX = sides[0] ? 1 : top;
		}
		if (sides[1] || sides[3]) {
			result.minZ = sides[3] ? 0 : bot;
			result.maxZ = sides[1] ? 1 : top;
		}
		if (sides[4] || sides[5]) {
			result.minY = sides[5] ? 0 : bot;
			result.maxY = sides[4] ? 1 : top;
		}

		return result;
	}

}
