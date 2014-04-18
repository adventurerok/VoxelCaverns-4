package vc4.api.vbt;

public class TagBoolean extends Tag {

	private boolean value;

	public TagBoolean(String name) {
		super(name);
	}

	public TagBoolean(String name, boolean value) {
		super(name);
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Boolean) {
			value = (boolean) o;
		}

	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Boolean" + append + ": " + value;
	}

}
