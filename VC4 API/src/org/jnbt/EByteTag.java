package org.jnbt;

public class EByteTag extends Tag {

	private short value;

	public EByteTag(String name) {
		super(name);
	}

	public EByteTag(String name, short value) {
		super(name);
		this.value = value;
	}

	@Override
	public Short getValue() {
		return (short) (value & 0xFFF);
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Number) value = ((Number) o).shortValue();
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_EByte" + append + ": " + (value & 0xfff);
	}

}
