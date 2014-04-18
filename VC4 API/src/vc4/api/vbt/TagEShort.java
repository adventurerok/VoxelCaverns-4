package vc4.api.vbt;

public class TagEShort extends Tag {

	private int value;

	public TagEShort(String name) {
		super(name);
	}

	public TagEShort(String name, int value) {
		super(name);
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value & 0xFFFFFF;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Number) value = ((Number) o).intValue();
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_EShort" + append + ": " + (value & 0xffffff);
	}

}
