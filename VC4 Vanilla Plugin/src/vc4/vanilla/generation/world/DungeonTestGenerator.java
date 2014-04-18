package vc4.vanilla.generation.world;

import java.util.ArrayList;
import java.util.Arrays;

import vc4.api.biome.ZoomGenerator;
import vc4.api.block.Block;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.gui.MapIcon;
import vc4.api.sound.Music;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3f;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.room.DungeonRoomBase;

public class DungeonTestGenerator implements WorldGenerator {
	
	private DungeonRoomBase dungeonGen = new DungeonRoomBase();

	@Override
	public void onWorldLoad(World world) {
	}

	@Override
	public GeneratorOutput generate(World world, long x, long y, long z, MapData map) {
		GeneratorOutput out = new GeneratorOutput();
		if(y < 0){
			Arrays.fill(out.blocks, Block.stone.uid);
		}
		return out;
	}

	@Override
	public void populate(World world, long x, long y, long z) {
		if(x % 5 != 0) return;
		if(z % 5 != 0) return;
		if(y == 1 || y == -10){
			dungeonGen.generate(world, x, y, z, true);
		}
	}

	@Override
	public Vector3d getSpawnPoint(World world) {
		return new Vector3d(0, 5, 0);
	}

	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		return Vanilla.musicHell;
	}

	@Override
	public void renderSkyBox(World world, EntityPlayer player) {
	}

	@Override
	public void generateMapData(World world, MapData data) {
		data.setBiomeMap(new short[1024]);
		data.setHeightMap(new int[1024]);
		data.setGenHeightMap(new int[1024]);
	}

	@Override
	public boolean generatePlants(World world, long x, long y, long z) {
		return true;
	}

	@Override
	public boolean hasBiomeMapGenerator(World world) {
		return false;
	}

	@Override
	public ZoomGenerator getBiomeMapGenerator(World world, int zoom) {
		return null;
	}

	@Override
	public ArrayList<MapIcon> getMapIcons(World world, long x, long z, int size) {
		return null;
	}

	@Override
	public Vector3f getLightColor(World world, MapData m, long x, long y, long z, int cx, int cz, int level) {
		return OverworldGenerator.lightColors[level];
	}

}
