package vc4.api.generator;

import java.util.Random;

import vc4.api.world.World;

public abstract class RecursiveGenerator {

	public int range = 4;
	
	protected long currentSeed = 0;
	protected Random rand;
	
	protected abstract void generateRecursive(long x, long y, long z, long cx, long cy, long cz, GeneratorOutput data);
	
	public void generate(World world, long x, long y, long z, GeneratorOutput data){
		currentSeed = world.getSeed();
		
		for(long ax = x - range; ax <= x + range; ++ax){
			for(long ay = y - range; ay <= y + range; ++ay){
				for(long az = z - range; az <= z + range; ++az){
					rand = world.createRandom(ax, ay, az, 56451562L);
					generateRecursive(x, y, z, ax, ay, az, data);
				}
			}
		}
	}
}
