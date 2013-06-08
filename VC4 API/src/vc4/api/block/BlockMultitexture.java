/**
 * 
 */
package vc4.api.block;

import java.awt.Color;

import vc4.api.graphics.TextureCoords;
import vc4.api.item.ItemStack;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class BlockMultitexture extends Block implements IBlockMultitexture {

	protected int mtIndex;
	
	public BlockMultitexture(int uid, int texture, Material m) {
		super(uid, texture, m);
		
	}

	public BlockMultitexture(int uid, int texture, String material) {
		super(uid, texture, material);
		
	}

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockMultitexture(short uid, int texture, Material m) {
		super(uid, texture, m);
		
	}
	

	public BlockMultitexture(short uid, int texture, Material m, int mtIndex) {
		super(uid, texture, m);
		this.mtIndex = mtIndex;
	}
	
	public BlockMultitexture(int uid, int texture, Material m, int mtIndex) {
		super(uid, texture, m);
		this.mtIndex = mtIndex;
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
		return mtIndex;
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
		return mtIndex;
	}

	/* (non-Javadoc)
	 * @see vc4.api.block.IBlockMultitexture#multitextureUsed(byte, int)
	 */
	@Override
	public boolean multitextureUsed(byte data, int side) {
		return true;
	}

	@Override
	public void setOrientationMultitexture(World world, long x, long y, long z, int side, TextureCoords coords) {
		
	}

	@Override
	public void setOrientationMultitexture(ItemStack item, int side, TextureCoords coords) {

	}

}
