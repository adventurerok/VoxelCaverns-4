package vc4.api.biome;

public class BiomeType {

	int id;
	String name;

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BiomeType other = (BiomeType) obj;
		if (id != other.id) return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	static int lastId;

	public static int getNumberOfTypes() {
		return lastId;
	}

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
