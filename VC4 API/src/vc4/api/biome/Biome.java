package vc4.api.biome;

import java.awt.Color;

import vc4.api.sound.Music;
import vc4.api.sound.MusicType;

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
	public int diffHeight = 27, midHeight = 16;
	public Music music = new Music("First_Day", MusicType.BIOME);
	
	public BiomeType getType() {
		return type;
	}

	public Color getMapColor() {
		return mapColor;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public Music getMusic() {
		return music;
	}

	private static int nextId;
	
	public static Biome byId(int id){
		return biomesList[id];
	}
	
	public Biome setHeights(int max, int min){
		minHeight = min;
		maxHeight = max;
		diffHeight = max - min;
		midHeight = (max + min) / 2;
		return this;
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
	
	public static Biome ocean = new Biome("ocean", BiomeType.ocean, Color.blue).setHeights(2, -40);
	public static Biome plains = new Biome("plains", BiomeType.normal, Color.green);
	public static Biome desert = new Biome("desert", BiomeType.hot, Color.yellow);
	public static Biome snowPlains = new Biome("snowplains", BiomeType.cold, Color.white);
	
	
}
