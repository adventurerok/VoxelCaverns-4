package vc4.api.world;

import vc4.api.biome.Biome;
import vc4.api.vector.Vector2l;

public interface MapData {

	public Biome getBiome(int cx, int cz);
	public int getGenHeight(int cx, int cz);
	public int[] getGenHeightMap();
	public void setGenHeightMap(int[] hm);
	public byte[] getBiomeMap();
	public void setBiomeMap(byte[] bm);
	public Vector2l getPosition();
	public void setHeightMap(int[] heightMap);
	public int[] getHeightMap();
	public void setHeight(int cx, int cz, int h);
	public int getHeight(int cx, int cz);
}
