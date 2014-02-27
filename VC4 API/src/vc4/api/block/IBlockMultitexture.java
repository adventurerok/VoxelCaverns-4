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
public interface IBlockMultitexture {

	public boolean multitextureUsed(byte data, int side);

	public boolean renderSideMultitexture(World world, long x, long y, long z, int side);

	public Color getColorMultitexture(World world, long x, long y, long z, int side);

	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side);

	public Color getColorMultitexture(ItemStack item, int side);

	public int getTextureIndexMultitexture(ItemStack item, int side);

	public void setOrientationMultitexture(World world, long x, long y, long z, int side, TextureCoords coords);

	public void setOrientationMultitexture(ItemStack item, int side, TextureCoords coords);
}
