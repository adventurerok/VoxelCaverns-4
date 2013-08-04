package vc4.api.biome;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.PlantGrowth;
import vc4.api.sound.Music;
import vc4.api.sound.MusicType;

public class Biome {

	private static Biome[] biomesList = new Biome[256];
	
	public BiomeType type;
	public Color mapColor;
	public int id;
	public String name;
	public int icingBlock;
	public int topBlock;
	public int fillerBlock;
	public int bottomBlock;
	public int minHeight = 0;
	public int maxHeight = 26;
	public int minEnf = 0;
	public int maxEnf = 30;
	public int soilDepth = 6;
	public int diffHeight = 26, midHeight = 13;
	public boolean enfHeight = true; //If the height should be clamped
	public Music music = new Music("First_Day", MusicType.BIOME);
	public Color grassColor = new Color(0x1C8F1C);
	public Color waterColor = new Color(0, 156, 254, 128);
	public Color plantColor = new Color(0x269100);
	
	public Color dayColor = new Color(0xADD8E6);
    public Color dawnColor = new Color(255, 74, 0);
    public Color nightColor = new Color(0, 0, 75);
	
	public ArrayList<PlantGrowth> plants = new ArrayList<>();
	
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
	
	public int generateSubBiome(Random rand, int op){
		return id;
	}
	
	public Biome setHeights(BiomeHeightModel model){
		setHeights(model.max, model.min);
		maxEnf = model.eMax;
		minEnf = model.eMin;
		return this;
	}
	
	public Biome setColors(Color grass, Color water, Color plant){
		waterColor = water;
		grassColor = grass;
		plantColor = plant;
		return this;
	}
	
	public void setIcingBlock(int icingBlock) {
		this.icingBlock = icingBlock;
	}
	
	public void placeBiomeBlock(GeneratorOutput out, Random rand, int cx, int cy, int cz, long diff, long y){
		if(diff == -1){
			out.setBlockId(cx, cy, cz, icingBlock);
		} else if (diff == 0){
			if((y << 5) + cy < -1) out.setBlockId(cx, cy, cz, fillerBlock);
			else out.setBlockId(cx, cy, cz, topBlock);
		}
		else if(diff < 5) out.setBlockId(cx, cy, cz, fillerBlock);
		else out.setBlockId(cx, cy, cz, bottomBlock);
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

	public Biome(int id, String name, BiomeType type, Color mapColor) {
		super();
		this.type = type;
		this.mapColor = mapColor;
		this.name = name;
		this.id = id;
		biomesList[id] = this;
	}
	
	private Biome(){
		
	}
	
	public static Biome createFake(){
		return new Biome();
	}
	
	public ArrayList<PlantGrowth> getPlants() {
		return plants;
	}
	
	public void addPlant(PlantGrowth plant){
		plants.add(plant);
	}
	

	public static void clear() {
		for(Biome b : biomesList){
			if(b == null) continue;
			b.plants.clear();
		}
	}
	
	
}
