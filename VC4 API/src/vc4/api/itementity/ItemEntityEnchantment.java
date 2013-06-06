package vc4.api.itementity;

import java.util.ArrayList;

import org.jnbt.*;

import vc4.api.world.World;

public class ItemEntityEnchantment extends ItemEntity {

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((enchantments == null) ? 0 : enchantments.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntityEnchantment other = (ItemEntityEnchantment) obj;
		if (enchantments == null) {
			if (other.enchantments != null)
				return false;
		} else if (!enchantments.equals(other.enchantments))
			return false;
		return true;
	}

	public ArrayList<Short> enchantments = new ArrayList<Short>();
	
	
	@Override
	public CompoundTag getSaveCompound(World world) {
		CompoundTag tag = super.getSaveCompound(world);
		ListTag lis = new ListTag("ench", ShortTag.class);
		for(int d = 0; d < enchantments.size(); ++d) lis.addTag(new ShortTag("val", enchantments.get(d)));
		tag.addTag(lis);
		return tag;
	}

	
	@Override
	public void loadSaveCompound(World world, CompoundTag tag) {
		super.loadSaveCompound(world, tag);
		ListTag lis = tag.getListTag("ench");
		enchantments.clear();
		while(lis.hasNext()){
			enchantments.add(((ShortTag)lis.getNextTag()).getValue());
		}
	}
	
	@Override
	public boolean canCombine(ItemEntity entity) {
		return equals(entity);
	}
	
	public void addEnchantment(int type, int level){
		for(int dofor = enchantments.size() - 1; dofor > -1; --dofor){
			if((enchantments.get(dofor)  & 0xFFF) == type){
				level = Math.max(level, (enchantments.get(dofor) >> 12) & 0xF);
				enchantments.remove(dofor);
			}
		}
		short ench = (short) ((level << 12) + type);
		enchantments.add(ench);
	}



	@SuppressWarnings("unchecked")
	@Override
	public ItemEntity clone() {
		ItemEntityEnchantment e = new ItemEntityEnchantment();
		e.enchantments = (ArrayList<Short>) enchantments.clone();
		return e;
	}

	@Override
	public String getName() {
		return "enchantment";
	}

}
