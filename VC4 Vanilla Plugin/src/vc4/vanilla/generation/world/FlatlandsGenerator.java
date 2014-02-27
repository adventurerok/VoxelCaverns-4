package vc4.vanilla.generation.world;

import java.util.ArrayList;
import java.util.Arrays;

import vc4.api.biome.ZoomGenerator;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.graphics.*;
import vc4.api.gui.MapIcon;
import vc4.api.sound.Music;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3f;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class FlatlandsGenerator implements WorldGenerator {

	@Override
	public void onWorldLoad(World world) {

	}

	@Override
	public GeneratorOutput generate(World world, long x, long y, long z, MapData data) {
		GeneratorOutput out = new GeneratorOutput();
		int woolType = (int) (((x & 3) << 4) + ((y & 3) << 2) + ((z & 3)));
		short bid = woolType < 32 ? Vanilla.wool0.uid : Vanilla.wool1.uid;
		byte dat = (byte) (woolType & 31);
		if (y < 0) {
			Arrays.fill(out.blocks, bid);
			Arrays.fill(out.data, dat);
		}
		return out;
	}

	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		return Vanilla.musicOverworld;
	}

	@Override
	public void populate(World world, long x, long y, long z) {

	}

	@Override
	public Vector3d getSpawnPoint(World world) {
		return new Vector3d(0, 1, 0);
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
		data.setBiomeMap(new short[32 * 32]);
		data.setHeightMap(new int[32 * 32]);
		data.setGenHeightMap(new int[32 * 32]);
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
		return new Vector3f(1, 1, 1);
	}

}
