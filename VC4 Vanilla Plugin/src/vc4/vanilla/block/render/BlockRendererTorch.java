package vc4.vanilla.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRenderer;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

public class BlockRendererTorch implements BlockRenderer {

	@Override
	public void renderBlock(Chunk c, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		Vector3f light = c.getWorld().getGenerator().getLightColor(c.getWorld(), m, x, y, z, cx, cz, c.getBlockLight(cx, cy, cz));
		renderers[1].light(light, c.getWorld().hasSkyLight(x, y, z));
		Color color = block.getColor(c.getWorld(), x, y, z, 0);
		int tex = block.getTextureIndex(c.getWorld(), x, y, z, 0);
		if(data == 0) renderTorchUpright(renderers[1], x, y, z, tex, color);
		else renderTorchSlanting(renderers[1], x, y, z, tex, color, data - 1);
	}
	
	public void renderTorchUpright(Renderer render, double x, double y, double z, int te, Color color) {
		render.useQuadInputMode(true);
		render.color(color);
		// Side 0
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.5625F);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		// Side 1
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.5625F);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.5625F);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
		// Side 2
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.5625F);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		// Side 3
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		// side 4
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.4375F);
		render.tex(0.5625F, 0.46875F, te);
		render.addVertex(x + 0.5625F, y + 0.65625F, z + 0.5625F);
		render.tex(0.4375F, 0.46875F, te);
		render.addVertex(x + 0.4375F, y + 0.65625F, z + 0.5625F);
		// side 5
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
	}
	
	public void renderTorchSlanting(Renderer render, double x, double y, double z, int te, Color color, int dir) {
		y += 0.171875;
		double xTop = 0;
		double zTop = 0;
		if (dir == 0) {
			xTop = 0.3;
			x -= 0.5;
		} else if (dir == 1) {
			zTop = 0.3;
			z -= 0.5;
		} else if (dir == 2) {
			xTop = -0.3;
			x += 0.5;
		} else if (dir == 3) {
			zTop = -0.3;
			z += 0.5;
		}

		render.useQuadInputMode(true);
		//render.customAttrib(block.uid, data, 0, 0);
		render.color(color);
		// Side 0
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		// Side 1
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
		// Side 2
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		// Side 3
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 1F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		render.tex(0.4375F, 1F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		// side 4
		render.tex(0.4375F, 0.34375F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 0.34375F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.4375F + zTop);
		render.tex(0.5625F, 0.46875F, te);
		render.addVertex(x + 0.5625F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		render.tex(0.4375F, 0.46875F, te);
		render.addVertex(x + 0.4375F + xTop, y + 0.65625F, z + 0.5625F + zTop);
		// side 5
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.4375F, y, z + 0.4375F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.5625F, y, z + 0.4375F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.5625F, y, z + 0.5625F);
		render.tex(0.46875F, 0.9375F, te);
		render.addVertex(x + 0.4375F, y, z + 0.5625F);
	}

	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		Color col = Block.byId(block.getId()).getColor(block, 0);
		int tex = Block.byId(block.getId()).getTextureIndex(block, 0);
		renderTorchUpright(render, x, y, z, tex, col);
	}

	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount) {
		
	}

}
