package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class TreeGenRedwood extends TreeGen {

	public TreeGenRedwood(World world, Random rand) {
		super(world, rand);
	}
	
	public TreeGenRedwood() {
		// TASK Auto-generated constructor stub
	}

	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = (byte) plant.getData();
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plant)){
			return false;
		}
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 12 + rand.nextInt(24);
		for(int dofor = 0; dofor < height; ++dofor){
			setBlockAsWood(x, y + dofor, z, data);
		}
		if(plant.getVariantId() != DEAD_VARIANT){
			int reach = 0;
			long ax;
			int xMod;
			long az;
			for(long ay = y; ay < y + height + 2; ++ay){
				for(ax = x - reach; ax <= x + reach; ++ax){
					xMod = Math.abs((int) (ax - x));
					for(az = z - reach; az <= z + reach; ++az){
						int zMod = Math.abs((int) (az - z));
						if((xMod != reach || zMod != reach || reach < 1) && world.getBlockType(ax, ay, az).replacableBy(world, ax, ay, az, Vanilla.leaf.uid, data)){
							setBlockAsLeaf(ax, ay, az, data);
						}
					}
				}
				if(ay == y + height) --reach;
				else if(reach < 2) ++reach;
				else if(ay < 5 && reach < 3 && rand.nextInt(3) == 0) ++reach;
				else --reach;
			}
		}
		
		return true;
	}

}
