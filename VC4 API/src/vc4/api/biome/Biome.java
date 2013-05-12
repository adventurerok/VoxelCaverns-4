package vc4.api.biome;

import java.awt.Color;

public class Biome {

	private static Biome[] biomesList = new Biome[256];
	
	public BiomeType type;
	public Color mapColor;
	public int id;
	public String name;
	public int topBlock;
	public int fillerBlock;
	public int bottomBlock;
	public int minHeight = 3;
	public int maxHeight = 30;
	
	private static int nextId;
	
	public static Biome byId(int id){
		return biomesList[id];
	}
	
	public Biome setBiomeBlocks(int top, int filler, int bottom){
		topBlock = top;
		fillerBlock = filler;
		bottomBlock = bottom;
		return this;
	}

	public Biome(String name, BiomeType type, Color mapColor) {
		super();
		this.type = type;
		this.mapColor = mapColor;
		this.name = name;
		this.id = nextId++;
		biomesList[id] = this;
	}
	
	public static Biome ocean = new Biome("ocean", BiomeType.ocean, Color.blue);
	public static Biome plains = new Biome("plains", BiomeType.normal, Color.green);
	public static Biome desert = new Biome("desert", BiomeType.hot, Color.yellow);
	public static Biome snowPlains = new Biome("snowplains", BiomeType.cold, Color.white);
	
	
}
