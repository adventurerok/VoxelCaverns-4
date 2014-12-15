package vc4.api.block.render;

import java.awt.Color;

import vc4.api.block.*;
import vc4.api.graphics.Renderer;
import vc4.api.graphics.TextureCoords;
import vc4.api.item.ItemStack;
import vc4.api.vector.Vector3f;
import vc4.api.world.*;

public class BlockRendererJoinable extends BlockRendererDefault {

	@Override
	public void renderBlock(Chunk chunk, MapData m, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = chunk.getChunkPos().worldX(cx);
		long y = chunk.getChunkPos().worldY(cy);
		long z = chunk.getChunkPos().worldZ(cz);
		World world = chunk.getWorld();
		boolean sides[] = getPipeAttachedSides(chunk, block, x, y, z);
		boolean isPipe = block instanceof BlockPipe;
		Renderer renderer = renderers[1];
		Color color;
		float bot = 0.3125F;
		if (block instanceof IBlockJoinable) {
			bot = ((IBlockJoinable) block).getDistanceSideToBlock(data);
		}
		float top = 1 - bot;
		float mix = sides[2] ? 0 : bot;
		float miy = sides[5] ? 0 : bot;
		float miz = sides[3] ? 0 : bot;
		float max = sides[0] ? 1 : top;
		float may = sides[4] ? 1 : top;
		float maz = sides[1] ? 1 : top;

		int addToTex = isPipe ? 16 : 0;

		int onlySide = -1;
		for (int dofor = 0; dofor < 6; ++dofor) {
			if (sides[dofor]) {
				if (onlySide == -1) onlySide = dofor;
				else {
					onlySide = -1;
					break;
				}
			}
		}

		IBlockMultitexture mt = null;
		if (block instanceof IBlockMultitexture) mt = (IBlockMultitexture) block;
		TextureCoords coords;
		renderer.useQuadInputMode(true);
		Vector3f light = world.getGenerator().getLightColor(world, m, x, y, z, cx, cz, chunk.getBlockLight(cx, cy, cz));
		renderer.light(light, world.hasSkyLight(x, y, z));
		/* Side 5 */{
			int tex = block.getTextureIndex(world, x, y, z, 5) + addToTex;
			if (isPipe && (sides[5] || onlySide == 4)) tex -= 1;
			color = block.getColor(world, x, y, z, 5);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_0(tex, max, mix, miz, maz);
				renderer.color(color);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + bot, z + miz);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + bot, z + maz);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + bot, z + maz);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + bot, z + miz);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 5)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 5);
					color = mt.getColorMultitexture(world, x, y, z, 5);
				} else break;
			}
		}
		/* Side 4 */{
			int tex = block.getTextureIndex(world, x, y, z, 4) + addToTex;
			if (isPipe && (sides[4] || onlySide == 5)) tex -= 1;
			color = block.getColor(world, x, y, z, 4);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_0(tex, max, mix, maz, miz);
				renderer.color(color);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + top, z + maz);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + top, z + miz);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + top, z + miz);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + top, z + maz);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 4)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 4);
					color = mt.getColorMultitexture(world, x, y, z, 4);
				} else break;
			}
		}
		/* Side 0 */{
			int tex = block.getTextureIndex(world, x, y, z, 0) + addToTex;
			if (isPipe && (sides[0] || onlySide == 2)) tex -= 1;
			color = block.getColor(world, x, y, z, 0);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_0(tex, may, miy, miz, maz);
				renderer.color(color);
				renderer.tex(coords.next());
				renderer.vertex(x + top, y + may, z + miz);
				renderer.tex(coords.next());
				renderer.vertex(x + top, y + may, z + maz);
				renderer.tex(coords.next());
				renderer.vertex(x + top, y + miy, z + maz);
				renderer.tex(coords.next());
				renderer.vertex(x + top, y + miy, z + miz);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 0)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 0);
					color = mt.getColorMultitexture(world, x, y, z, 0);
				} else break;
			}
		}
		/* Side 1 */{
			int tex = block.getTextureIndex(world, x, y, z, 1) + addToTex;
			if (isPipe && (sides[1] || onlySide == 3)) tex -= 1;
			color = block.getColor(world, x, y, z, 1);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_1(tex, max, mix, may, miy);
				renderer.color(color);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + may, z + top);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + may, z + top);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + miy, z + top);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + miy, z + top);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 1)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 1);
					color = mt.getColorMultitexture(world, x, y, z, 1);
				} else break;
			}
		}
		/* Side 2 */{
			int tex = block.getTextureIndex(world, x, y, z, 2) + addToTex;
			if (isPipe && (sides[2] || onlySide == 0)) tex -= 1;
			color = block.getColor(world, x, y, z, 2);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_0(tex, may, miy, maz, miz);
				renderer.color(color);
				renderer.tex(may, maz, tex);
				renderer.vertex(x + bot, y + may, z + maz);
				renderer.tex(may, miz, tex);
				renderer.vertex(x + bot, y + may, z + miz);
				renderer.tex(miy, miz, tex);
				renderer.vertex(x + bot, y + miy, z + miz);
				renderer.tex(miy, maz, tex);
				renderer.vertex(x + bot, y + miy, z + maz);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 2)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 2);
					color = mt.getColorMultitexture(world, x, y, z, 2);
				} else break;
			}
		}
		/* Side 3 */{
			int tex = block.getTextureIndex(world, x, y, z, 3) + addToTex;
			if (isPipe && (sides[3] || onlySide == 1)) tex -= 1;
			color = block.getColor(world, x, y, z, 3);
			// renderer.customAttrib(block.uid, data, 5, 5);
			for (int df = 0; df < 2; ++df) {
				coords = TextureCoords.pipeTex_1(tex, mix, max, may, miy);
				renderer.color(color);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + may, z + bot);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + may, z + bot);
				renderer.tex(coords.next());
				renderer.vertex(x + max, y + miy, z + bot);
				renderer.tex(coords.next());
				renderer.vertex(x + mix, y + miy, z + bot);
				if (df == 0 && mt != null && mt.renderSideMultitexture(world, x, y, z, 3)) {
					tex = mt.getTextureIndexMultitexture(world, x, y, z, 3);
					color = mt.getColorMultitexture(world, x, y, z, 3);
				} else break;
			}
		}
	}

	public boolean[] getPipeAttachedSides(Chunk chunk, Block block, long x, long y, long z) {
		boolean[] result = new boolean[6];
		IBlockJoinable j = (IBlockJoinable) block;
		for (int dofor = 0; dofor < 6; ++dofor) {
			result[dofor] = j.joinTo(chunk.getWorld(), x, y, z, dofor);
		}
		return result;
	}

	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
	}

	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount) {
	}
}
