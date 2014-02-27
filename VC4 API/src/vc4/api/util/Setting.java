/**
 * 
 */
package vc4.api.util;

/**
 * @author paul
 * 
 */
public class Setting<T> {

	public Setting(T object) {
		super();
		this.object = object;
	}

	/**
	 * 
	 */
	public Setting() {
	}

	private boolean changed;
	private T object;

	/**
	 * @param object
	 *            the object to set
	 */
	public void set(T object) {
		this.object = object;
	}

	/**
	 * @return the object
	 */
	public T get() {
		return object;
	}

	public boolean hasChanged() {
		boolean b = changed;
		changed = false;
		return b;
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return object.toString();
	}

	public boolean isNumber() {
		return object instanceof Number;
	}

	public double getDouble() {
		return ((Number) object).doubleValue();
	}

	public int getInt() {
		return ((Number) object).intValue();
	}

	public String getString() {
		return object.toString();
	}

}
