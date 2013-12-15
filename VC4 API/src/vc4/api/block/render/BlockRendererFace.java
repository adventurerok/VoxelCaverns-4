/**
 * 
 */
package vc4.api.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

/**
 * @author paul
 * 
 */
public class BlockRendererFace implements BlockRenderer {

	private static float shadow = 0.97f;
	private static float min = 1/16f;
	private static float max = 15/16f;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.render.BlockRenderer#renderBlock(vc4.api.world.Chunk, int, int, int, vc4.api.block.Block, byte)
	 */
	@Override
	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		if (block.isAir()) return;
		Vector3f light = c.getWorld().getGenerator().getLightColor(c.getWorld(), m, x, y, z, cx, cz, c.getBlockLight(cx, cy, cz));
		for(int d = 0; d < renderers.length; ++d) renderers[d].light(light.x, light.y, light.z, y >= m.getHeight(cx, cz));
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
		render.tex(0, 0, tex, 0);
		render.addVertex(x + max, y + 1, z + 1);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + max, y + 1, z + 0);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + max, y + 0, z + 0);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + max, y + 0, z + 1);
	}
	
	public void renderBlockFaceNorth(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		float e = 1;
		float w = 1;
		if(world.getBlockType(x + 1, y - 1, z).isSolid(world, x + 1, y - 1, z, 4)){
			e *= shadow;
			w *= shadow;
		}
		if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 1)) w *= shadow;
		else if(world.getBlockType(x + 1, y - 1, z - 1).isSolid(world, x + 1, y - 1, z - 1, 4)) w *= shadow;
		Block wB = world.getBlockType(x, y, z - 1);
		if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 0)) w *= shadow;
		if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 3)) e *= shadow;
		else if(world.getBlockType(x + 1, y - 1, z + 1).isSolid(world, x + 1, y - 1, z + 1, 4)) e *= shadow;
		Block eB = world.getBlockType(x, y, z + 1);
		if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 0)) e *= shadow;
		render.useQuadInputMode(true);
		render.color(color, e);
		render.tex(0, 1 - 1, tex, 0);
		render.addVertex(x + max, y + 1, z + 1);
		render.color(color, w);
		render.tex(1, 1 - 1, tex, 0);
		render.addVertex(x + max, y + 1, z + 0);
		render.color(color, w);
		render.tex(1, 1 - 0, tex, 0);
		render.addVertex(x + max, y + 0, z + 0);
		render.color(color, e);
		render.tex(0, 1 - 0, tex, 0);
		render.addVertex(x + max, y + 0, z + 1);
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
				renderBlockFaceNorth(world, x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(world, x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(world, x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(world, x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(world, x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(world, x, y, z, render, bounds, tex, color);
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
				renderBlockFaceNorth(world, x, y, z, render, bounds, tex, color);
				break;
			case 1:
				renderBlockFaceEast(world, x, y, z, render, bounds, tex, color);
				break;
			case 2:
				renderBlockFaceSouth(world, x, y, z, render, bounds, tex, color);
				break;
			case 3:
				renderBlockFaceWest(world, x, y, z, render, bounds, tex, color);
				break;
			case 4:
				renderBlockFaceTop(world, x, y, z, render, bounds, tex, color);
				break;
			case 5:
				renderBlockFaceBottom(world, x, y, z, render, bounds, tex, color);
				break;

		}
	}

	public void renderBlockFaceEast(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color);
		render.addVertex(x + 0, y + 1, z + max);
		render.tex(1, 1 - 1, tex, 0);
		render.addVertex(x + 1, y + 1, z + max);
		render.tex(1, 1 - 0, tex, 0);
		render.addVertex(x + 1, y + 0, z + max);
		render.tex(0, 1 - 0, tex, 0);
		render.addVertex(x + 0, y + 0, z + max);

	}
	
	public void renderBlockFaceEast(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		float n = 1;
		float s = 1;
		if(world.getBlockType(x, y - 1, z + 1).isSolid(world, x, y - 1, z + 1, 4)){
			n *= shadow;
			s *= shadow;
		}
		if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 0)) s *= shadow;
		else if(world.getBlockType(x - 1, y - 1, z + 1).isSolid(world, x - 1, y - 1, z + 1, 4)) s *= shadow;
		Block sB = world.getBlockType(x - 1, y, z);
		if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 1)) s *= shadow;
		if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 2)) n *= shadow;
		else if(world.getBlockType(x + 1, y - 1, z + 1).isSolid(world, x + 1, y - 1, z + 1, 4)) n *= shadow;
		Block nB = world.getBlockType(x + 1, y, z);
		if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 1)) n *= shadow;
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color, s);
		render.addVertex(x + 0, y + 1, z + max);
		render.tex(1, 1 - 1, tex, 0);
		render.color(color, n);
		render.addVertex(x + 1, y + 1, z + max);
		render.tex(1, 1 - 0, tex, 0);
		render.color(color, n);
		render.addVertex(x + 1, y + 0, z + max);
		render.tex(0, 1 - 0, tex, 0);
		render.color(color, s);
		render.addVertex(x + 0, y + 0, z + max);

	}


	public void renderBlockFaceSouth(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color);
		render.addVertex(x + min, y + 1, z + 0);
		render.tex(1, 1 - 1, tex, 0);
		render.addVertex(x + min, y + 1, z + 1);
		render.tex(1, 1 - 0, tex, 0);
		render.addVertex(x + min, y + 0, z + 1);
		render.tex(0, 1 - 0, tex, 0);
		render.addVertex(x + min, y + 0, z + 0);

	}
	
	public void renderBlockFaceSouth(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		float e = 1;
		float w = 1;
		if(world.getBlockType(x - 1, y - 1, z).isSolid(world, x - 1, y - 1, z, 4)){
			e *= shadow;
			w *= shadow;
		}
		if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 1)) w *= shadow;
		else if(world.getBlockType(x - 1, y - 1, z - 1).isSolid(world, x - 1, y - 1, z - 1, 4)) w *= shadow;
		Block wB = world.getBlockType(x, y, z - 1);
		if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 2)) w *= shadow;
		if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 3)) e *= shadow;
		else if(world.getBlockType(x - 1, y - 1, z + 1).isSolid(world, x - 1, y - 1, z + 1, 4)) e *= shadow;
		Block eB = world.getBlockType(x, y, z + 1);
		if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 2)) e *= shadow;
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color, w);
		render.addVertex(x + min, y + 1, z + 0);
		render.tex(1, 1 - 1, tex, 0);
		render.color(color, e);
		render.addVertex(x + min, y + 1, z + 1);
		render.tex(1, 1 - 0, tex, 0);
		render.color(color, e);
		render.addVertex(x + min, y + 0, z + 1);
		render.tex(0, 1 - 0, tex, 0);
		render.color(color, w);
		render.addVertex(x + min, y + 0, z + 0);

	}

	public void renderBlockFaceWest(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y + 1, z + min);
		render.tex(1, 1 - 1, tex, 0);
		render.addVertex(x + 0, y + 1, z + min);
		render.tex(1, 1 - 0, tex, 0);
		render.addVertex(x + 0, y + 0, z + min);
		render.tex(0, 1 - 0, tex, 0);
		render.addVertex(x + 1, y + 0, z + min);

	}
	
	public void renderBlockFaceWest(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		float n = 1;
		float s = 1;
		if(world.getBlockType(x, y - 1, z - 1).isSolid(world, x, y - 1, z - 1, 4)){
			n *= shadow;
			s *= shadow;
		}
		if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 0)) s *= shadow;
		else if(world.getBlockType(x - 1, y - 1, z - 1).isSolid(world, x - 1, y - 1, z - 1, 4)) s *= shadow;
		Block sB = world.getBlockType(x - 1, y, z);
		if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 3)) s *= shadow;
		if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 2)) n *= shadow;
		else if(world.getBlockType(x + 1, y - 1, z - 1).isSolid(world, x + 1, y - 1, z - 1, 4)) n *= shadow;
		Block nB = world.getBlockType(x + 1, y, z);
		if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 3)) n *= shadow;
		render.useQuadInputMode(true);
		render.tex(0, 1 - 1, tex, 0);
		render.color(color, n);
		render.addVertex(x + 1, y + 1, z + min);
		render.tex(1, 1 - 1, tex, 0);
		render.color(color, s);
		render.addVertex(x + 0, y + 1, z + min);
		render.tex(1, 1 - 0, tex, 0);
		render.color(color, s);
		render.addVertex(x + 0, y + 0, z + min);
		render.tex(0, 1 - 0, tex, 0);
		render.color(color, n);
		render.addVertex(x + 1, y + 0, z + min);

	}

	public void renderBlockFaceTop(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 0, y + max, z + 0);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 1, y + max, z + 0);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 1, y + max, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 0, y + max, z + 1);

	}
	
	public void renderBlockFaceTop(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		float ne = 1;
		float se = 1;
		float sw = 1;
		float nw = 1;
		if(world.getBlockType(x + 1, y + 1, z).isSolid(world, x + 1, y + 1, z, 2)){
			ne *= shadow;
			nw *= shadow;
		}
		if(world.getBlockType(x - 1, y + 1, z).isSolid(world, x - 1, y + 1, z, 0)){
			se *= shadow;
			sw *= shadow;
		}
		if(world.getBlockType(x, y + 1, z + 1).isSolid(world, x, y + 1, z + 1, 3)){
			ne *= shadow;
			se *= shadow;
		}
		if(world.getBlockType(x, y + 1, z - 1).isSolid(world, x, y + 1, z - 1, 1)){
			nw *= shadow;
			sw *= shadow;
		}
		Block neB = world.getBlockType(x + 1, y + 1, z + 1);
		Block nwB = world.getBlockType(x + 1, y + 1, z - 1);
		Block seB = world.getBlockType(x - 1, y + 1, z + 1);
		Block swB = world.getBlockType(x - 1, y + 1, z - 1);
		if(neB.isSolid(world, x + 1, y + 1, z + 1, 2) && neB.isSolid(world, x + 1, y + 1, z + 1, 3)) ne *= shadow;
		if(nwB.isSolid(world, x + 1, y + 1, z - 1, 2) && nwB.isSolid(world, x + 1, y + 1, z - 1, 1)) nw *= shadow;
		if(seB.isSolid(world, x - 1, y + 1, z + 1, 0) && seB.isSolid(world, x - 1, y + 1, z + 1, 3)) se *= shadow;
		if(swB.isSolid(world, x - 1, y + 1, z - 1, 0) && swB.isSolid(world, x - 1, y + 1, z - 1, 1)) sw *= shadow;
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color, sw);
		render.addVertex(x + 0, y + max, z + 0);
		render.tex(1, 0, tex, 0);
		render.color(color, nw);
		render.addVertex(x + 1, y + max, z + 0);
		render.tex(1, 1, tex, 0);
		render.color(color, ne);
		render.addVertex(x + 1, y + max, z + 1);
		render.tex(0, 1, tex, 0);
		render.color(color, se);
		render.addVertex(x + 0, y + max, z + 1);

	}

	public void renderBlockFaceBottom(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y + min, z + 0);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 0, y + min, z + 0);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 0, y + min, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 1, y + min, z + 1);

	}
	
	public void renderBlockFaceBottom(World world, long x, long y, long z, Renderer render, AABB bounds, double tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(0, 0, tex, 0);
		render.color(color);
		render.addVertex(x + 1, y + min, z + 0);
		render.tex(1, 0, tex, 0);
		render.addVertex(x + 0, y + min, z + 0);
		render.tex(1, 1, tex, 0);
		render.addVertex(x + 0, y + min, z + 1);
		render.tex(0, 1, tex, 0);
		render.addVertex(x + 1, y + min, z + 1);

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
