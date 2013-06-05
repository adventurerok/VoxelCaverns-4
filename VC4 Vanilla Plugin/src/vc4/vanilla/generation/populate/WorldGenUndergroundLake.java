/**
 * 
 */
package vc4.vanilla.generation.populate;

import java.util.Random;

import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

/**
 * @author paul
 * 
 */
public class WorldGenUndergroundLake implements WorldPopulator {

	int waterId = Vanilla.water.uid;
	int chance = 50;
	
	

	public WorldGenUndergroundLake(int waterId, int chance) {
		super();
		this.waterId = waterId;
		this.chance = chance;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldPopulator#populate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public void populate(World world, long x, long y, long z) {
		if (y > -1 || y < -150) return;
		Random rand = world.createRandom(x, y, z, 16431642312398L / waterId + 3);
		if (rand.nextInt(chance) != 0) return;
		long cx = (x << 5) + rand.nextInt(32);
		long cy = (y << 5) + rand.nextInt(32);
		long cz = (z << 5) + rand.nextInt(32);
		int size = 7 + rand.nextInt(rand.nextInt(16) + 1);
		int sizeSq = size * size;
		int big = size + 2;
		int height = 2;
		if(rand.nextInt(6) == 0) height = 3;
		for (long ax = cx - big; ax <= cx + big; ++ax) {
			for (long az = cz - big; az <= cz + big; ++az) {
				int sq = (int) ((cx - ax) * (cx - ax) + (cz - az) * (cz - az));
				if(sq > sizeSq - 4 + rand.nextInt(8)) continue;
				int nHeight = height;
				if(sq > (sizeSq - 4 + rand.nextInt(8)) / 1.5){
					nHeight -= 1;
				}
				for (long ay = cy - nHeight; ay <= cy + nHeight; ++ay) {
					if(ay <= cy){
						if(world.getBlockId(ax, ay, az) != 0) world.setBlockId(ax, ay, az, waterId);
					} else world.setBlockId(ax, ay, az, 0);
				}
			}
		}
	}

}
