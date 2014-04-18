package vc4.api.vbt;

public class TagEInt extends Tag {

	private long value;

	public TagEInt(String name) {
		super(name);
	}

	public TagEInt(String name, long value) {
		super(name);
		this.value = value;
	}

	@Override
	public Long getValue() {
		return value & 0xFFFFFFFFFFFFL;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Number) value = ((Number) o).longValue();
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_EInt" + append + ": " + (value & 0xffffffffffffL);
	}

}
