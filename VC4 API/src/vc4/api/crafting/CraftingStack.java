/**
 * 
 */
package vc4.api.crafting;

import vc4.api.item.ItemStack;

/**
 * @author paul
 *
 */
public class CraftingStack extends ItemStack {
	
	private boolean anyData = false;
	private boolean isOutputData = false;

	public CraftingStack() {
		super();
		
	}

	public CraftingStack(int id, int damage, int amount) {
		super(id, damage, amount);
		
	}

	public CraftingStack(int id, int damage) {
		super(id, damage);
		
	}

	public CraftingStack(int id, short amount) {
		super(id, amount);
		
	}

	public CraftingStack(int id) {
		super(id);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6576046463037042142L;
	
	/**
	 * @return the anyData
	 */
	public boolean isAnyData() {
		return anyData;
	}
	
	/**
	 * @return the isOutputData
	 */
	public boolean isOutputData() {
		return isOutputData;
	}
	
	/**
	 * @param anyData the anyData to set
	 */
	public CraftingStack setAnyData(boolean anyData) {
		this.anyData = anyData;
		return this;
	}
	
	/**
	 * @param isOutputData the isOutputData to set
	 */
	public CraftingStack setOutputData(boolean isOutputData) {
		this.isOutputData = isOutputData;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see game.vc3d.item.ItemStack#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemStack))
			return false;
		ItemStack other = (ItemStack) obj;
		if (!anyData && getDamage() != other.getDamage())
			return false;
		if (getId() != other.getId())
			return false;
		if(entities.size() != other.entities.size()) return false;
		for(int dofor = 0; dofor < entities.size(); ++dofor){
			if(!entities.get(dofor).equals(other.entities.get(dofor))) return false;
		}
		return true;
	}

}
