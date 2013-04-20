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

	public int getTextureIndexMultitexture(ItemStack item);
	public Color getColorMultitexture(ItemStack item);
}
