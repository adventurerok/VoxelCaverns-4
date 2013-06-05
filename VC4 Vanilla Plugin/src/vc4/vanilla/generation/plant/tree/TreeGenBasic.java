package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class TreeGenBasic extends TreeGen {

	
	public TreeGenBasic() {
		// TASK Auto-generated constructor stub
	}
	
	
	

	public TreeGenBasic(World world, Random rand) {
		super(world, rand);
		// TASK Auto-generated constructor stub
	}




	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = (byte) (plant.getSubId() % 100);
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plant)) return false;
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 5 + rand.nextInt(9);
		if(plant.getSubId() / 100 != 2){
			int gy, gz;
			for(int gx = 0; gx < 5; ++gx){
				for(gy = 0; gy < 5; ++gy){
					for(gz = 0; gz < 5; ++gz){
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
