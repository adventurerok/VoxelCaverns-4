package vc4.vanilla.generation.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class FurnatureChair extends Furnature {

	int bid;
	int dir;

	@Override
	public void place(World world, Vector3l start, Direction dir) {
		start = pos.adjust(start, dir);
		int dat = 1 << ((dir.id() + this.dir) & 3);
		world.setBlockIdData(start.x, start.y, start.z, bid, dat);
	}

	public FurnatureChair(Adjustment pos, int bid, int dir) {
		super(pos);
		this.bid = bid;
		this.dir = dir;
	}
}
