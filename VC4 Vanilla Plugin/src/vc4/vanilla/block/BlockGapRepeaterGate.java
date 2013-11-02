package vc4.vanilla.block;

import java.util.Random;

import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockGapRepeaterGate extends BlockLogicGate implements IBlockWrenchable {

	public BlockGapRepeaterGate(int uid) {
		super(uid);
	}
	
	@Override
	public int getTextureIndexGate(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		if(side == data) return BlockTexture.gapRepeaterGateInput;
		return BlockTexture.gapRepeaterGateOutput;
	}
	
	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		byte dat = world.getBlockData(x, y, z);
		if((dat & 15) == side) return 0;
		if((dat & 16) != 0) return 15;
		return 0;
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		byte data = world.getBlockData(x, y, z);
		if(Direction.getDirection(data & 15) != dir) return;
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		Direction dir = Direction.getDirection(data & 15);
		long ax = x + dir.getX();
		long ay = y + dir.getY();
		long az = z + dir.getZ();
		boolean b = world.getBlockType(ax, ay, az).getDirectSignal(world, ax, ay, az, dir.opposite().id()) > 0;
		if(((data & 16) != 0) != b){
			world.setBlockData(x, y, z, (data & 15) + (b ? 16 : 0));
		}
		return 0;
	}

	@Override
	public void wrench(World world, long x, long y, long z, int side, int mouseButton) {
		byte data = world.getBlockData(x, y, z);
		if((data & 15) == side) return;
		world.setBlockData(x, y, z, (data & 16) + side);
		world.scheduleBlockUpdate(x, y, z, 1, 1);
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return BlockTexture.gapRepeaterGateOutput;
	}
	

}
