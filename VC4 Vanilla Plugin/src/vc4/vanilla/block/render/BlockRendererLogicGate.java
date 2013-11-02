package vc4.vanilla.block.render;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererJoinable;
import vc4.api.graphics.Renderer;
import vc4.api.graphics.TextureCoords;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;
import vc4.vanilla.block.IBlockLogicGate;

public class BlockRendererLogicGate extends BlockRendererJoinable {

	@Override
	public void renderBlock(Chunk chunk, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		super.renderBlock(chunk, m, cx, cy, cz, block, data, renderers);
		if (block.isAir()) return;
		IBlockLogicGate gate = (IBlockLogicGate) block;
		long x = chunk.getChunkPos().worldX(cx);
		long y = chunk.getChunkPos().worldY(cy);
		long z = chunk.getChunkPos().worldZ(cz);
		Vector3f light;
		Direction dir;
		AABB bounds = gate.getGateBounds(chunk.getWorld(), x, y, z);
		for (int d = 0; d < 6; ++d) {
			dir = Direction.getDirection(d);
			int rend = 0;
			int llvl = chunk.getBlockLightWithBBCheck(cx + dir.getX(), cy + dir.getY(), cz + dir.getZ());
			light = chunk.getWorld().getGenerator().getLightColor(chunk.getWorld(), m, x, y, z, cx, cz, llvl);
			renderers[rend].light(light.x, light.y, light.z, chunk.getWorld().getNearbySkylight(x, y, z, d));
			renderBlockFace(chunk.getWorld(), x, y, z, gate, renderers[rend], bounds, d);
		}
	}
	
	public void renderBlockFace(World world, long x, long y, long z, IBlockLogicGate gate, Renderer render, AABB bounds, int side) {
		int tex = gate.getTextureIndexGate(world, x, y, z, side);
		Color color = gate.getColorGate(world, x, y, z, side);
		TextureCoords cor = new TextureCoords(bounds, side, tex);
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


	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
	}

	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount) {
	}
}
