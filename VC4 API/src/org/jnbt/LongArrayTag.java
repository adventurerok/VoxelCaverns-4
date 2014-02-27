/**
 * 
 */
package org.jnbt;

/**
 * @author paul
 * 
 */
public class LongArrayTag extends Tag {

	private long[] value;

	public LongArrayTag(String name) {
		super(name);
	}

	/**
	 * @param name
	 */
	public LongArrayTag(String name, long[] value) {
		super(name);
		this.value = value;

	}

	@Override
	public long[] getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof long[]) value = (long[]) o;
	}

	@Override
	public String toString() {
		StringBuilder hex = new StringBuilder();
		for (long b : value) {
			String hexDigits = Long.toHexString(b).toUpperCase();
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
		return "TAG_Long_Array" + append + ": " + hex.toString();
	}

}
