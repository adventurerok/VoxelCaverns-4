/**
 * 
 */
package vc4.vanilla.generation.world;

import java.util.*;

import vc4.api.biome.*;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.graphics.*;
import vc4.api.sound.Music;
import vc4.api.util.noise.SimplexNoiseGenerator;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.chunk.ChunkGenChasms;
import vc4.vanilla.generation.dungeon.room.DungeonRoomBase;
import vc4.vanilla.generation.plant.tree.TreeGenBasic;
import vc4.vanilla.generation.populate.*;
import vc4.vanilla.generation.recursive.RecursiveGenCaves;
import vc4.vanilla.generation.recursive.RecursiveGenVolcano;
import vc4.vanilla.generation.village.building.BuildingHouse;

/**
 * @author paul
 * 
 */
public class OverworldGenerator implements WorldGenerator {

	private RecursiveGenCaves caveGen = new RecursiveGenCaves();
	private WorldGenRuins ruinsGen = new WorldGenRuins();
	private WorldGenOres oresGen = new WorldGenOres();
	private DungeonRoomBase dungeonGen = new DungeonRoomBase();
	private BuildingHouse villageGen = new BuildingHouse();
	private WorldGenUndergroundLake waterLakeGen;
	private WorldGenUndergroundLake lavaLakeGen;
	private WorldGenDungeons dungeonsGen = new WorldGenDungeons();
	private WorldGenFloatingIslands floatingGen = new WorldGenFloatingIslands();
	private WorldGenPyramids pyramidsGen = new WorldGenPyramids();
	private RecursiveGenVolcano volcanoGen = new RecursiveGenVolcano();
	ChunkGenChasms chasmGen = new ChunkGenChasms();
	ArrayList<ArrayList<Integer>> biomes;
	
	
	public ArrayList<ArrayList<Integer>> getBiomes() {
		return biomes;
	}

	@Override
	public void onWorldLoad(World world) {
		waterLakeGen = new WorldGenUndergroundLake(Vanilla.water.uid, 100);
		lavaLakeGen = new WorldGenUndergroundLake(Vanilla.lava.uid, 175);
		biomes = Vanilla.biomes;
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
		int[] res = Arrays.copyOf(data.getHeightMap(), 1024);
		for (int cx = 0; cx < 32; ++cx) {
			for (int cz = 0; cz < 32; ++cz) {
				res[cz * 32 + cx] = (int) (res[cz * 32 + cx] - ((y << 5) + 31));
			}
		}
		return res;
	}

	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		Vector3l pos = player.position.toVector3l();
		if (pos.y < -5000) return Vanilla.musicHell;
		if (pos.y > 750) return Vanilla.musicSky;
		Biome biome = player.getWorld().getBiome(pos.x, pos.z);
		return biome.music;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.generator.WorldGenerator#generate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public GeneratorOutput generate(World world, long x, long y, long z, MapData data) {
		SimplexOctaveGenerator hellNoise = null;
		GeneratorOutput out = new GeneratorOutput();
		int[] nz = getNoiseDiff(data, y);
		Random rand = world.createRandom(x, y, z, 456564231L);
		Biome bio;
		int cz;
		long diff;
		int cy;
		boolean b;
		for (int cx = 0; cx < 32; ++cx) {
			for (cz = 0; cz < 32; ++cz) {
				bio = data.getBiome(cx, cz);
				diff = nz[cz * 32 + cx];
				for (cy = 31; cy >= 0; --cy) {
					b = true;
					if (diff > 5000) {
						double big = -1 + (diff - 5000) / 500;
						if (big > 0.95) big = 1.9 - big;
						if (hellNoise == null) hellNoise = initHellNoise(world);
						b = hellNoise.noise((x << 5) + cx, (y << 5) + cy, (z << 5) + cz, 0.1f, 1f, true) >= big;
					}
					if (diff > -1 && b) {
						if (diff < bio.soilDepth){
							bio.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
						}
						else if (diff < 5000) out.setBlockId(cx, cy, cz, 1);
						else out.setBlockId(cx, cy, cz, Vanilla.hellrock.uid);
					} else if (!b) {
						if ((y << 5) + cy < -5975) {
							out.setBlockId(cx, cy, cz, Vanilla.lava.uid);
						}
					} else if(diff < 0 && (y << 5) + cy < 0){
						out.setBlockId(cx, cy, cz, Vanilla.water.uid);
					} else if(diff == -1){
						bio.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
					}
					++diff;
				}
			}
		}
		if (world.getGeneratorTag().getBoolean("chasms", true)) chasmGen.generate(world, data, x, y, z, out);
		if(world.getGeneratorTag().getBoolean("volcanos", true)) volcanoGen.generate(world, x, y, z, out);
		if (world.getGeneratorTag().getBoolean("caves", true)) caveGen.generate(world, x, y, z, out);
		return out;
	}

	/**
	 * @param world
	 */
	private SimplexOctaveGenerator initHellNoise(World world) {
		SimplexNoiseGenerator[] octaves = new SimplexNoiseGenerator[2];
		Random rand = new Random(world.getSeed());
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
	 * @see vc4.api.generator.WorldGenerator#populate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public void populate(World world, long x, long y, long z) {
		if(y < -3 || !world.getBiome((x << 5) + 16, (z << 5) + 16).getType().equals(BiomeType.ocean)){
			waterLakeGen.populate(world, x, y, z);
			lavaLakeGen.populate(world, x, y, z);
		}
		if (world.getGeneratorTag().getBoolean("dungeons", true)) {
			dungeonGen.generate(world, x, y, z);
			ruinsGen.populate(world, x, y, z);
			pyramidsGen.populate(world, x, y, z);
			dungeonsGen.populate(world, x, y, z);
		}
		if(world.getGeneratorTag().getBoolean("villages", true)){
			villageGen.generate(world, x, y, z);
		}
		if (world.getGeneratorTag().getBoolean("ores", true)) oresGen.populate(world, x, y, z);
		if (y > 30) {
			floatingGen.populate(world, x, y, z);
			TreeGenBasic oakGen = new TreeGenBasic(world, new Random(world.getSeed() ^ 71927L ^ x ^ 139013L ^ y ^ 1038794L ^ z));
			for (int d = 0; d < 100; ++d) {
				oakGen.generate((x << 5) + oakGen.rand.nextInt(32), (y << 5) + oakGen.rand.nextInt(32), (z << 5) + oakGen.rand.nextInt(32), Vanilla.plantTreeOak);
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
			if (pos.y > -5200) gl.color(0.6f, 0.45f, 0.45f, 1);
			else gl.color(0.8f, 0.3f, 0.3f, 1);
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

	@Override
	public void generateMapData(World world, MapData data) {
		long wx = data.getPosition().x << 5;
		long wz = data.getPosition().y << 5;
		int op = 0;
		ZoomGenerator bgen = new BiomeGenIslands(world);
		bgen = new BiomeGenZoom(world, bgen, false);
		bgen = new BiomeGenIslands(world, bgen);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSuperBiome(world, bgen);
		bgen = new BiomeGenZoom(world, bgen, false);
		bgen = new BiomeGenBiome(world, bgen, biomes);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSubBiome(world, bgen, op++);
		bgen = new BiomeGenZoom(world, bgen, true);
		ZoomGenerator hgen = new HeightGenSeed(world);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //0
		bgen = new BiomeGenSubBiome(world, bgen, op++);
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new HeightGenZoom(world, hgen);
		float rVal = 1f;
		float rMod = 0.5f;
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //1
		bgen = new BiomeGenSubBiome(world, bgen, op++);
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new BiomeGenZoom(world, hgen);
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //2
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new HeightGenZoom(world, hgen);
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //3
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new HeightGenZoom(world, hgen);
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //4
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new HeightGenZoom(world, hgen);
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //5
		bgen = new BiomeGenZoom(world, bgen, true);
		hgen = new HeightGenZoom(world, hgen);
		hgen = new HeightGenDisplace(world, hgen, rVal *= rMod);
		((BiomeGenZoom)bgen).setSeeding((HeightGenBiomeInput) hgen); //6
		hgen = new HeightGenZoom(world, hgen);;
		hgen = new BiomeGenZoom(world, hgen);
		bgen = new BiomeGenZoom(world, bgen, true); //7
		bgen = new BiomeGenZoom(world, bgen, true); //8
		
		int[] intBiomes = bgen.generate(wx, wz, 32);
		byte[] biomes = new byte[32 * 32];
		for(int d = 0; d < 1024; ++d) biomes[d] = (byte) intBiomes[d];
		data.setBiomeMap(biomes);
		data.setHeightMap(hgen.generate(wx, wz, 32));
		
	}
	

}
