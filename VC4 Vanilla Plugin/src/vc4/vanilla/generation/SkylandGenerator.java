package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.sound.Music;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.vector.Vector3d;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.trees.TreeGenBasic;

public class SkylandGenerator implements WorldGenerator {

	@Override
	public void onWorldLoad(World world) {
		

	}

	@Override
	public GeneratorOutput generate(World world, long x, long y, long z, MapData data) {
		GeneratorOutput out = new GeneratorOutput();
		return out;
	}
	
	public int[] getNoiseDiff(World world, long x, long y, long z, SimplexOctaveGenerator noise) {
		int[] res = new int[32 * 32];
		long px = (x << 5);
		long pz = (z << 5);
		int cz;
		for (int cx = 0; cx < 32; ++cx) {
			for (cz = 0; cz < 32; ++cz) {
				res[cx * 32 + cz] = (int) ((noise.noise(px + cx, pz + cz, 0.1f, 1f) + 1) * 15);
				res[cx * 32 + cz] = (int) (res[cx * 32 + cz] - ((y << 5) + 31));
			}
		}
		return res;
	}

	@Override
	public void populate(World world, long x, long y, long z) {
		new WorldGenFloatingIslands().populate(world, x, y, z);
		TreeGenBasic oakGen = new TreeGenBasic(world, new Random(world.getSeed() ^ 71927L ^ x ^ 139013L ^ y ^ 1038794L ^ z));
		x <<= 5;
		y <<= 5;
		z <<= 5;
		for (int d = 0; d < 100; ++d) {
			oakGen.generate(x + oakGen.rand.nextInt(32), y + oakGen.rand.nextInt(32), z + oakGen.rand.nextInt(32), Vanilla.plantTreeOak);
		}
	}

	@Override
	public Vector3d getSpawnPoint(World world) {
		return null;
	}

	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		return Vanilla.musicSky;
	}

	@Override
	public void renderSkyBox(World world, EntityPlayer player) {
		// TASK Auto-generated method stub
		
	}

	@Override
	public void generateMapData(World world, MapData data) {
		
	}

}
