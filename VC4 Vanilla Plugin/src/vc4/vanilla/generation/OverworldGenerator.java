/**
 * 
 */
package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.util.noise.SimplexNoiseGenerator;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.DungeonRoomBase;
import vc4.vanilla.generation.trees.TreeGenBasic;

/**
 * @author paul
 * 
 */
public class OverworldGenerator implements WorldGenerator {

	private RecursiveGenCaves caveGen = new RecursiveGenCaves();
	private WorldGenRuins ruinsGen = new WorldGenRuins();
	private WorldGenOres oresGen = new WorldGenOres();
	private DungeonRoomBase dungeonGen = new DungeonRoomBase();
	private WorldGenUndergroundLake waterLakeGen;
	private WorldGenUndergroundLake lavaLakeGen;
	private WorldGenDungeons dungeonsGen = new WorldGenDungeons();
	SimplexOctaveGenerator noise;
	SimplexOctaveGenerator hellNoise;
	ChunkGenChasms chasmGen = new ChunkGenChasms();

	@Override
	public void onWorldLoad(World world) {
		waterLakeGen = new WorldGenUndergroundLake(Vanilla.water.uid, 100);
		lavaLakeGen = new WorldGenUndergroundLake(Vanilla.lava.uid, 175);
	}

	public int[] getNoiseDiff(World world, long x, long y, long z) {
		int[] res = new int[32 * 32];
		long px = (x << 5);
		long pz = (z << 5);
		for (int cx = 0; cx < 32; ++cx) {
			for (int cz = 0; cz < 32; ++cz) {
				res[cx * 32 + cz] = (int) ((noise.noise(px + cx, pz + cz, 0.1f, 1f) + 1) * 15);
				res[cx * 32 + cz] = (int) (res[cx * 32 + cz] - ((y << 5) + 31));
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldGenerator#generate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public GeneratorOutput generate(World world, long x, long y, long z) {
		noise = new SimplexOctaveGenerator(world, 4);
		noise.setScale(1 / 96f);
		hellNoise = null;
		GeneratorOutput out = new GeneratorOutput();
		int[] nz = getNoiseDiff(world, x, y, z);
		for (int cx = 0; cx < 32; ++cx) {
			for (int cz = 0; cz < 32; ++cz) {
				long diff = nz[cx * 32 + cz];
				for (int cy = 31; cy >= 0; --cy) {
					boolean b = true;
					if (diff > 5000) {
						double big = -1 + (diff - 5000) / 500;
						if (big > 0.95) big = 1.9 - big;
						if (hellNoise == null) initHellNoise(world);
						b = hellNoise.noise((x << 5) + cx, (y << 5) + cy, (z << 5) + cz, 0.1f, 1f, true) >= big;
					}
					if (diff > -1 && b) {
						if (diff == 0) out.setBlockId(cx, cy, cz, Vanilla.grass.uid);
						else if (diff < 6) out.setBlockId(cx, cy, cz, Vanilla.dirt.uid);
						else if (diff < 5000) out.setBlockId(cx, cy, cz, 1);
						else out.setBlockId(cx, cy, cz, Vanilla.hellrock.uid);
					} else if (!b) {
						if ((y << 5) + cy < -5975) {
							out.setBlockId(cx, cy, cz, Vanilla.lava.uid);
						}
					}
					++diff;
				}
			}
		}
		if (world.getGeneratorTag().getBoolean("caves", true)) caveGen.generate(world, x, y, z, out);
		if (world.getGeneratorTag().getBoolean("chasms", true)) chasmGen.generate(world, x, y, z, out);
		return out;
	}

	/**
	 * @param world
	 */
	private void initHellNoise(World world) {
		SimplexNoiseGenerator[] octaves = new SimplexNoiseGenerator[4];
		Random rand = new Random(world.getSeed());
		for (int d = 0; d < 2; ++d) {
			octaves[d] = new SimplexNoiseGenerator(rand);
			octaves[d].setScale(1 / (48d * (d * 2 + 1)));
		}
		// hellNoise = new SimplexOctaveGenerator(world, 4);
		hellNoise = new SimplexOctaveGenerator(octaves);
		// hellNoise.setScale(1/96f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldGenerator#getSpawnPoint(vc4.api.world.World)
	 */
	@Override
	public Vector3d getSpawnPoint(World world) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldGenerator#populate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public void populate(World world, long x, long y, long z) {
		waterLakeGen.populate(world, x, y, z);
		lavaLakeGen.populate(world, x, y, z);
		if (world.getGeneratorTag().getBoolean("dungeons", true)) {
			dungeonGen.generate(world, x, y, z);
			ruinsGen.populate(world, x, y, z);
			dungeonsGen.populate(world, x, y, z);
		}
		TreeGenBasic oakGen = new TreeGenBasic(world, new Random(world.getSeed() ^ 71927L ^ x ^ 139013L ^ y ^ 1038794L ^ z));
		for (int d = 0; d < 100; ++d) {
			oakGen.generate((x << 5) + oakGen.rand.nextInt(32), (y << 5) + oakGen.rand.nextInt(32), (z << 5) + oakGen.rand.nextInt(32), 0);
		}
		if(world.getGeneratorTag().getBoolean("ores", true)) oresGen.populate(world, x, y, z);
	}

}
