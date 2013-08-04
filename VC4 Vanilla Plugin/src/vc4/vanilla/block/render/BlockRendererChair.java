package vc4.vanilla.block.render;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.block.render.BlockRendererDefault;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.ColorUtils;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

public class BlockRendererChair extends BlockRendererDefault {

	float min = 2/16f;
	float max = 14/16f;
	float legMax = 7/16f;
	float small = 1/16f;
	float baseMax = legMax + small;
	AABB top = AABB.getBoundingBox(min, max, legMax, baseMax, min, max);
	AABB[] legs = new AABB[]{AABB.getBoundingBox(min, min + 1/16d, 0, legMax, min, min + 1/16d), AABB.getBoundingBox(15/16d - min, max, 0, legMax, min, min + 1/16d), AABB.getBoundingBox(15/16d - min, max, 0, legMax, 15/16d - min, max), AABB.getBoundingBox(min, min + 1/16d, 0, legMax, 15/16d - min, max)};
	AABB[] sides = new AABB[]{AABB.getBoundingBox(max - small, max, baseMax, 1, min, max), AABB.getBoundingBox(min, max, baseMax, 1, max - small, max), AABB.getBoundingBox(min, min + small, baseMax, 1, min, max), AABB.getBoundingBox(min, max, baseMax, 1, min, min + small)};
	
	@Override
	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		Vector3f light = ColorUtils.getLightColor(c.getBlockLight(cx, cy, cz));
		for(int d = 0; d < renderers.length; ++d) renderers[d].light(light.x, light.y, light.z, y >= m.getHeight(cx, cz));
		for (int d = 0; d < 6; ++d) {
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], top, d);
		}
		for(int a = 0; a < 4; ++a){
			for (int d = 0; d < 6; ++d) {
				if(d == 4) continue;
				renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], legs[a], d);
			}
		}
		if((data & 1) != 0){
			for(int d = 0; d < 5; ++d)  renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], sides[0], d);
		}
		if((data & 2) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], sides[1], d);
		}
		if((data & 4) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], sides[2], d);
		}
		if((data & 8) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], sides[3], d);
		}
		if (!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mt = (IBlockMultitexture) block;
		for (int d = 0; d < 6; ++d) {
			renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], top, d);
		}
		for(int a = 0; a < 4; ++a){
			for (int d = 0; d < 6; ++d) {
				if(d == 4) continue;
				renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], legs[a], d);
			}
		}
		if((data & 1) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], sides[0], d);
		}
		if((data & 2) != 0){
			for(int d = 0; d < 5; ++d)  renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], sides[1], d);
		}
		if((data & 4) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], sides[2], d);
		}
		if((data & 8) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[block.getRendererToUse(data, d)], sides[3], d);
		}
	}
	
	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		super.renderBlock(block, x, y, z, render);
	}
	
	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount) {
		Block block = world.getBlockType(x, y, z);
		if(block == null) return;
		for (int d = 0; d < 6; ++d) {
			renderBlockFaceCracks(world, x, y, z, block, render, top, d, amount);
		}
		for(int a = 0; a < 4; ++a){
			for (int d = 0; d < 6; ++d) {
				if(d == 4) continue;
				renderBlockFaceCracks(world, x, y, z, block, render, legs[a], d, amount);
			}
		}
		byte data = world.getBlockData(x, y, z);
		if((data & 1) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFaceCracks(world, x, y, z, block, render, sides[0], d, amount);
		}
		if((data & 2) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFaceCracks(world, x, y, z, block, render, sides[1], d, amount);
		}
		if((data & 4) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFaceCracks(world, x, y, z, block, render, sides[2], d, amount);
		}
		if((data & 8) != 0){
			for(int d = 0; d < 5; ++d) renderBlockFaceCracks(world, x, y, z, block, render, sides[3], d, amount);
		}
	}
}
