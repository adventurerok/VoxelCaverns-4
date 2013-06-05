package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class TreeGenWillow extends TreeGen {

	public TreeGenWillow(World world, Random rand) {
		super(world, rand);
	}
	
	public TreeGenWillow() {
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant) {
		if(y < 0) return;
		int cx = rand.nextInt(32);
		int cz = rand.nextInt(32);
		long height = data.getHeight(cx, cz);
		if(height >> 5 != y) return;
		this.world = world;
		this.rand = rand;
		generate((x << 5) + cx, height + 1, (z << 5) + cz, plant);
	}

	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = (byte) (plant.getSubId() % 100);
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plant)){
			return false;
		}
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 5 + rand.nextInt(5);
		for(int dofor = 0; dofor < height; ++dofor){
			setBlockAsWood(x, y + dofor, z, data);
		}
		if(height > 8 && rand.nextInt(2) == 0){
			int at = rand.nextInt(8);
			int dir = rand.nextInt(4);
			setBlockAsWoodRelative(x, y + at, z, dir, data);
		}
		if(plant.getSubId() / 100 != 2){
			int range = 2 + rand.nextInt(2);
			int ax, az;
			for(int ay = 0; ay < 2; ++ay){
				for(ax = 0; ax <= range * 2; ++ax){
					if(ay == 1 && (ax == 0 || ax == range * 2)) continue;
					for(az = 0; az <= range * 2; ++az){
						if(ay == 1 && (az == 0 || az == range * 2)) continue;
						if(Math.abs(ax - range) == range && Math.abs(az - range) == range) continue;
						setBlockAsLeaf(x - range + ax, y + height - 1 + ay, z - range + az, data);
					}
				}
			}
			range++;
			int bot = 1 + rand.nextInt(4);
			for(int ay = height - 1; ay >= bot; --ay){
				for(ax = 0; ax <= range * 2; ++ax){
					for(az = 0; az <= range * 2; ++az){
						if(Math.abs(ax - range) == range && Math.abs(az - range) == range) continue;
						setBlockVine(x - range + ax, y + ay, z - range + az);
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public int getVineBlockId() {
		return Vanilla.willowVines.uid;
	}

}
