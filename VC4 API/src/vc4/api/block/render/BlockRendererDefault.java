/**
 * 
 */
package vc4.api.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.block.render.BlockRenderer;
import vc4.api.graphics.Renderer;
import vc4.api.graphics.TextureCoords;
import vc4.api.item.ItemStack;
import vc4.api.util.*;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

/**
 * @author paul
 * 
 */
public class BlockRendererDefault implements BlockRenderer {

	private static float shadow = 0.9625f;
	
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
		if(c.getBlockLight(cx, cy, cz) > 4) System.out.println("Size");
		Vector3f light;
		Direction dir;
		AABB bounds = block.getRenderSize(c.getWorld(), x, y, z);
		for (int d = 0; d < 6; ++d) {
			if (!block.renderSide(c.getWorld(), x, y, z, d)) continue;
			dir = Direction.getDirection(d);
			int rend = block.getRendererToUse(data, d);
			int llvl = c.getBlockLightWithBBCheck(cx + dir.getX(), cy + dir.getY(), cz + dir.getZ());
			light = c.getWorld().getGenerator().getLightColor(c.getWorld(), m, x, y, z, cx, cz, llvl);
			renderers[rend].light(light.x, light.y, light.z, c.getWorld().hasNearbySkylight(x, y, z, d));
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[rend], bounds, d);
		}
		if (!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) block;
		for (int d = 0; d < 6; ++d) {
			if (!mt.renderSideMultitexture(c.getWorld(), x, y, z, d)) continue;
			dir = Direction.getDirection(d);
			int rend = block.getRendererToUse(data, d);
			int llvl = c.getBlockLightWithBBCheck(cx + dir.getX(), cy + dir.getY(), cz + dir.getZ());
			light = c.getWorld().getGenerator().getLightColor(c.getWorld(), m, x, y, z, cx, cz, llvl);
			renderers[rend].light(light.x, light.y, light.z, c.getWorld().hasNearbySkylight(x, y, z, d));
			renderBlockFace(c.getWorld(), x, y, z, mt, renderers[rend], bounds, d);
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

	public void renderBlockFaceNorth(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.color(color);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
	}
	
	public void renderBlockFaceNorth(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		float eb = 1;
		float wb = 1;
		float et = 1;
		float wt = 1;
		if(world.getBlockType(x + 1, y - 1, z).isSolid(world, x + 1, y - 1, z, 4)){
			eb *= shadow;
			wb *= shadow;
			if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 1)) wb *= shadow;
			else if(world.getBlockType(x + 1, y - 1, z - 1).isSolid(world, x + 1, y - 1, z - 1, 4)) wb *= shadow;
			Block wB = world.getBlockType(x, y, z - 1);
			if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 0)) wb *= shadow;
			if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 3)) eb *= shadow;
			else if(world.getBlockType(x + 1, y - 1, z + 1).isSolid(world, x + 1, y - 1, z + 1, 4)) eb *= shadow;
			Block eB = world.getBlockType(x, y, z + 1);
			if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 0)) eb *= shadow;
		}
		if(world.getBlockType(x + 1, y + 1, z).isSolid(world, x + 1, y + 1, z, 4)){
			et *= shadow;
			wt *= shadow;
			if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 1)) wt *= shadow;
			else if(world.getBlockType(x + 1, y + 1, z - 1).isSolid(world, x + 1, y + 1, z - 1, 4)) wt *= shadow;
			Block wB = world.getBlockType(x, y, z - 1);
			if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 0)) wt *= shadow;
			if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 3)) et *= shadow;
			else if(world.getBlockType(x + 1, y + 1, z + 1).isSolid(world, x + 1, y + 1, z + 1, 4)) et *= shadow;
			Block eB = world.getBlockType(x, y, z + 1);
			if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 0)) et *= shadow;
		}
		render.useQuadInputMode(true);
		render.color(color, et);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.color(color, wt);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.color(color, wb);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.color(color, eb);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
	}
	
	public void renderBlockFace(double x, double y, double z, Block block, ItemStack item, Renderer render, AABB bounds, int side){
		int tex = block.getTextureIndex(item, side);
		Color color = block.getColor(item, side);
		TextureCoords cor = new TextureCoords(bounds, side, tex);
		block.setOrientation(item, side, cor);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, cor, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, cor, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, cor, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, cor, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, cor, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, cor, color);
				break;

		}
	}
	
	public void renderBlockFace(double x, double y, double z, IBlockMultitexture block, ItemStack item, Renderer render, AABB bounds, int side){
		if(!block.multitextureUsed(item.getData(), side)) return;
		int tex = block.getTextureIndexMultitexture(item, side);
		Color color = block.getColorMultitexture(item, side);
		TextureCoords cor = new TextureCoords(bounds, side, tex);
		block.setOrientationMultitexture(item, side, cor);
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, cor, color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, cor, color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, cor, color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, cor, color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, cor, color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, cor, color);
				break;

		}
	}

	public void renderBlockFace(World world, long x, long y, long z, Block block, Renderer render, AABB bounds, int side) {
		int tex = block.getTextureIndex(world, x, y, z, side);
		Color color = block.getColor(world, x, y, z, side);
		TextureCoords cor = new TextureCoords(bounds, side, tex);
		block.setOrientation(world, x, y, z, side, cor);
		switch (side) {
			case 0:
				renderBlockFaceNorth(world, x, y, z, render, bounds, cor, color);
				break;
			case 1:
				renderBlockFaceEast(world, x, y, z, render, bounds, cor, color);
				break;
			case 2:
				renderBlockFaceSouth(world, x, y, z, render, bounds, cor, color);
				break;
			case 3:
				renderBlockFaceWest(world, x, y, z, render, bounds, cor, color);
				break;
			case 4:
				renderBlockFaceTop(world, x, y, z, render, bounds, cor, color);
				break;
			case 5:
				renderBlockFaceBottom(world, x, y, z, render, bounds, cor, color);
				break;

		}

	}
	
	public void renderBlockFaceCracks(World world, long x, long y, long z, Block block, Renderer render, AABB bounds, int side, double amount) {
		Color color = Color.white;
		switch (side) {
			case 0:
				renderBlockFaceNorth(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;
			case 1:
				renderBlockFaceEast(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;
			case 2:
				renderBlockFaceSouth(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;
			case 3:
				renderBlockFaceWest(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;
			case 4:
				renderBlockFaceTop(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;
			case 5:
				renderBlockFaceBottom(x, y, z, render, bounds, new TextureCoords(bounds, side, amount), color);
				break;

		}

	}

	public void renderBlockFace(World world, long x, long y, long z, IBlockMultitexture block, Renderer render, AABB bounds, int side) {
		int tex = block.getTextureIndexMultitexture(world, x, y, z, side);
		Color color = block.getColorMultitexture(world, x, y, z, side);
		TextureCoords cor = new TextureCoords(bounds, side, tex);
		block.setOrientationMultitexture(world, x, y, z, side, cor);
		switch (side) {
			case 0:
				renderBlockFaceNorth(world, x, y, z, render, bounds, cor, color);
				break;
			case 1:
				renderBlockFaceEast(world, x, y, z, render, bounds, cor, color);
				break;
			case 2:
				renderBlockFaceSouth(world, x, y, z, render, bounds, cor, color);
				break;
			case 3:
				renderBlockFaceWest(world, x, y, z, render, bounds, cor, color);
				break;
			case 4:
				renderBlockFaceTop(world, x, y, z, render, bounds, cor, color);
				break;
			case 5:
				renderBlockFaceBottom(world, x, y, z, render, bounds, cor, color);
				break;

		}
	}

	public void renderBlockFaceEast(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);

	}
	
	public void renderBlockFaceEast(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		float nb = 1;
		float sb = 1;
		float nt = 1;
		float st = 1;
		if(world.getBlockType(x, y - 1, z + 1).isSolid(world, x, y - 1, z + 1, 4)){
			nb *= shadow;
			sb *= shadow;
			if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 0)) sb *= shadow;
			else if(world.getBlockType(x - 1, y - 1, z + 1).isSolid(world, x - 1, y - 1, z + 1, 4)) sb *= shadow;
			Block sB = world.getBlockType(x - 1, y, z);
			if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 1)) sb *= shadow;
			if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 2)) nb *= shadow;
			else if(world.getBlockType(x + 1, y - 1, z + 1).isSolid(world, x + 1, y - 1, z + 1, 4)) nb *= shadow;
			Block nB = world.getBlockType(x + 1, y, z);
			if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 1)) nb *= shadow;
		}
		if(world.getBlockType(x, y + 1, z + 1).isSolid(world, x, y + 1, z + 1, 4)){
			nt *= shadow;
			st *= shadow;
			if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 0)) st *= shadow;
			else if(world.getBlockType(x - 1, y + 1, z + 1).isSolid(world, x - 1, y + 1, z + 1, 4)) st *= shadow;
			Block sB = world.getBlockType(x - 1, y, z);
			if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 1)) st *= shadow;
			if(world.getBlockType(x + 1, y, z + 1).isSolid(world, x + 1, y, z + 1, 2)) nt *= shadow;
			else if(world.getBlockType(x + 1, y + 1, z + 1).isSolid(world, x + 1, y + 1, z + 1, 4)) nt *= shadow;
			Block nB = world.getBlockType(x + 1, y, z);
			if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 1)) nt *= shadow;
		}
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color, st);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, nt);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, nb);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, sb);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);

	}


	public void renderBlockFaceSouth(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);

	}
	
	public void renderBlockFaceSouth(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		float eb = 1;
		float wb = 1;
		float et = 1;
		float wt = 1;
		if(world.getBlockType(x - 1, y - 1, z).isSolid(world, x - 1, y - 1, z, 4)){
			eb *= shadow;
			wb *= shadow;
			if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 1)) wb *= shadow;
			else if(world.getBlockType(x - 1, y - 1, z - 1).isSolid(world, x - 1, y - 1, z - 1, 4)) wb *= shadow;
			Block wB = world.getBlockType(x, y, z - 1);
			if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 2)) wb *= shadow;
			if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 3)) eb *= shadow;
			else if(world.getBlockType(x - 1, y - 1, z + 1).isSolid(world, x - 1, y - 1, z + 1, 4)) eb *= shadow;
			Block eB = world.getBlockType(x, y, z + 1);
			if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 2)) eb *= shadow;
		}
		if(world.getBlockType(x - 1, y + 1, z).isSolid(world, x - 1, y + 1, z, 4)){
			et *= shadow;
			wt *= shadow;
			if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 1)) wt *= shadow;
			else if(world.getBlockType(x - 1, y + 1, z - 1).isSolid(world, x - 1, y + 1, z - 1, 4)) wt *= shadow;
			Block wB = world.getBlockType(x, y, z - 1);
			if(wB.isSolid(world, x, y, z - 1, 1) && wB.isSolid(world, x, y, z - 1, 2)) wt *= shadow;
			if(world.getBlockType(x - 1, y, z + 1).isSolid(world, x - 1, y, z + 1, 3)) et *= shadow;
			else if(world.getBlockType(x - 1, y + 1, z + 1).isSolid(world, x - 1, y + 1, z + 1, 4)) et *= shadow;
			Block eB = world.getBlockType(x, y, z + 1);
			if(wB.isSolid(world, x, y, z + 1, 3) && eB.isSolid(world, x, y, z + 1, 2)) et *= shadow;
		}
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color, wt);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, et);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, eb);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, wb);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);

	}

	public void renderBlockFaceWest(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);

	}
	
	public void renderBlockFaceWest(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		float nb = 1;
		float sb = 1;
		float nt = 1;
		float st = 1;
		if(world.getBlockType(x, y - 1, z - 1).isSolid(world, x, y - 1, z - 1, 4)){
			nb *= shadow;
			sb *= shadow;
			if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 0)) sb *= shadow;
			else if(world.getBlockType(x - 1, y - 1, z - 1).isSolid(world, x - 1, y - 1, z - 1, 4)) sb *= shadow;
			Block sB = world.getBlockType(x - 1, y, z);
			if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 3)) sb *= shadow;
			if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 2)) nb *= shadow;
			else if(world.getBlockType(x + 1, y - 1, z - 1).isSolid(world, x + 1, y - 1, z - 1, 4)) nb *= shadow;
			Block nB = world.getBlockType(x + 1, y, z);
			if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 3)) nb *= shadow;
		}
		if(world.getBlockType(x, y + 1, z - 1).isSolid(world, x, y + 1, z - 1, 4)){
			nt *= shadow;
			st *= shadow;
			if(world.getBlockType(x - 1, y, z - 1).isSolid(world, x - 1, y, z - 1, 0)) st *= shadow;
			else if(world.getBlockType(x - 1, y + 1, z - 1).isSolid(world, x - 1, y + 1, z - 1, 4)) st *= shadow;
			Block sB = world.getBlockType(x - 1, y, z);
			if(sB.isSolid(world, x - 1, y, z, 0) && sB.isSolid(world, x - 1, y, z, 3)) st *= shadow;
			if(world.getBlockType(x + 1, y, z - 1).isSolid(world, x + 1, y, z - 1, 2)) nt *= shadow;
			else if(world.getBlockType(x + 1, y + 1, z - 1).isSolid(world, x + 1, y + 1, z - 1, 4)) nt *= shadow;
			Block nB = world.getBlockType(x + 1, y, z);
			if(sB.isSolid(world, x + 1, y, z, 2) && nB.isSolid(world, x + 1, y, z, 3)) nt *= shadow;
		}
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color, nt);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, st);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, sb);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, nb);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);

	}

	public void renderBlockFaceTop(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);

	}
	
	public void renderBlockFaceTop(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
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
		render.tex(tex.next());
		render.color(color, sw);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, nw);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, ne);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, se);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);

	}

	public void renderBlockFaceBottom(double x, double y, double z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
		render.useQuadInputMode(true);
		render.tex(tex.next());
		render.color(color);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);

	}
	
	public void renderBlockFaceBottom(World world, long x, long y, long z, Renderer render, AABB bounds, TextureCoords tex, Color color) {
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
		render.tex(tex.next());
		render.color(color, nw);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, sw);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(tex.next());
		render.color(color, se);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(tex.next());
		render.color(color, ne);
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
