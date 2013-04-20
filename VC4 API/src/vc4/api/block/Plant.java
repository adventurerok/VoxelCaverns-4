/**
 * 
 */
package vc4.api.block;

/**
 * @author paul
 *
 */
public class Plant {

	private int uid, subId;
	private String name;
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @return the subId
	 */
	public int getSubId() {
		return subId;
	}
	public Plant(int uid, int subId, String name) {
		super();
		this.uid = uid;
		this.subId = subId;
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
