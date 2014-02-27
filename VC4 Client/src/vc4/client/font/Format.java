/**
 * 
 */
package vc4.client.font;

/**
 * @author paul
 * 
 */
public class Format<T> {

	/**
	 * 
	 */
	public Format() {
	}

	public Format(T object) {
		super();
		this.object = object;
	}

	private T object;
	private boolean changed;

	/**
	 * @return the object
	 */
	public T get() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void set(T object) {
		this.object = object;
		changed = true;
	}

	public void setSilently(T object) {
		this.object = object;
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

}
