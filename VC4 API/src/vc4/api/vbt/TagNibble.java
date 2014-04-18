package vc4.api.vbt;

public class TagNibble extends Tag {

	private byte value;

	public TagNibble(String name) {
		super(name);
	}

	public TagNibble(String name, byte value) {
		super(name);
		this.value = value;
	}

	@Override
	public Byte getValue() {
		return (byte) (value & 0xF);
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Number) value = ((Number) o).byteValue();
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Nibble" + append + ": " + (value & 0xf);
	}

}
