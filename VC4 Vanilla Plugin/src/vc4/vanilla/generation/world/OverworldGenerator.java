/**
 * 
 */
package vc4.vanilla.generation.world;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import vc4.api.biome.*;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.graphics.*;
import vc4.api.gui.MapIcon;
import vc4.api.sound.Music;
import vc4.api.util.XORShiftRandom;
import vc4.api.util.noise.SimplexNoiseGenerator;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.vector.*;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.chunk.ChunkGenChasms;
import vc4.vanilla.generation.dungeon.room.DungeonRoomBase;
import vc4.vanilla.generation.plant.tree.TreeGenBasic;
import vc4.vanilla.generation.populate.*;
import vc4.vanilla.generation.recursive.RecursiveGenCaves;
import vc4.vanilla.generation.recursive.RecursiveGenVolcano;
import vc4.vanilla.generation.village.VillageGenerator;
import vc4.vanilla.underbiome.BiomeGenUnderBiomes;
import vc4.vanilla.underbiome.UnderBiome;

/**
 * @author paul
 * 
 */
public class OverworldGenerator implements WorldGenerator {
	
	private static class BiomeHeightGenerator {
		
		ZoomGenerator biome;
		ZoomGenerator height;
		
		public BiomeHeightGenerator(ZoomGenerator biome, ZoomGenerator height) {
			super();
			this.biome = biome;
			this.height = height;
		}
		
	}

	private RecursiveGenCaves caveGen = new RecursiveGenCaves();
	private WorldGenRuins ruinsGen = new WorldGenRuins();
	private WorldGenOres oresGen = new WorldGenOres();
	private DungeonRoomBase dungeonGen = new DungeonRoomBase();
	private VillageGenerator villageGen = new VillageGenerator();
	private WorldGenUndergroundLake waterLakeGen;
	private WorldGenUndergroundLake lavaLakeGen;
	private WorldGenDungeons dungeonsGen = new WorldGenDungeons();
	private WorldGenFloatingIslands floatingGen = new WorldGenFloatingIslands();
	private WorldGenPyramids pyramidsGen = new WorldGenPyramids();
	private WorldGenLightBerries lightBerryGen = new WorldGenLightBerries();
	private WorldGenAlgae algaeGen = new WorldGenAlgae();
	private WorldGenUndergroundClearing undergroundGrass = new WorldGenUndergroundClearing();
	private RecursiveGenVolcano volcanoGen = new RecursiveGenVolcano();
	ChunkGenChasms chasmGen = new ChunkGenChasms();
	ArrayList<ArrayList<Integer>> biomes;
	ZoomGenerator underBiomeGenerator;
	ConcurrentHashMap<Long, BiomeHeightGenerator> biomeHeightGens = new ConcurrentHashMap<>();
	long worldSeed;
	

	public static Vector3f[] lightColors = new Vector3f[256];

	static {
		for (int d = 0; d < 16; ++d) {
			Vector3f c = new Vector3f(1, 1, 1);
			float multi = 0.83f + (d * 0.007777f);
			for (int i = 15; i > -1; --i) {
				lightColors[d * 16 + i] = c.clone();
				c.x *= multi;
				c.y *= multi;
				c.z *= multi;
			}
		}

	}

	public ArrayList<ArrayList<Integer>> getBiomes() {
		return biomes;
	}

	@Override
	public Vector3f getLightColor(World world, MapData m, long x, long y, long z, int cx, int cz, int level) {
		if (y >= -5050)
			return lightColors[level];
		if (y <= -5144)
			return lightColors[240 + level];
		return lightColors[(int) (((14 - ((y + 5150) / 7)) << 4) + level)];
	}

	@Override
	public void onWorldLoad(World world) {
		waterLakeGen = new WorldGenUndergroundLake(Vanilla.water.uid, 100);
		lavaLakeGen = new WorldGenUndergroundLake(Vanilla.lava.uid, 175);
		biomes = Vanilla.biomes;
		underBiomeGenerator = new BiomeGenUnderBiomes(world.getSeed(), Vanilla.underBiomesList.toArray());
		for (int d = 0; d < 8; ++d)
			underBiomeGenerator = new BiomeGenZoom(world.getSeed(), underBiomeGenerator);
		Vanilla.underBiomesGen = underBiomeGenerator;
	}

	@Override
	public boolean hasBiomeMapGenerator(World world) {
		return true;
	}

	public int[] getNoiseDiff(World world, long x, long y, long z, SimplexOctaveGenerator noise) {
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

	public int[] getNoiseDiff(MapData data, long y) {
		int[] res = Arrays.copyOf(data.getGenHeightMap(), 1024);
		for (int d = 0; d < res.length; ++d) {
			res[d] = (int) (res[d] - ((y << 5) + 31));
		}
		return res;
	}

	public void getNoiseDiff(int[] res, long y) {
		for (int d = 0; d < res.length; ++d) {
			res[d] = (int) (res[d] - ((y << 5) + 31));
		}
	}

	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		Vector3l pos = player.position.toVector3l();
		if (pos.y < -5000)
			return Vanilla.musicHell;
		if (pos.y > 750)
			return Vanilla.musicSky;
		Biome biome = player.getWorld().getBiome(pos.x, pos.z);
		return biome.music;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldGenerator#generate(vc4.api.world.World, long,
	 * long, long)
	 */
	@Override
	public GeneratorOutput generate(World world, long x, long y, long z, MapData data) {
		SimplexOctaveGenerator hellNoise = null;
		GeneratorOutput out = new GeneratorOutput();
		
		//prepareGenerators(world);
		//biomeGenerator.generate(wx - 1, wz - 1, 34);
		//int rn[] = heightGenerator.generate(wx - 1, wz - 1, 34);
		//getNoiseDiff(rn, y);

		int[] nz = getNoiseDiff(data, y);
		Random rand = world.createRandom(x, y, z, 456564231L);
		Biome bio;
		int cz;
		long diff;
		int cy;
		int ubiomes[] = underBiomeGenerator.generate(x, z, 32);
		UnderBiome ubiom;
		boolean b;
		for (int cx = 0; cx < 32; ++cx) {
			for (cz = 0; cz < 32; ++cz) {
				ubiom = UnderBiome.byId(ubiomes[cz * 32 + cx]);
				bio = data.getBiome(cx, cz);
				diff = nz[cz * 32 + cx];
				//diff = rn[(cz + 1) * 34 + (cx + 1)];
				for (cy = 31; cy >= 0; --cy) {
					b = true;
					if (diff > 5000) {
						double big = -1 + (diff - 5000) / 500;
						if (big > 0.95)
							big = 1.9 - big;
						if (hellNoise == null)
							hellNoise = initHellNoise(world);
						b = hellNoise.noise((x << 5) + cx, (y << 5) + cy, (z << 5) + cz, 0.1f, 1f, true) >= big;
					}
					if (diff > -1 && b) {
						if (diff < bio.soilDepth) {
							bio.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
						} else if (diff < 5000) {
							out.setBlockId(cx, cy, cz, ubiom.stoneBlockId);
							out.setBlockData(cx, cy, cz, ubiom.stoneBlockData);
						} else
							out.setBlockId(cx, cy, cz, Vanilla.hellrock.uid);
					} else if (!b) {
						if ((y << 5) + cy < -5975) {
							out.setBlockId(cx, cy, cz, Vanilla.lava.uid);
							out.setBlockData(cx, cy, cz, 16);
						}
					} else if (diff < 0 && (y << 5) + cy < 0) {
						out.setBlockId(cx, cy, cz, Vanilla.water.uid);
						out.setBlockData(cx, cy, cz, 16);
					} else if (diff == -1) {
						bio.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
					}
					++diff;
				}
			}
		}
		if (world.getGeneratorTag().getBoolean("chasms", true))
			chasmGen.generate(world, data, x, y, z, out);
		if (world.getGeneratorTag().getBoolean("volcanos", true))
			volcanoGen.generate(world, data, x, y, z, out);
		if (world.getGeneratorTag().getBoolean("caves", true))
			caveGen.generate(world, data, x, y, z, out);
		return out;
	}

	/**
	 * @param world
	 */
	private SimplexOctaveGenerator initHellNoise(World world) {
		SimplexNoiseGenerator[] octaves = new SimplexNoiseGenerator[2];
		Random rand = new XORShiftRandom(world.getSeed());
		for (int d = 0; d < 2; ++d) {
			octaves[d] = new SimplexNoiseGenerator(rand);
			octaves[d].setScale(1 / (48d * (d * 2 + 1)));
		}
		// hellNoise = new SimplexOctaveGenerator(world, 4);
		return new SimplexOctaveGenerator(octaves);
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
	 * @see vc4.api.generator.WorldGenerator#populate(vc4.api.world.World, long,
	 * long, long)
	 */
	@Override
	public void populate(World world, long x, long y, long z) {
		if (y < -3 || !world.getBiome((x << 5) + 16, (z << 5) + 16).getType().equals(BiomeType.ocean)) {
			waterLakeGen.populate(world, x, y, z);
			lavaLakeGen.populate(world, x, y, z);
			undergroundGrass.populate(world, x, y, z);
			lightBerryGen.populate(world, x, y, z);
		}
		if (world.getGeneratorTag().getBoolean("dungeons", true)) {
			dungeonGen.generate(world, x, y, z);
			ruinsGen.populate(world, x, y, z);
			pyramidsGen.populate(world, x, y, z);
			dungeonsGen.populate(world, x, y, z);
		}
		if (world.getGeneratorTag().getBoolean("villages", true)) {
			villageGen.generate(world, x, y, z);
			villageGen.generateExtra(world, x, y, z);
		}
		if (y == 0 && world.getBiome((x << 5) + 16, (z << 5) + 16) == Vanilla.biomeSwamp) {
			algaeGen.populate(world, x, y, z);
		}
		if (world.getGeneratorTag().getBoolean("ores", true))
			oresGen.populate(world, x, y, z);
		if (y > 30) {
			floatingGen.populate(world, x, y, z);
			TreeGenBasic oakGen = new TreeGenBasic(world, new Random(world.getSeed() ^ 71927L ^ x ^ 139013L ^ y
					^ 1038794L ^ z));
			for (int d = 0; d < 100; ++d) {
				oakGen.generate((x << 5) + oakGen.rand.nextInt(32), (y << 5) + oakGen.rand.nextInt(32), (z << 5)
						+ oakGen.rand.nextInt(32), Vanilla.plantTreeOak);
			}
		}
	}

	@Override
	public void renderSkyBox(World world, EntityPlayer player) {
		Vector3d pos = player.getEyePos();
		OpenGL gl = Graphics.getOpenGL();
		gl.disable(GLFlag.DEPTH_TEST);
		Graphics.getClientShaderManager().unbindShader();
		if (pos.y < 192) {
			gl.begin(GLPrimative.QUADS);
			if (pos.y > -5200)
				gl.color(0.6f, 0.45f, 0.45f, 1);
			else
				gl.color(0.8f, 0.3f, 0.3f, 1);
			gl.vertex(-256, -256, -256);
			gl.vertex(256, -256, -256);
			gl.vertex(256, -256, 256);
			gl.vertex(-256, -256, 256);
			if (pos.y < -192) {
				gl.vertex(256, -256, -256);
				gl.vertex(256, 256, -256);
				gl.vertex(256, 256, 256);
				gl.vertex(256, -256, 256);

				gl.vertex(-256, -256, -256);
				gl.vertex(-256, 256, -256);
				gl.vertex(-256, 256, 256);
				gl.vertex(-256, -256, 256);

				gl.vertex(-256, -256, 256);
				gl.vertex(-256, 256, 256);
				gl.vertex(256, 256, 256);
				gl.vertex(256, -256, 256);

				gl.vertex(256, -256, -256);
				gl.vertex(256, 256, -256);
				gl.vertex(-256, 256, -256);
				gl.vertex(-256, -256, -256);
				if (pos.y < -256) {
					gl.vertex(-256, 256, -256);
					gl.vertex(256, 256, -256);
					gl.vertex(256, 256, 256);
					gl.vertex(-256, 256, 256);
				}
			}
			gl.end();
		}

	}

	public void prepareGenerators(World world) {
		if(worldSeed != world.getSeed()){
			biomeHeightGens.clear();
			worldSeed = world.getSeed();
		} else{
			BiomeHeightGenerator gen = biomeHeightGens.get(Thread.currentThread().getId());
			if(gen != null && gen.biome != null && gen.height != null) return;
		}
		int op = 0;
		ZoomGenerator bgen = new BiomeGenIslands(world.getSeed());
		// bgen = new BiomeGenZoom(world, bgen, false);
		// bgen = new BiomeGenIslands(world, bgen);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		bgen = new BiomeGenSuperBiome(world.getSeed(), bgen);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenBiome(world.getSeed(), bgen, biomes);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		ZoomGenerator hgen = new HeightGenSeed(world.getSeed());
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 0
		bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		float rVal = 1f;
		float rMod = 0.5f;
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 1
		bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new BiomeGenZoom(world.getSeed(), hgen);
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 2
		bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 3
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 4
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 5
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		hgen = new HeightGenDisplace(world.getSeed(), hgen, rVal *= rMod);
		((BiomeGenZoom) bgen).setSeeding((HeightGenBiomeInput) hgen); // 6
		hgen = new HeightGenZoom(world.getSeed(), hgen);
		hgen = new BiomeGenZoom(world.getSeed(), hgen);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true); // 7
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true); // 8
		
		BiomeHeightGenerator gen = new BiomeHeightGenerator(bgen, hgen);
		biomeHeightGens.put(Thread.currentThread().getId(), gen);
	}

	@Override
	public void generateMapData(World world, MapData data) {
		long wx = data.getPosition().x << 5;
		long wz = data.getPosition().y << 5;

		prepareGenerators(world);
		BiomeHeightGenerator gen = biomeHeightGens.get(Thread.currentThread().getId());

		int[] intBiomes = gen.biome.generate(wx, wz, 32);
		short[] biomes = new short[32 * 32];
		for (int d = 0; d < 1024; ++d)
			biomes[d] = (short) intBiomes[d];
		data.setBiomeMap(biomes);
		int[] gh = gen.height.generate(wx, wz, 32);
		int[] th = new int[1024];
		for (int d = 0; d < 1024; ++d) {
			th[d] = gh[d];
		}
		data.setGenHeightMap(gh);
		data.setHeightMap(th);

	}

	@Override
	public ZoomGenerator getBiomeMapGenerator(World world, int zoom) {
		int op = 0;
		ZoomGenerator bgen = new BiomeGenIslands(world.getSeed());
		// bgen = new BiomeGenZoom(world, bgen, false);
		// bgen = new BiomeGenIslands(world, bgen);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
		bgen = new BiomeGenSuperBiome(world.getSeed(), bgen);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenZoom(world.getSeed(), bgen, false);
		bgen = new BiomeGenBiome(world.getSeed(), bgen, biomes);
		if (zoom < 10) {
			bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
			bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
			if (zoom < 9) {
				bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
				bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
				if (zoom < 8) {
					bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
					bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
					if (zoom < 7) {
						bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
						bgen = new BiomeGenSubBiome(world.getSeed(), bgen, op++);
						if (zoom < 6) {
							bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
							if (zoom < 5) {
								bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
								if (zoom < 4) {
									bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
									if (zoom < 3) {
										bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
										if (zoom < 2) {
											bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
											if (zoom == 0)
												bgen = new BiomeGenZoom(world.getSeed(), bgen, true);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return bgen;
	}

	@Override
	public ArrayList<MapIcon> getMapIcons(World world, long x, long z, int size) {
		ArrayList<MapIcon> res = new ArrayList<>();
		if (size < 4097) {
			long ax = x >> 9;
			long az = z >> 9;
			int asize = size >> 9;
			long dx, dz;
			Random check;
			float px, pz;
			for (dx = 0; dx < asize; ++dx) {
				for (dz = 0; dz < asize; ++dz) {
					check = world.createRandom(ax + dx, az + dz, 119821868L);
					long lx = (((check.nextInt(16) << 5) + 16) + (dx << 9) + (ax << 9)) - x;
					long lz = (((check.nextInt(16) << 5) + 16) + (dz << 9) + (az << 9)) - z;
					px = lx / (float) size;
					pz = lz / (float) size;
					res.add(new MapIcon("village", new Vector2f(px, pz)));
				}
			}
		}
		return res;
	}

	@Override
	public boolean generatePlants(World world, long x, long y, long z) {
		return true;
	}

}
