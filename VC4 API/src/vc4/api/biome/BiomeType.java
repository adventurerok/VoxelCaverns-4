package vc4.api.biome;


public class BiomeType {

	
	int id;
	String name;
	
	static int lastId;

	public BiomeType(String name) {
		super();
		this.name = name;
		this.id = lastId++;
	}
	
	public static BiomeType ocean = new BiomeType("ocean");
	public static BiomeType normal = new BiomeType("normal");
	public static BiomeType cold = new BiomeType("cold");
	public static BiomeType hot = new BiomeType("hot");
	
	
}
