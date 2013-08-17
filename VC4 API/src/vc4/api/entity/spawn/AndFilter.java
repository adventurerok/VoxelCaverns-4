package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class AndFilter implements SpawnFilter {
	
	SpawnFilter[] filters;
	
	

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		for(SpawnFilter s : filters){
			if(!s.canSpawn(world, x, y, z, rand)) return false;
		}
		return true;
	}



	public AndFilter(SpawnFilter...filters) {
		super();
		this.filters = filters;
	}

}
