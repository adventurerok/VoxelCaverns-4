package vc4.vanilla.generation.village.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.vanilla.generation.village.Village;

public class FurnatureTorch extends Furnature {

	int bid, dir;
	
	public FurnatureTorch(Adjustment pos, int bid, int dir) {
		super(pos);
		this.bid = bid;
		this.dir = dir;
	}

	@Override
	public void place(Village ville, Vector3l start, Direction dir) {
		if(this.dir == 0){
			ville.getWorld().setBlockId(start.x, start.y, start.z, bid);
			return;
		}
		int dar = this.dir - 1;
		int dat = 1 + ((dir.id() + dar) & 3);
		start = pos.adjust(start, dir);
		ville.getWorld().setBlockIdData(start.x, start.y, start.z, bid, dat);
	}

}
