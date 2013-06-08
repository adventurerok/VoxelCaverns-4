package vc4.vanilla.block.render;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.block.render.BlockRendererDefault;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.world.Chunk;
import vc4.api.world.World;

public class BlockRendererTable extends BlockRendererDefault {

	AABB top = AABB.getBoundingBox(0, 1, 15/16d, 1, 0, 1);
	AABB[] legs = new AABB[]{AABB.getBoundingBox(0, 1/16d, 0, 15/16d, 0, 1/16d), AABB.getBoundingBox(15/16d, 1d, 0, 15/16d, 0, 1/16d), AABB.getBoundingBox(15/16d, 1d, 0, 15/16d, 15/16d, 1d), AABB.getBoundingBox(0, 1/16d, 0, 15/16d, 15/16d, 1d)};
	@Override
	public void renderBlock(Chunk c, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		for (int d = 0; d < 6; ++d) {
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], top, d);
		}
		for(int a = 0; a < 4; ++a){
			for (int d = 0; d < 6; ++d) {
				if(d == 4) continue;
				renderBlockFace(c.getWorld(), x, y, z, block, renderers[block.getRendererToUse(data, d)], legs[a], d);
			}
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

	}

	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		// TASK Auto-generated method stub

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
	}

}
