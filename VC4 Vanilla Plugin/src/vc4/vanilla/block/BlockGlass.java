/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.util.Direction;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class BlockGlass extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockGlass(int uid, int texture, Material m) {
		super(uid, texture, m);
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#isSolid(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getRendererToUse(byte, int)
	 */
	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#renderSide(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return super.renderSide(world, x, y, z, side) && world.getNearbyBlockId(x, y, z, Direction.getDirection(side)) != uid;
	}

}
