/**
 * 
 */
package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.BlockFluid;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

/**
 * @author paul
 *
 */
public class BlockWater extends BlockFluid {

	public static Color water = new Color(0, 156, 254, 128);
	
	/**
	 * @param uid
	 * @param texture
	 * @param material
	 */
	public BlockWater(int uid) {
		super(uid, BlockTexture.fluid, "water");
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getColor(vc4.api.item.ItemStack, int)
	 */
	@Override
	public Color getColor(ItemStack current, int side) {
		return water;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getColor(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return water;
	}

}
