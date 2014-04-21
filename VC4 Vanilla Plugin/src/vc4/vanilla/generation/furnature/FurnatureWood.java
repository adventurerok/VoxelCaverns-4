package vc4.vanilla.generation.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class FurnatureWood extends Furnature {

	int bid;

	@Override
	public void place(World world, Vector3l start, Direction dir) {
		start = pos.adjust(start, dir);
		world.setBlockIdData(start.x, start.y, start.z, bid, world.createRandom(start.x, start.y, start.z).nextInt(7));
	}

	public FurnatureWood(Adjustment pos, int bid) {
		super(pos);
		this.bid = bid;
	}

}
