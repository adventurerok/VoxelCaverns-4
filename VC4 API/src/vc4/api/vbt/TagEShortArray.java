/**
 * 
 */
package vc4.api.vbt;

/**
 * @author paul
 * 
 */
public class TagEShortArray extends Tag {

	private int[] value;

	public TagEShortArray(String name) {
		super(name);
		// TASK Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public TagEShortArray(String name, int[] value) {
		super(name);
		this.value = value;

	}

	@Override
	public int[] getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof int[]) value = (int[]) o;
	}

	@Override
	public String toString() {
		StringBuilder hex = new StringBuilder();
		for (int b : value) {
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
		return "TAG_Eshort_Array" + append + ": " + hex.toString();
	}

}
