package vc4.vanilla.underbiome;

import java.awt.Color;

import vc4.api.sound.Music;

public class UnderBiome {

	private static UnderBiome[] biomesList = new UnderBiome[256];

	public Color mapColor;
	public int id;
	public int stoneBlockId;
	public byte stoneBlockData;
	public int cavesChance = 115;
	public int cavesAmount = 7;
	public String name;
	public Music music = null;

	public static UnderBiome[] getBiomesList() {
		return biomesList.clone();
	}

	public UnderBiome(int id, String name, Color mapColor) {
		super();
		this.mapColor = mapColor;
		this.name = name;
		this.id = id;
		biomesList[id] = this;
	}

	public Color getMapColor() {
		return mapColor;
	}

	public UnderBiome setStoneBlock(int stoneBlockId, int data) {
		this.stoneBlockId = stoneBlockId;
		this.stoneBlockData = (byte) data;
		return this;
	}

	public int getId() {
		return id;
	}

	public UnderBiome setCaveData(int chance, int amount) {
		if (chance < 55) chance = 55;
		this.cavesChance = chance;
		this.cavesAmount = amount;
		return this;
	}

	public String getName() {
		return name;
	}

	public Music getMusic() {
		return music;
	}

	public static UnderBiome byId(int id) {
		return biomesList[id];
	}

	private UnderBiome() {

	}

	public static UnderBiome createFake() {
		return new UnderBiome();
	}

}
