package vc4.vanilla.block;

import java.util.Random;

import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockOrGate extends BlockLogicGate implements IBlockWrenchable {

	int side1[] = new int[15];
	int side2[] = new int[15];
	int lookup[][] = new int[6][6];

	public BlockOrGate(int uid) {
		super(uid);
		int dat = 0;
		for (int a = 0; a < 6; ++a) {
			for (int b = a + 1; b < 6; ++b) {
				side1[dat] = a;
				lookup[a][b] = dat;
				side2[dat++] = b;
			}
		}
	}

	@Override
	public int getTextureIndexGate(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		if (side1[data] == side) return BlockTexture.orGateInputA;
		if (side2[data] == side) return BlockTexture.orGateInputB;
		return BlockTexture.orGateOutput;
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		int data = world.getBlockData(x, y, z) & 15;
		if (side1[data] == dir.id() || side2[data] == dir.id()) world.scheduleBlockUpdate(x, y, z, 1, 1);
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		super.place(world, x, y, z, player, item);
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}

	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z);
		if (side1[data & 15] == side || side2[data & 15] == side) return 0;
		return (data & 16) != 0 ? 15 : 0;
	}

	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		Direction s1 = Direction.getDirection(side1[data & 15]);
		Direction s2 = Direction.getDirection(side2[data & 15]);
		boolean b1, b2;
		{
			long ax = x + s1.getX();
			long ay = y + s1.getY();
			long az = z + s1.getZ();
			b1 = world.getBlockType(ax, ay, az).getProvidingSignal(world, ax, ay, az, s1.opposite().id()) > 0;
		}
		{
			long ax = x + s2.getX();
			long ay = y + s2.getY();
			long az = z + s2.getZ();
			b2 = world.getBlockType(ax, ay, az).getProvidingSignal(world, ax, ay, az, s2.opposite().id()) > 0;
		}
		if (((data & 16) != 0) != (b1 | b2)) {
			world.setBlockData(x, y, z, (data & 15) + ((b1 | b2) ? 16 : 0));
		}
		return 0;
	}

	@Override
	public void wrench(World world, long x, long y, long z, int side, int mouseButton) {
		int data = world.getBlockData(x, y, z);
		int s1 = side1[data & 15];
		int s2 = side2[data & 15];
		if (side == s1 || side == s2) return;
		if (mouseButton == 1) s1 = side;
		else if (mouseButton == 0) s2 = side;
		if (s1 > s2) {
			int cop = s2;
			s2 = s1;
			s1 = cop;
		}
		data = (data & 16) + lookup[s1][s2];
		world.setBlockData(x, y, z, data);
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}

	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return BlockTexture.orGateOutput;
	}

}
