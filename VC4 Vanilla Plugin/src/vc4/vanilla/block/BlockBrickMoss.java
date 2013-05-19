/**
 * 
 */
package vc4.vanilla.block;

import java.awt.Color;
import java.util.Random;

import vc4.api.block.IBlockMultitexture;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

/**
 * @author paul
 *
 */
public class BlockBrickMoss extends BlockBrick implements IBlockMultitexture{

	/**
	 * @param uid
	 */
	public BlockBrickMoss(short uid) {
		super(uid);
		
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "mossy" + super.getModifiedName(item);
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#renderSideMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		return renderSide(world, x, y, z, side);
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#getColorMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return Color.white;
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#getTextureIndexMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return BlockTexture.moss;
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#getColorMultitexture(vc4.api.item.ItemStack, int)
	 */
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return Color.white;
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#getTextureIndexMultitexture(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return BlockTexture.moss;
	}
	

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#multitextureUsed(byte, int)
	 */
	@Override
	public boolean multitextureUsed(byte data, int side) {
		return true;
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z) {
		if(rand.nextInt(5) != 0) return 0;
		Direction dir = Direction.getDirection(rand.nextInt(10));
		if(world.getNearbyBlockId(x, y, z, dir) == Vanilla.brick.uid){
			byte data = world.getNearbyBlockData(x, y, z, dir);
			world.setNearbyBlockIdData(x, y, z, uid, data, dir);
		}
		return 0;
	}

}
