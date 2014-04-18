/**
 * 
 */
package vc4.api.vbt;

/**
 * @author paul
 * 
 */
public class TagShortArray extends Tag {

	private short[] value;

	public TagShortArray(String name) {
		super(name);
	}

	/**
	 * @param name
	 */
	public TagShortArray(String name, short[] value) {
		super(name);
		this.value = value;

	}

	@Override
	public short[] getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof short[]) value = (short[]) o;
	}

	@Override
	public String toString() {
		StringBuilder hex = new StringBuilder();
		for (short b : value) {
			String hexDigits = Integer.toHexString(b).toUpperCase();
			if (hexDigits.length() == 1) {
				hex.append("0");
			}
			hex.append(hexDigits).append(" ");
		}
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Short_Array" + append + ": " + hex.toString();
	}

}
