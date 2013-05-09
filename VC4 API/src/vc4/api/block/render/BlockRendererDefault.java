/**
 * 
 */
package vc4.api.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.block.render.BlockRenderer;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.world.Chunk;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public class BlockRendererDefault implements BlockRenderer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.render.BlockRenderer#renderBlock(vc4.api.world.Chunk, int, int, int, vc4.api.block.Block, byte)
	 */
	@Override
	public void renderBlock(Chunk c, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		if (block.isAir()) return;
		AABB bounds = block.getRenderSize(c.getWorld(), x, y, z);
		for (int d = 0; d < 6; ++d) {
			if (!block.renderSide(c.getWorld(), x, y, z, d)) continue;
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], bounds, d);
		}
		if (!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) block;
		for (int d = 0; d < 6; ++d) {
			if (!mt.renderSideMultitexture(c.getWorld(), x, y, z, d)) continue;
			renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], bounds, d);
		}

	}
	
	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount){
			Block block = world.getBlockType(x, y, z);
			AABB bounds = block.getRenderSize(world, x, y, z);
			bounds.expand(0.002, 0.002, 0.002);
			for(int d = 0; d < 6; ++d){
				renderBlockFaceCracks(world, x, y, z, block, render, bounds, d, amount);
			}
	}

	public void renderBlockFaceNorth(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.color(color);
		render.tex(bounds.minZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.maxZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.minZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
	}
	
	public void renderBlockFace(double x, double y, double z, Block block, ItemStack item, Renderer render, AABB bounds, int side){
		int tex = block.getTextureIndex(item, side);
		Color color = block.getColor(item, side);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, tex, color);
				break;

		}
	}
	
	public void renderBlockFace(double x, double y, double z, IBlockMultitexture block, ItemStack item, Renderer render, AABB bounds, int side){
		if(!block.multitextureUsed(item.getData(), side)) return;
		int tex = block.getTextureIndexMultitexture(item, side);
		Color color = block.getColorMultitexture(item, side);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, tex, color);
				break;

		}
	}

	public void renderBlockFace(World world, long x, long y, long z, Block block, Renderer render, AABB bounds, int side) {
		int tex = block.getTextureIndex(world, x, y, z, side);
		Color color = block.getColor(world, x, y, z, side);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, tex, color);
				break;

		}

	}
	
	public void renderBlockFaceCracks(World world, long x, long y, long z, Block block, Renderer render, AABB bounds, int side, double amount) {
		Color color = Color.white;
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, amount, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, amount, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, amount, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, amount, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, amount, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, amount, color);
				break;

		}

	}

	public void renderBlockFace(World world, long x, long y, long z, IBlockMultitexture block, Renderer render, AABB bounds, int side) {
		int tex = block.getTextureIndexMultitexture(world, x, y, z, side);
		Color color = block.getColorMultitexture(world, x, y, z, side);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, tex, color);
				break;

		}
	}

	public void renderBlockFaceEast(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(bounds.minX, 1 - bounds.maxY, tex, 0);
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.maxX, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.maxX, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
		render.tex(bounds.minX, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);

	}


	public void renderBlockFaceSouth(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(bounds.minZ, 1 - bounds.maxY, tex, 0);
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.maxZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(bounds.minZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);

	}

	public void renderBlockFaceWest(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(bounds.minX, 1 - bounds.maxY, tex, 0);
		render.color(color);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxX, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxX, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.minX, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);

	}

	public void renderBlockFaceTop(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(bounds.minX, bounds.minZ, tex, 0);
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxX, bounds.minZ, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.maxX, bounds.maxZ, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.minX, bounds.maxZ, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);

	}

	public void renderBlockFaceBottom(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(bounds.minX, bounds.minZ, tex, 0);
		render.color(color);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.maxX, bounds.minZ, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.maxX, bounds.maxZ, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(bounds.minX, bounds.maxZ, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.render.BlockRenderer#renderBlock(vc4.api.item.ItemStack, float, float, float)
	 */
	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		Block b = Block.byId(block.getId());
		AABB bounds = b.getRenderSize(block);
		for(int d = 0; d < 6; ++d){
			renderBlockFace(x, y, z, b, block, render, bounds, d);
		}
		if(!(b instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) b;
		for(int d = 0; d < 6; ++d){
			renderBlockFace(x, y, z, mt, block, render, bounds, d);
		}
	}

}
