/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;
import vc4.vanilla.util.WoodBlocks;

/**
 * @author paul
 *
 */
public class BlockLog extends Block {

	private int rot;
	
	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockLog(short uid, Material m, int rot) {
		super(uid, 0, m);
		this.rot = rot;
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public ItemStack[] getCreativeItems() {
		if(uid != Vanilla.logV.uid) return new ItemStack[0];
		return WoodBlocks.genCreativeItems(uid);
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "log." + WoodBlocks.getName(item.getDamage() & 15);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		if(rot == 0 && side < 4) return sideTex(data);
		else if(rot > 0 && (side > 3) || (side % 2 == (rot - 1))) return sideTex(data);
		else return baseTex(data);
	}
	
	private int baseTex(int data) {
		switch(data){
			case 0:
				return BlockTexture.oakBase;
			case 1:
				return BlockTexture.birchBase;
			case 2:
				return BlockTexture.willowBase;
			case 3:
				return BlockTexture.ashBase;
			case 4:
				return BlockTexture.chestnutBase;
			case 5:
				return BlockTexture.redwoodBase;
			case 6:
				return BlockTexture.kapokBase;
		}
		return BlockTexture.oakBase;
	}

	private int sideTex(int data) {
		switch(data){
			case 0:
				return BlockTexture.oakWood;
			case 1:
				return BlockTexture.birchWood;
			case 2:
				return BlockTexture.willowWood;
			case 3:
				return BlockTexture.ashWood;
			case 4:
				return BlockTexture.chestnutWood;
			case 5:
				return BlockTexture.redwoodWood;
			case 6:
				return BlockTexture.kapokWood;
		}
		return BlockTexture.oakWood;
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		int data = item.getDamage() & 15;
		if(rot == 0 && side < 4) return sideTex(data);
		else if(rot > 0 && (side > 3) || (side % 2 == (rot - 1))) return sideTex(data);
		else return baseTex(data);
	}
	
	

}
