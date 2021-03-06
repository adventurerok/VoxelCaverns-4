package vc4.api.render;

import java.awt.Color;

import vc4.api.Resources;
import vc4.api.block.Block;
import vc4.api.block.BlockMultitexture;
import vc4.api.block.IBlockMultitexture;
import vc4.api.graphics.*;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.vector.Vector2f;

public class ItemBlockRenderer {

	private static OpenGL gl;
	private static ItemStack current;
	private static Vector2f position;

	private static float minX, minY, minZ, maxX, maxY, maxZ;
	private static float tix, tiy, tiz, tax, tay, taz;

	public static void renderItemBlock(ItemStack stack, int x, int y) {
		gl = Graphics.getOpenGL();
		current = stack;
		position = new Vector2f(x, y);
		if (stack == null || !stack.exists() || !stack.isBlock()) return;

		if (Block.byId(stack.getId()).render3d(stack.getData())) {
			renderBlock3d();
		} else {
			renderBlock2d();
		}
	}

	public static void renderJustBlock(ItemStack stack, int x, int y, Renderer render){
		gl = Graphics.getOpenGL();
		current = stack;
		position = new Vector2f(x, y);
		if (stack == null || !stack.exists() || !stack.isBlock()) return;

		if (Block.byId(stack.getId()).render3d(stack.getData())) {
			renderBlock3d(render);
		} else {
			renderBlock2d(render);
		}

	}

	protected static void renderBlock2d() {
		int texInd = Block.byId(current.getId()).getTextureIndex(current, 0);
		Resources.getAnimatedTexture("blocks").bind();

		gl.begin(GLPrimitive.QUADS);
		Color blockColor = Block.byId(current.getId()).getColor(current, 0);
		gl.color(blockColor.getRed() / 255F, blockColor.getGreen() / 255F, blockColor.getBlue() / 255F);
		gl.texCoord(0, 0, texInd);
		gl.vertex(position.x, position.y);
		gl.texCoord(1, 0, texInd);
		gl.vertex(position.x + 24, position.y);
		gl.texCoord(1, 1, texInd);
		gl.vertex(position.x + 24, position.y + 24);
		gl.texCoord(0, 1, texInd);
		gl.vertex(position.x, position.y + 24);
		gl.end();

		if (Block.byId(current.getId()) instanceof IBlockMultitexture && ((IBlockMultitexture) Block.byId(current.getId())).multitextureUsed(current.getData(), 0)) {
			int texInd2 = ((IBlockMultitexture) Block.byId(current.getId())).getTextureIndexMultitexture(current, 0);
			Color color = ((IBlockMultitexture) Block.byId(current.getId())).getColorMultitexture(current, 0);

			gl.begin(GLPrimitive.QUADS);
			gl.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			gl.texCoord(0, 0, texInd2);
			gl.vertex(position.x, position.y);
			gl.texCoord(1, 0, texInd2);
			gl.vertex(position.x + 24, position.y);
			gl.texCoord(1, 1, texInd2);
			gl.vertex(position.x + 24, position.y + 24);
			gl.texCoord(0, 1, texInd2);
			gl.vertex(position.x, position.y + 24);
			gl.end();
		}
	}

	protected static void renderBlock2d(Renderer render) {
		int texInd = Block.byId(current.getId()).getTextureIndex(current, 0);
		//Resources.getAnimatedTexture("blocks").bind();

		//gl.begin(GLPrimitive.QUADS);
		Color blockColor = Block.byId(current.getId()).getColor(current, 0);
		render.color(blockColor.getRed() / 255F, blockColor.getGreen() / 255F, blockColor.getBlue() / 255F, 1F);
		render.tex(0, 0, texInd);
		render.vertex(position.x, position.y, 0);
		render.tex(1, 0, texInd);
		render.vertex(position.x + 24, position.y, 0);
		render.tex(1, 1, texInd);
		render.vertex(position.x + 24, position.y + 24, 0);
		render.tex(0, 1, texInd);
		render.vertex(position.x, position.y + 24, 0);


		if (Block.byId(current.getId()) instanceof IBlockMultitexture && ((IBlockMultitexture) Block.byId(current.getId())).multitextureUsed(current.getData(), 0)) {
			int texInd2 = ((IBlockMultitexture) Block.byId(current.getId())).getTextureIndexMultitexture(current, 0);
			Color color = ((IBlockMultitexture) Block.byId(current.getId())).getColorMultitexture(current, 0);

			//gl.begin(GLPrimitive.QUADS);
			render.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
			render.tex(0, 0, texInd2);
			render.vertex(position.x, position.y, 0);
			render.tex(1, 0, texInd2);
			render.vertex(position.x + 24, position.y, 0);
			render.tex(1, 1, texInd2);
			render.vertex(position.x + 24, position.y + 24, 0);
			render.tex(0, 1, texInd2);
			render.vertex(position.x, position.y + 24, 0);
			//gl.end();
		}
	}

	protected static void renderBlock3d() {
		AABB bounds = Block.byId(current.getId()).getRenderSize(current);

		minX = (float) ((bounds.minX - 0.5F) * 16);
		minY = (float) (((bounds.minY) * 16) - 16F);
		minZ = (float) ((bounds.minZ - 0.5F) * 16);
		maxX = (float) ((bounds.maxX - 0.5F) * 16);
		maxY = (float) (((bounds.maxY) * 16) - 16F);
		maxZ = (float) ((bounds.maxZ - 0.5F) * 16);



		gl.pushMatrix();
		gl.translate(position.x + 12, position.y + 4, 0);
		gl.rotate(60, 1, 0, 0);
		gl.rotate(45, 0, 0, 1);
		Resources.getAnimatedTexture("blocks").bind();
		renderBlock3dTop();
		renderBlock3dLeft();
		renderBlock3dRight();
		gl.popMatrix();
	}

	private static final float LEFT_X = 8f/150f;
	private static final float SIDE_TOP_Y = 34f/150f;
	private static final float MIDDLE_X = 0.5f;
	private static final float TOP_Y = 2f/150f;
	private static final float RIGHT_X = 142f/150f;
	private static final float MIDDLE_Y = 55f/150f;
	private static final float SIDE_BOTTOM_Y = 116f/150f;
	private static final float BOTTOM_Y = 148f/150f;

	protected static void renderBlock3d(Renderer render) {
		AABB bounds = Block.byId(current.getId()).getRenderSize(current);

		tix = (float) bounds.minX;
		tiy = (float) bounds.minY;
		tiz = (float) bounds.minZ;
		tax = (float) bounds.maxX;
		tay = (float) bounds.maxY;
		taz = (float) bounds.maxZ;

		float x = position.x;
		float y = position.y;
		float size = 32;

		float leftX = x + LEFT_X * size;
		float sideTopY = y + SIDE_TOP_Y * size;
		float middleX = x + MIDDLE_X * size;
		float topY = y + TOP_Y * size;
		float rightX = x + RIGHT_X * size;
		float middleY = y + MIDDLE_Y * size;
		float sideBottomY = y + SIDE_BOTTOM_Y * size;
		float bottomY = y + BOTTOM_Y * size;
		float topZ = 34f/150f * 2f * size;
		float middleZ = 34f/150f * size;
		float bottomZ = 0;

		Block block = Block.byId(current.getId());
		int texInd;
		Color color;


		//TOP FACE
		texInd = block.getTextureIndex(current, 4);
		color = block.getColor(current, 4);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(leftX, sideTopY, middleZ);

		render.tex(1, 0, texInd);
		render.vertex(middleX, topY, bottomZ);

		render.tex(1, 1, texInd);
		render.vertex(rightX, sideTopY, middleZ);

		render.tex(0, 1, texInd);
		render.vertex(middleX, middleY, topZ);

		//EAST FACE
		texInd = block.getTextureIndex(current, 1);
		color = block.getColor(current, 1);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(leftX, sideTopY, middleZ);

		render.tex(1, 0, texInd);
		render.vertex(middleX, middleY, topZ);

		render.tex(1, 1, texInd);
		render.vertex(middleX, bottomY, topZ);

		render.tex(0, 1, texInd);
		render.vertex(leftX, sideBottomY, middleZ);

		//NORTH FACE
		texInd = block.getTextureIndex(current, 0);
		color = block.getColor(current, 0);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(middleX, middleY, topZ);

		render.tex(1, 0, texInd);
		render.vertex(rightX, sideTopY, middleZ);

		render.tex(1, 1, texInd);
		render.vertex(rightX, sideBottomY, middleZ);

		render.tex(0, 1, texInd);
		render.vertex(middleX, bottomY, topZ);

		if(!(block instanceof IBlockMultitexture)) return;
		IBlockMultitexture mult = (IBlockMultitexture) block;

		//TOP FACE
		texInd = mult.getTextureIndexMultitexture(current, 4);
		color = mult.getColorMultitexture(current, 4);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(leftX, sideTopY, middleZ);

		render.tex(1, 0, texInd);
		render.vertex(middleX, topY, bottomZ);

		render.tex(1, 1, texInd);
		render.vertex(rightX, sideTopY, middleZ);

		render.tex(0, 1, texInd);
		render.vertex(middleX, middleY, topZ);

		//EAST FACE
		texInd = mult.getTextureIndexMultitexture(current, 1);
		color = mult.getColorMultitexture(current, 1);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(leftX, sideTopY, middleZ);

		render.tex(1, 0, texInd);
		render.vertex(middleX, middleY, topZ);

		render.tex(1, 1, texInd);
		render.vertex(middleX, bottomY, topZ);

		render.tex(0, 1, texInd);
		render.vertex(leftX, sideBottomY, middleZ);

		//NORTH FACE
		texInd = mult.getTextureIndexMultitexture(current, 0);
		color = mult.getColorMultitexture(current, 0);
		render.color(color);

		render.tex(0, 0, texInd);
		render.vertex(middleX, middleY, topZ);

		render.tex(1, 0, texInd);
		render.vertex(rightX, sideTopY, middleZ);

		render.tex(1, 1, texInd);
		render.vertex(rightX, sideBottomY, middleZ);

		render.tex(0, 1, texInd);
		render.vertex(middleX, bottomY, topZ);

	}

	protected static void renderBlock3dTop() {
		int texInd = Block.byId(current.getId()).getTextureIndex(current, 4);
		gl.begin(GLPrimitive.QUADS);
		Color blockColor = Block.byId(current.getId()).getColor(current, 4);
		gl.color(blockColor.getRed() / 255F, blockColor.getGreen() / 255F, blockColor.getBlue() / 255F);
		gl.texCoord(tix, tiz, texInd);
		gl.vertex(minX, minZ, maxY);
		gl.texCoord(tax, tiz, texInd);
		gl.vertex(maxX, minZ, maxY);
		gl.texCoord(tax, taz, texInd);
		gl.vertex(maxX, maxZ, maxY);
		gl.texCoord(tix, taz, texInd);
		gl.vertex(minX, maxZ, maxY);
		gl.end();

		if (Block.byId(current.getId()) instanceof IBlockMultitexture && ((IBlockMultitexture) Block.byId(current.getId())).multitextureUsed(current.getData(), 4)) {

			int texInd2 = ((IBlockMultitexture) Block.byId(current.getId())).getTextureIndexMultitexture(current, 4);
			float tsx2 = tix;
			float tsy2 = tiz;
			float tex2 = tax;
			float tey2 = taz;

			Color color = ((IBlockMultitexture) Block.byId(current.getId())).getColorMultitexture(current, 4);

			gl.begin(GLPrimitive.QUADS);
			gl.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			gl.texCoord(tsx2, tsy2, texInd2);
			gl.vertex(minX, minZ, maxY);
			gl.texCoord(tex2, tsy2, texInd2);
			gl.vertex(maxX, minZ, maxY);
			gl.texCoord(tex2, tey2, texInd2);
			gl.vertex(maxX, maxZ, maxY);
			gl.texCoord(tsx2, tey2, texInd2);
			gl.vertex(minX, maxZ, maxY);
			gl.end();
		}
	}

	protected static void renderBlock3dRight() {
		int texInd = Block.byId(current.getId()).getTextureIndex(current, 3);
		float tsx = tiz;
		float tsy = tiy;
		float tex = taz;
		float tey = tay;

		gl.begin(GLPrimitive.QUADS);
		Color blockColor = Block.byId(current.getId()).getColor(current, 3);
		gl.color(blockColor.getRed() / 255F, blockColor.getGreen() / 255F, blockColor.getBlue() / 255F);
		gl.texCoord(tsx, tsy, texInd);
		gl.vertex(maxX, maxZ, maxY);
		gl.texCoord(tex, tsy, texInd);
		gl.vertex(maxX, minZ, maxY);
		gl.texCoord(tex, tey, texInd);
		gl.vertex(maxX, minZ, minY);
		gl.texCoord(tsx, tey, texInd);
		gl.vertex(maxX, maxZ, minY);
		gl.end();

		if (Block.byId(current.getId()) instanceof IBlockMultitexture && ((IBlockMultitexture) Block.byId(current.getId())).multitextureUsed(current.getData(), 3)) {
			int texInd2 = ((IBlockMultitexture) Block.byId(current.getId())).getTextureIndexMultitexture(current, 3);
			float tsx2 = tiz;
			float tsy2 = tiy;
			float tex2 = taz;
			float tey2 = tay;
			Color color = ((IBlockMultitexture) Block.byId(current.getId())).getColorMultitexture(current, 3);

			gl.begin(GLPrimitive.QUADS);
			gl.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			gl.texCoord(tsx2, tsy2, texInd2);
			gl.vertex(maxX, maxZ, maxY);
			gl.texCoord(tex2, tsy2, texInd2);
			gl.vertex(maxX, minZ, maxY);
			gl.texCoord(tex2, tey2, texInd2);
			gl.vertex(maxX, minZ, minY);
			gl.texCoord(tsx2, tey2, texInd2);
			gl.vertex(maxX, maxZ, minY);
			gl.end();
		}
	}

	protected static void renderBlock3dLeft() {
		int texInd = Block.byId(current.getId()).getTextureIndex(current, 0);
		float tsx = tix;
		float tsy = tiy;
		float tex = tax;
		float tey = tay;

		gl.begin(GLPrimitive.QUADS);
		Color blockColor = Block.byId(current.getId()).getColor(current, 0);
		gl.color(blockColor.getRed() / 255F, blockColor.getGreen() / 255F, blockColor.getBlue() / 255F);
		gl.texCoord(tsx, tsy, texInd);
		gl.vertex(minX, maxZ, maxY);
		gl.texCoord(tex, tsy, texInd);
		gl.vertex(maxX, maxZ, maxY);
		gl.texCoord(tex, tey, texInd);
		gl.vertex(maxX, maxZ, minY);
		gl.texCoord(tsx, tey, texInd);
		gl.vertex(minX, maxZ, minY);
		gl.end();

		if (Block.byId(current.getId()) instanceof IBlockMultitexture && ((IBlockMultitexture) Block.byId(current.getId())).multitextureUsed(current.getData(), 0)) {
			int texInd2 = ((IBlockMultitexture) Block.byId(current.getId())).getTextureIndexMultitexture(current, 0);
			float tsx2 = tix;
			float tsy2 = tiy;
			float tex2 = tax;
			float tey2 = tay;

			Color color = ((IBlockMultitexture) Block.byId(current.getId())).getColorMultitexture(current, 0);

			gl.begin(GLPrimitive.QUADS);
			gl.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			gl.texCoord(tsx2, tsy2, texInd2);
			gl.vertex(minX, maxZ, maxY);
			gl.texCoord(tex2, tsy2, texInd2);
			gl.vertex(maxX, maxZ, maxY);
			gl.texCoord(tex2, tey2, texInd2);
			gl.vertex(maxX, maxZ, minY);
			gl.texCoord(tsx2, tey2, texInd2);
			gl.vertex(minX, maxZ, minY);
			gl.end();
		}
	}
}
