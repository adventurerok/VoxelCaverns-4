package vc4.api.tool;

public class ToolMaterial {

	private int id;
	private int durability; // Max durability
	private String name;
	private double power; // How fast it breaks things, and what it can break

	private static int nextId;

	public ToolMaterial(int id, String name, int durability, double power) {
		super();
		this.id = id;
		this.name = name;
		this.durability = durability;
		this.power = power;
	}

	public ToolMaterial(String name, int durability, double power) {
		super();
		this.id = nextId++;
		this.name = name;
		this.durability = durability;
		this.power = power;
	}

	public int getId() {
		return id;
	}

	public int getDurability() {
		return durability;
	}

	public String getName() {
		return name;
	}

	public double getPower() {
		return power;
	}

}
