package vc4.vanilla.generation;

import java.util.Arrays;

import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class FlatlandsGenerator implements WorldGenerator {

	@Override
	public void onWorldLoad(World world) {
		

	}

	@Override
	public GeneratorOutput generate(World world, long x, long y, long z) {
		GeneratorOutput out = new GeneratorOutput();
		if(y < 0){
			Arrays.fill(out.blocks, world.getGeneratorTag().getShort("blockId", (short)1));
			Arrays.fill(out.data, world.getGeneratorTag().getNibble("blockData", (byte)0));
		}
		return out;
	}

	@Override
	public void populate(World world, long x, long y, long z) {
		
	}

	@Override
	public Vector3d getSpawnPoint(World world) {
		return new Vector3d(0, 1, 0);
	}

}
