package vc4.vanilla.generation.trees;

import java.util.Random;

import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.TreeGen;

public class TreeGenBasic extends TreeGen {

	
	
	public TreeGenBasic(World world, Random rand) {
		super(world, rand);
	}

	@Override
	public boolean generate(long x, long y, long z, int tt) {
		byte data = (byte) (tt % 100);
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantTreeOak)){
			++y;
			if(!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantTreeOak)){
				++y;
				if(!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantTreeOak)) return false;
			}
		}
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 5 + rand.nextInt(9);
		if(tt / 100 != 2){
			for(int gx = 0; gx < 5; ++gx){
				for(int gy = 0; gy < 5; ++gy){
					for(int gz = 0; gz < 5; ++gz){
						if(gx == 0 || gx == 4){
							if(gy == 0 || gy == 4){
								if(gz == 0 || gz == 4) continue;
							}
						}
						setBlockAsLeaf(x - 2 + gx, y - 2 + gy + height - 1, z - 2 + gz, data);
					}
				}
			}
		}
		for(int dofor = 0; dofor < height; ++dofor){
			setBlockAsWood(x, y + dofor, z, data);
		}
		if(height > 8 && rand.nextInt(2) == 0){
			int at = rand.nextInt(8);
			int dir = rand.nextInt(4);
			setBlockAsWoodRelative(x, y + at, z, dir, data);
		}
		return true;
	}

}
