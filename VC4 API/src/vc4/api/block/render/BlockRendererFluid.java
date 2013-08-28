/**
 * 
 */
package vc4.api.block.render;

import java.awt.Color;
import java.util.Arrays;

import vc4.api.block.Block;
import vc4.api.block.BlockFluid;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

/**
 * @author paul
 *
 */
public class BlockRendererFluid implements BlockRenderer {

	/* (non-Javadoc)
	 * @see vc4.api.block.render.BlockRenderer#renderBlock(vc4.api.world.Chunk, int, int, int, vc4.api.block.Block, byte, vc4.api.graphics.Renderer[])
	 */
	@Override
	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		Vector3f light = c.getWorld().getGenerator().getLightColor(c.getWorld(), m, x, y, z, cx, cz, c.getBlockLight(cx, cy, cz));
		for(int d = 0; d < renderers.length; ++d) renderers[d].light(light.x, light.y, light.z, y >= m.getHeight(cx, cz));
		double heights[] = null;
		for(int d = 0; d < 6; ++d){
			if(!block.renderSide(c.getWorld(), x, y, z, d)) continue;
			if(d < 5 && heights == null) heights = ((BlockFluid)block).getSideHeights(c.getWorld(), x, y, z);
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[2], heights, d);
		}
	}
	
	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount){
		
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param world
	 * @param renderer
	 * @param heights
	 */
	public void renderBlockFace(World world, long x, long y, long z, Block block, Renderer render, double[] heights, int side) {
		int tex = block.getTextureIndex(world, x, y, z, side);
		Color color = block.getColor(world, x, y, z, side);
		switch(side){
			case 0:
				renderBlockFaceNorth(x, y, z, render, heights, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, heights, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, heights, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, heights, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, heights, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, tex, color);
				break;
		}
	}
	
	public void renderBlockFace(double x, double y, double z, Block block, ItemStack item, Renderer render, double[] heights, int side){
		int tex = block.getTextureIndex(item, side);
		Color color = block.getColor(item, side);
		switch(side){
			case 0:
				renderBlockFaceNorth(x, y, z, render, heights, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, heights, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, heights, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, heights, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, heights, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, tex, color);
				break;
		}
	}

	public void renderBlockFaceNorth(double x, double y, double z, Renderer render, double[] heights, int tex, Color color){
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y + heights[0], z + 1);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 1, y + heights[3], z);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 1, y, z);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 1, y, z + 1);
	}
	
	public void renderBlockFaceEast(double x, double y, double z, Renderer render, double[] heights, int tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x, y + heights[1], z + 1);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 1, y + heights[0], z + 1);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 1, y, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x, y, z + 1);

	}
	
	public void renderBlockFaceSouth(double x, double y, double z, Renderer render, double[] heights, int tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x, y + heights[2], z);
		render.tex(1, 0, tex, 0);
		render.addVertex(x, y + heights[1], z + 1);
		render.tex(1, 1, tex, 0);
		render.addVertex(x, y, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x, y, z);

	}
	
	public void renderBlockFaceWest(double x, double y, double z, Renderer render, double[] heights, int tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y + heights[3], z);
		render.tex(1, 0, tex, 0);
		render.addVertex(x, y + heights[2], z);
		render.tex(1, 1, tex, 0);
		render.addVertex(x, y, z);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 1, y, z);

	}
	
	public void renderBlockFaceTop(double x, double y, double z, Renderer render, double[] heights, int tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x, y + heights[2], z);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 1, y + heights[3], z);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 1, y + heights[0], z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x, y + heights[1], z + 1);

	}
	
	public void renderBlockFaceBottom(double x, double y, double z, Renderer render, int tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y, z);
		render.tex(1, 0, tex, 0);
		render.addVertex(x, y, z);
		render.tex(1, 1, tex, 0);
		render.addVertex(x, y, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 1, y, z + 1);

	}

	/* (non-Javadoc)
	 * @see vc4.api.block.render.BlockRenderer#renderBlock(vc4.api.item.ItemStack, float, float, float, vc4.api.graphics.Renderer)
	 */
	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		Block b = Block.byId(block.getId());
		double[] heights = new double[4];
		Arrays.fill(heights, 0.95f);
		for(int d = 0; d < 6; ++d){
			renderBlockFace(x, y, z, b, block, render, heights, d);
		}
	}

}
