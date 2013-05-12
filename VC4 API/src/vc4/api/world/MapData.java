package vc4.api.world;

import vc4.api.biome.Biome;
import vc4.api.vector.Vector2l;

public interface MapData {

	public Biome getBiome(int cx, int cz);
	public int getHeight(int cx, int cz);
	public int[] getHeightMap();
	public void setHeightMap(int[] hm);
	public byte[] getBiomeMap();
	public void setBiomeMap(byte[] bm);
	public Vector2l getPosition();
	public void addReference();
	public void removeReference();
	public int getReferences();
}
