package org.jnbt;

public class EShortTag extends Tag {

	private int value;

	public EShortTag(String name) {
		super(name);
	}

	public EShortTag(String name, int value) {
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
