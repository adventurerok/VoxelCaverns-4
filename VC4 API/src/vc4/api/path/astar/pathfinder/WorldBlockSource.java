package vc4.api.path.astar.pathfinder;

import vc4.api.world.World;

public class WorldBlockSource extends BlockSource{

	World world;
	
	@Override
	public int getBlockTypeIdAt(long x, long y, long z) {
		return world.getBlockId(x, y, z);
	}

}
