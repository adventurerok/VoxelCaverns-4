package vc4.impl.world;

import vc4.api.biome.Biome;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3d;
import vc4.api.world.MapData;

public class ImplMapData implements MapData{

	Vector2l pos;
	
	volatile byte[] biomes;
	volatile int[] heightMap;
	volatile int reference;
	
	
	
	public ImplMapData(Vector2l pos) {
		super();
		this.pos = pos;
	}
	@Override
	public Biome getBiome(int cx, int cz) {
		return biomes == null ? null : Biome.byId(biomes[cz * 32 + cx]);
	}
	@Override
	public int getHeight(int cx, int cz) {
		return heightMap == null ? 0 : heightMap[cz * 32 + cx];
	}
	@Override
	public int[] getHeightMap() {
		return heightMap;
	}
	@Override
	public void setHeightMap(int[] hm) {
		heightMap = hm;
		
	}
	@Override
	public byte[] getBiomeMap() {
		return biomes;
	}
	@Override
	public void setBiomeMap(byte[] bm) {
		biomes = bm;
	}
	@Override
	public Vector2l getPosition() {
		return pos;
	}
	@Override
	public void addReference() {
		reference++;
	}
	@Override
	public void removeReference() {
		if(reference > 0) reference--;
	}
	@Override
	public int getReferences() {
		return reference;
	}
	
	public double distanceSquared(Vector3d loc){
		double x = (pos.x << 5) + 16 - loc.x;
		double z = (pos.y << 5) + 16 - loc.z;
		return x*x + z*z;
	}
	
	
	
	
}
