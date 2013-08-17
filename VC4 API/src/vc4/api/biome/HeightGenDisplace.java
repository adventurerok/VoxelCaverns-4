package vc4.api.biome;

import vc4.api.biome.ZoomGenerator;
import vc4.api.math.MathUtils;
import vc4.api.world.World;

public class HeightGenDisplace extends ZoomGenerator implements HeightGenBiomeInput{

	float rough;
	int[] biomes;
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		Biome bio;
		int pz;
		for(int px = 0; px < size; ++px){
			for(pz = 0; pz < size; ++pz){
				initSeed(x + px, z + pz);
				bio = Biome.byId(biomes[pz * size + px]);
				result[pz * size + px] += MathUtils.floor((nextInt(bio.diffHeight) + bio.minHeight - bio.midHeight) * rough);
				if(bio.enfHeight){
					if(result[pz * size + px] > bio.maxEnf) result[pz * size + px] = bio.maxEnf - 1 + nextInt(3);
					else if(result[pz * size + px] < bio.minEnf) result[pz * size + px] = bio.minEnf - 1 + nextInt(3);
				}
			}
		}
		return result;
	}

	public HeightGenDisplace(World world, ZoomGenerator parent, float rough) {
		super(world, parent);
		this.rough = rough;
	}
	
	@Override
	public void setBiomes(int[] biomes) {
		this.biomes = biomes;
	}
	

	


	
}
