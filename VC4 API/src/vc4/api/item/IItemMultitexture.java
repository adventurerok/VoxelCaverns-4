/**
 * 
 */
package vc4.api.item;

import java.awt.Color;

/**
 * @author paul
 * 
 */
public interface IItemMultitexture {

	public int getMultitextureTextureIndex(ItemStack item);

	public Color getMultitextureColor(ItemStack item);
}
