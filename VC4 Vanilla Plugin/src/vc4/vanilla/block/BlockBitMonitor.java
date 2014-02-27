package vc4.vanilla.block;

import java.util.Random;

import vc4.api.block.BlockMultitexture;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockBitMonitor extends BlockMultitexture implements IBlockWrenchable {

	public BlockBitMonitor(int uid) {
		super(uid, BlockTexture.bitMonitor, "monitor");

	}

	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if (item.getDamage() == side) return BlockTexture.monitorInput;
		else return textureIndex;
	}

	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if ((world.getBlockData(x, y, z) & 7) == side) return BlockTexture.monitorInput;
		else return textureIndex;
	}

	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[] { new ItemStack(uid, 0, 1) };
	}

	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		return (world.getBlockData(x, y, z) & 7) != side;
	}

	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return BlockTexture.digital[0];
	}

	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		if ((world.getBlockData(x, y, z) & 16) != 0) return BlockTexture.digital[1];
		else return BlockTexture.digital[0];
	}

	@Override
	public boolean multitextureUsed(byte data, int side) {
		return data != side;
	}

	@Override
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		return (world.getBlockData(x, y, z) & 7) == side;
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		byte data = world.getBlockData(x, y, z);
		if (dir.id() != (data & 7)) return;
		long ax = x + dir.getX();
		long ay = y + dir.getY();
		long az = z + dir.getZ();
		boolean b = world.getBlockType(ax, ay, az).getProvidingSignal(world, ax, ay, az, dir.opposite().id()) > 0;
		if (((data & 16) != 0) != b) {
			world.setBlockData(x, y, z, (data & 15) + (b ? 16 : 0));
		}

	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		super.place(world, x, y, z, player, item);
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}

	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		if (buid != 1) return 0;
		Direction dir = Direction.getDirection(data & 7);
		long ax = x + dir.getX();
		long ay = y + dir.getY();
		long az = z + dir.getZ();
		boolean b = world.getBlockType(ax, ay, az).getProvidingSignal(world, ax, ay, az, dir.opposite().id()) > 0;
		if (((data & 16) != 0) != b) {
			world.setBlockData(x, y, z, (data & 15) + (b ? 16 : 0));
		}
		return 0;
	}

	@Override
	public void wrench(World world, long x, long y, long z, int side, int mouseButton) {
		byte data = world.getBlockData(x, y, z);
		if ((data & 7) == side) return;
		world.setBlockData(x, y, z, (data & 16) + side);
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}

}
