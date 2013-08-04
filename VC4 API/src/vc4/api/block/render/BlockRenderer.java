/**
 * 
 */
package vc4.api.block.render;

import vc4.api.block.Block;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.world.*;

/**
 * @author paul
 *
 */
public interface BlockRenderer {

	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers);
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render);
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount);
}
