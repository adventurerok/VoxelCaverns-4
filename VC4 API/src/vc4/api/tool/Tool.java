package vc4.api.tool;

public class Tool {

	private ToolType type;
	private ToolMaterial material;

	public Tool(ToolType type, ToolMaterial material) {
		super();
		this.type = type;
		this.material = material;
	}

	public ToolType getType() {
		return type;
	}

	public ToolMaterial getMaterial() {
		return material;
	}

	public int getDurability() {
		return material.getDurability();
	}

	public double getPower() {
		return material.getPower();
	}
}
