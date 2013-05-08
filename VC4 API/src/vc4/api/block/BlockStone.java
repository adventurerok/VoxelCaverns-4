/**
 * 
 */
package vc4.api.block;

import vc4.api.Resources;
import vc4.api.item.ItemStack;
import vc4.api.world.World;



/**
 * @author paul
 *
 */
public class BlockStone extends Block{

	
	int tIndex;
	
	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockStone(short uid, Material m) {
		super(uid, 0, m);
		
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if(tIndex == 0) tIndex = Resources.getAnimatedTexture("blocks").getArrayIndex("stone");
		return tIndex;
	}
	
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if(tIndex == 0) tIndex = Resources.getAnimatedTexture("blocks").getArrayIndex("stone");
		return tIndex;
	}

	

}
