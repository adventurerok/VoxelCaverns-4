package vc4.api.tool;

public class ToolType {

	private int id;
	private String name;
	
	private static int nextId = 0;
	
	public static final ToolType pickaxe = new ToolType("pick");
	public static final ToolType axe = new ToolType("hatchet");
	public static final ToolType spade = new ToolType("shovel");
	public static final ToolType hoe = new ToolType("hoe");
	
	public ToolType(String name) {
		super();
		this.id = nextId++;
		this.name = name;
	}

	public ToolType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ToolType other = (ToolType) obj;
		if (id != other.id) return false;
		return true;
	}
	
}
