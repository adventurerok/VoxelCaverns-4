package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class OrFilter implements SpawnFilter {
	
	SpawnFilter[] filters;
	
	

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		for(SpawnFilter s : filters){
			if(s.canSpawn(world, x, y, z, rand)) return true;
		}
		return false;
	}



	public OrFilter(SpawnFilter...filters) {
		super();
		this.filters = filters;
	}

}
