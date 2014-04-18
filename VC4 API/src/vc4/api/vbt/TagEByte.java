package vc4.api.vbt;

public class TagEByte extends Tag {

	private short value;

	public TagEByte(String name) {
		super(name);
	}

	public TagEByte(String name, short value) {
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
