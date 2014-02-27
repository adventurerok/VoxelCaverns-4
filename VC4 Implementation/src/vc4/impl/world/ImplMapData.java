package vc4.impl.world;

import vc4.api.biome.Biome;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3d;
import vc4.api.world.MapData;

public class ImplMapData implements MapData {

	Vector2l pos;

	volatile short[] biomes;
	volatile int[] genMap;
	volatile int[] heightMap;
	volatile boolean[] heightUpdates;

	public ImplMapData(Vector2l pos) {
		super();
		this.pos = pos;
	}

	@Override
	public Biome getBiome(int cx, int cz) {
		return biomes == null ? null : Biome.byId(biomes[cz * 32 + cx]);
	}

	@Override
	public int getGenHeight(int cx, int cz) {
		return genMap == null ? 0 : genMap[cz * 32 + cx];
	}

	@Override
	public int getHeight(int cx, int cz) {
		return heightMap == null ? 0 : heightMap[cz * 32 + cx];
	}

	@Override
	public void setHeight(int cx, int cz, int h) {
		if (heightMap != null) heightMap[cz * 32 + cx] = h;
	}

	@Override
	public int[] getGenHeightMap() {
		return genMap;
	}

	@Override
	public void setGenHeightMap(int[] hm) {
		genMap = hm;

	}

	@Override
	public int[] getHeightMap() {
		return heightMap;
	}

	@Override
	public void setHeightMap(int[] heightMap) {
		this.heightMap = heightMap;
		return;
	}

	@Override
	public short[] getBiomeMap() {
		return biomes;
	}

	@Override
	public void setBiomeMap(short[] bm) {
		biomes = bm;
	}

	@Override
	public Vector2l getPosition() {
		return pos;
	}

	public double distanceSquared(Vector3d loc) {
		double x = (pos.x << 5) + 16 - loc.x;
		double z = (pos.y << 5) + 16 - loc.z;
		return x * x + z * z;
	}

}
