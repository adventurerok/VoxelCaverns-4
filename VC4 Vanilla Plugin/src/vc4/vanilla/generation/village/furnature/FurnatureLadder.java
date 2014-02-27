package vc4.vanilla.generation.village.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.vanilla.generation.village.Village;

public class FurnatureLadder extends Furnature {

	int bid, dir;

	public FurnatureLadder(Adjustment pos, int bid, int dir) {
		super(pos);
		this.bid = bid;
		this.dir = dir;
	}

	@Override
	public void place(Village ville, Vector3l start, Direction dir) {
		int dat = (dir.id() + this.dir) & 3;
		start = pos.adjust(start, dir);
		ville.getWorld().setBlockIdData(start.x, start.y, start.z, bid, dat);
	}

}
