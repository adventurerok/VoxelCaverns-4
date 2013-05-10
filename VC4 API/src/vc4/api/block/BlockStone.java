/**
 * 
 */
package vc4.api.block;

import vc4.api.Resources;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.tool.ToolType;
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
		setMineData(new MiningData().setRequired(ToolType.pickaxe).setPowers(1, 1, 25).setTimes(5, 0.1, 1));
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
