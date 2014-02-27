package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class BlockWireColored extends BlockWire {

	boolean connect;

	public BlockWireColored(int uid, Color max, Color min, boolean connect) {
		super(uid);
		this.max = max;
		this.min = min;
		this.connect = connect;
	}

	@Override
	public boolean joinTo(World world, long x, long y, long z, int side) {
		Direction dir = Direction.getDirection(side);
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		int ida = world.getBlockId(x, y, z);
		if (ida == uid || ida == Vanilla.wire.uid) return true;
		Block blah = Block.byId(ida);
		if (blah instanceof BlockWireColored) return false;
		if (connect) return blah.takesSignalInput(world, x, y, z, dir.opposite().id());
		return false;
	}

	@Override
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		if (connect) return true;
		int ida = world.getNearbyBlockId(x, y, z, side);
		if (ida != Vanilla.wire.uid && ida != uid) return false;
		return true;
	}

	@Override
	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		return connect ? super.getProvidingSignal(world, x, y, z, side) : 0;
	}

	@Override
	byte getSignalLevelAt(World world, long x, long y, long z, int side) {
		int ida = world.getBlockId(x, y, z);
		if (Block.byId(ida) instanceof BlockWireColored) {
			if (ida != uid) return 0;
			return world.getBlockData(x, y, z);
		}
		if (ida == Vanilla.wire.uid) return world.getBlockData(x, y, z);
		if (connect) return (byte) (world.getBlockType(x, y, z).getProvidingSignal(world, x, y, z, side) + 1);
		return 0;
	}

}
