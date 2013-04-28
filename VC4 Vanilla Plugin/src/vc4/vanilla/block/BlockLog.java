/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
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
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		if(rot == 0 && side < 4) return 128 + data;
		else if(rot > 0 && (side > 3) || (side % 2 == (rot - 1))) return 128 + data;
		else return 144 + data;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		int data = item.getDamage() & 15;
		if(rot == 0 && side < 4) return 128 + data;
		else if(rot > 0 && (side > 3) || (side % 2 == (rot - 1))) return 128 + data;
		else return 144 + data;
	}
	
	

}
