package vc4.api.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.ColorUtils;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

public class BlockRendererCross implements BlockRenderer {

	@Override
	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		AABB bounds = block.getRenderSize(c.getWorld(), x, y, z);
		Vector3f light = ColorUtils.getLightColor(c.getBlockLight(cx, cy, cz));
		for(int d = 0; d < renderers.length; ++d) renderers[d].light(light.x, light.y, light.z, y >= m.getHeight(cx, cz));
		for(int d = 0; d < 2; ++d){
			int tex = block.getTextureIndex(c.getWorld(), x, y, z, d);
			Color color = block.getColor(c.getWorld(), x, y, z, d);
			if(d == 0) renderBlockLeft(x, y, z, renderers[1], bounds, tex, color);
			else renderBlockRight(x, y, z, renderers[1], bounds, tex, color);
		}
		if(!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) block;
		for(int d = 0; d < 2; ++d){
			int tex = mt.getTextureIndexMultitexture(c.getWorld(), x, y, z, d);
			Color color = mt.getColorMultitexture(c.getWorld(), x, y, z, d);
			if(d == 0) renderBlockLeft(x, y, z, renderers[1], bounds, tex, color);
			else renderBlockRight(x, y, z, renderers[1], bounds, tex, color);
		}

	}

	@Override
	public void renderBlock(ItemStack item, float x, float y, float z, Renderer render) {
		Block block = Block.byId(item.getId());
		AABB bounds = block.getRenderSize(item);
		for(int d = 0; d < 2; ++d){
			int tex = block.getTextureIndex(item, d);
			Color color = block.getColor(item, d);
			if(d == 0) renderBlockLeft(x, y, z, render, bounds, tex, color);
			else renderBlockRight(x, y, z, render, bounds, tex, color);
		}
		if(!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) block;
		for(int d = 0; d < 2; ++d){
			int tex = mt.getTextureIndexMultitexture(item, d);
			Color color = mt.getColorMultitexture(item, d);
			if(d == 0) renderBlockLeft(x, y, z, render, bounds, tex, color);
			else renderBlockRight(x, y, z, render, bounds, tex, color);
		}

	}

	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount) {
		//No cracks here
	}
	
	public void renderBlockLeft(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color){
		render.useQuadInputMode(true);
		render.color(color);
		render.tex(bounds.minZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.minZ);
		render.tex(bounds.minZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.maxZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.maxZ);
		render.tex(bounds.maxZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.maxZ);
	}
	
	public void renderBlockRight(double x, double y, double z, Renderer render, AABB bounds, double tex, Color color){
		render.useQuadInputMode(true);
		render.color(color);
		render.tex(bounds.maxZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.maxY, z + bounds.maxZ);
		render.tex(bounds.maxZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.maxX, y + bounds.minY, z + bounds.maxZ);
		render.tex(bounds.minZ, 1 - bounds.minY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.minY, z + bounds.minZ);
		render.tex(bounds.minZ, 1 - bounds.maxY, tex, 0);
		render.addVertex(x + bounds.minX, y + bounds.maxY, z + bounds.minZ);
	}

}
