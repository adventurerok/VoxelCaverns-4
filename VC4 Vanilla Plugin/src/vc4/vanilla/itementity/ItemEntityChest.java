package vc4.vanilla.itementity;

import java.awt.Color;
import java.io.IOException;

import org.jnbt.CompoundTag;

import vc4.api.itementity.ItemEntity;
import vc4.vanilla.block.BlockPlanks;

public class ItemEntityChest extends ItemEntity {

	
	private static Color gold = new Color(255, 226, 102);
	private static Color adamantite = new Color(60, 93, 60);
	public byte type = 0, subtype = 0;
	
	public ItemEntityChest() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemEntityChest(byte type, byte subtype) {
		super();
		this.type = type;
		this.subtype = subtype;
	}

	
	public String getTypeName(){
		switch(type){
		case 0:
			return "wood";
		case 1:
			return "gold";
		case 2:
			return "adamantite";
		}
		return "empty";
	}
	
	public Color getColor(){
		switch(type){
		case 0:
			return BlockPlanks.backColors[subtype];
		case 1:
			return gold;
		case 2:
			return adamantite;
		}
		
		return Color.white;
	}

	@Override
	public short getId() {
		return 1;
	}

	@Override
	public ItemEntity clone() {
		return new ItemEntityChest(type, subtype);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + subtype;
		result = prime * result + type;
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
		ItemEntityChest other = (ItemEntityChest) obj;
		if (subtype != other.subtype)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public boolean canCombine(ItemEntity entity) {
		return equals(entity);
	}

	@Override
	public void writeAdditionalData(CompoundTag tag) throws IOException {
		tag.setNibble("type", type);
		tag.setNibble("sub", subtype);
		
	}

	@Override
	public void readAdditionalData(CompoundTag tag) throws IOException {
		type = tag.getNibble("type");
		subtype = tag.getNibble("sub");
		
	}
	
	

}
