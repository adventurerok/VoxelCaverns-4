package vc4.vanilla.generation.village.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.vanilla.generation.village.Village;

public class FurnatureVillagePlanks extends Furnature {

	int bid;
	
	@Override
	public void place(Village ville, Vector3l start, Direction dir) {
		start = pos.adjust(start, dir);
		ville.getWorld().setBlockIdData(start.x, start.y, start.z, bid, ville.getWoodType());
	}

	public FurnatureVillagePlanks(Adjustment pos, int bid) {
		super(pos);
		this.bid = bid;
	}

}
