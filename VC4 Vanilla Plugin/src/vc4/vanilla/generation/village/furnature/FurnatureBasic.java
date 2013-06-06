package vc4.vanilla.generation.village.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class FurnatureBasic extends Furnature {

	int bid;
	int data;
	
	@Override
	public void place(World world, Vector3l start, Direction dir) {
		start = pos.adjust(start, dir);
		world.setBlockIdData(start.x, start.y, start.z, bid, data);
	}

	public FurnatureBasic(Adjustment pos, int bid, int data) {
		super(pos);
		this.bid = bid;
		this.data = data;
	}

}
