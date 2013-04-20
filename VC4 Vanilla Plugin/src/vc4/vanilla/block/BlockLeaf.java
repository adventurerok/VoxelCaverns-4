/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class BlockLeaf extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockLeaf(short uid, Material m) {
		super(uid, 0, m);
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getRendererToUse(byte, int)
	 */
	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public ItemStack[] getCreativeItems() {
		ItemStack[] result = new ItemStack[7];
		for(int d = 0; d < 7; ++d){
			result[d] = new ItemStack(uid, d, 1);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		return 112 + data;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		return 112 + (item.getDamage() & 15);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#isSolid(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

}
