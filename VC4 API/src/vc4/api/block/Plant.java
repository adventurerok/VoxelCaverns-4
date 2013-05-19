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
	private boolean checkSubtype = true;
	
	public void setCheckSubtype(boolean checkSubtype) {
		this.checkSubtype = checkSubtype;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(checkSubtype) result = prime * result + subId;
		result = prime * result + uid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Plant other = (Plant) obj;
		if (checkSubtype && other.checkSubtype && subId != other.subId) return false;
		if (uid != other.uid) return false;
		return true;
	}
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
