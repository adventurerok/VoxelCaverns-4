package vc4.vanilla.block;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockButton extends Block {

	AABB bounds[] = new AABB[16];

	public BlockButton(int uid) {
		super(uid, BlockTexture.stone, "stone");
		float a = 1f / 8f;
		float b = 1f / 16f;
		bounds[0] = AABB.getBoundingBox(1 - a, 1, 0.25, 0.75, 0.25, 0.75);
		bounds[1] = AABB.getBoundingBox(0.25, 0.75, 0.25, 0.75, 1 - a, 1);
		bounds[2] = AABB.getBoundingBox(0, a, 0.25, 0.75, 0.25, 0.75);
		bounds[3] = AABB.getBoundingBox(0.25, 0.75, 0.25, 0.75, 0, a);
		bounds[4] = AABB.getBoundingBox(0.25, 0.75, 1 - a, 1, 0.25, 0.75);
		bounds[5] = AABB.getBoundingBox(0.25, 0.75, 0, a, 0.25, 0.75);
		bounds[0 + 8] = AABB.getBoundingBox(1 - b, 1, 0.25, 0.75, 0.25, 0.75);
		bounds[1 + 8] = AABB.getBoundingBox(0.25, 0.75, 0.25, 0.75, 1 - b, 1);
		bounds[2 + 8] = AABB.getBoundingBox(0, b, 0.25, 0.75, 0.25, 0.75);
		bounds[3 + 8] = AABB.getBoundingBox(0.25, 0.75, 0.25, 0.75, 0, b);
		bounds[4 + 8] = AABB.getBoundingBox(0.25, 0.75, 1 - b, 1, 0.25, 0.75);
		bounds[5 + 8] = AABB.getBoundingBox(0.25, 0.75, 0, b, 0.25, 0.75);
	}

	@Override
	public boolean overrideRightClick(World world, long x, long y, long z) {
		return true;
	}

	@Override
	public void onRightClick(World world, long x, long y, long z, int side, EntityPlayer player, ItemStack item) {
		if (player.getCoolDown() > 0.1) return;
		byte data = world.getBlockData(x, y, z);
		if ((data & 8) != 0) return;
		world.setBlockData(x, y, z, (data & 7) + 8);
		{
			Direction dir = Direction.getDirection(data & 7);
			long ax = x + dir.getX();
			long ay = y + dir.getY();
			long az = z + dir.getZ();
			world.notifyNear(ax, ay, az);
		}
		world.scheduleBlockUpdate(x, y, z, 10, 5);
		player.setCoolDown(200);
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
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return bounds[world.getBlockData(x, y, z)];
	}

	@Override
	public AABB getRenderSize(ItemStack item) {
		return bounds[item.getDamage()];
	}

	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		return bounds[world.getBlockData(x, y, z)];
	}

	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		if ((data & 8) == 0) return 0;
		if ((data & 7) != side) return 0;
		return 15;
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		Direction dir = Direction.getDirection(player.getRays().side).opposite();
		long ax = x + dir.getX();
		long ay = y + dir.getY();
		long az = z + dir.getZ();
		if (!world.getBlockType(ax, ay, az).isSolid(world, ax, ay, az, dir.opposite().id())) return;
		world.setBlockIdData(x, y, z, uid, dir.id());
		item.decrementAmount();
	}

	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}

	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[] { new ItemStack(uid, 0, 1) };
	}

	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		if (buid != 5) return 0;
		if ((data & 8) == 0) return 0;
		world.setBlockData(x, y, z, (data & 7));
		{
			Direction dir = Direction.getDirection(data & 7);
			long ax = x + dir.getX();
			long ay = y + dir.getY();
			long az = z + dir.getZ();
			world.notifyNear(ax, ay, az);
		}
		return 0;
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if (dir.id() != (world.getBlockData(x, y, z) & 7)) return;
		long ax = x + dir.getX();
		long ay = y + dir.getY();
		long az = z + dir.getZ();
		if (world.getBlockType(ax, ay, az).isSolid(world, ax, ay, az, dir.opposite().id())) return;
		dropItems(world, x, y, z, null);
		world.setBlockId(x, y, z, 0);

	}

}
