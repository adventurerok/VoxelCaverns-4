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
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				createRandom(x + px, z + pz);
				Biome bio = Biome.byId(biomes[pz * size + px]);
				result[pz * size + px] += MathUtils.floor((rand.nextInt(bio.diffHeight) + bio.minHeight - bio.midHeight) * rough);
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
