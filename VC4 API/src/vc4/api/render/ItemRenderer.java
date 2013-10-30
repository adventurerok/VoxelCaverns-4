package vc4.api.render;

import java.awt.Color;

import vc4.api.Resources;
import vc4.api.block.Block;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.item.*;
import vc4.api.math.MathUtils;
import vc4.api.util.ColorUtils;
import vc4.api.vector.Vector2f;


public class ItemRenderer {

	static OpenGL gl;
	
	public static FontRenderer font = FontRenderer.createFontRenderer("unispaced_14", 14);
	//public static FontRenderer infinite = FontRenderer.create(new Font("arial", 0, 20));

	private static ItemStack current;
	private static Vector2f position;

	private static boolean big = false;

	public static void setRenderItemBig(){
		big = true;
	}

	public static void setRenderItemSmall(){
		big = false;
	}

	/**
	 * @param stack
	 * @param x
	 * @param y
	 */
	public static void renderItemStack(ItemStack stack, int x, int y){
		if(stack == null) return;
		if(stack.getId() == 0) return;
		if(!stack.exists()) return;
		if(stack.getItem() == null){
			stack.setId(0);
			stack.setAmount(0);
			stack.setDamage(0);
			return;
		}
		gl = Graphics.getOpenGL();
		current = stack;
		position = new Vector2f(x, y);
		Graphics.getClientShaderManager().bindShader("texture");
		if(stack.isBlock()){
			renderBlock();
		} else {
			renderItem();
			if(!big)renderDurabiliy();
		}
		displayAmount();
	}
	private static void renderBlock(){
		ItemBlockRenderer.renderItemBlock(current, (int)position.x - 4, (int)position.y - 4);
	}
	private static void renderItem(){
		int tex = current.getItem().getTextureIndex(current);

		Resources.getAnimatedTexture("items").bind();

		gl.begin(GLPrimative.QUADS);

		Color color = current.getItem().getColor(current);
		gl.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
		if(big){
			gl.texCoord(0, 0, tex);
			gl.vertex(position.x - 8, position.y - 8);
			gl.texCoord(1, 0, tex);

			gl.vertex(position.x + 24, position.y - 8);
			gl.texCoord(1, 1, tex);

			gl.vertex(position.x + 24, position.y + 24);
			gl.texCoord(0, 1, tex);

			gl.vertex(position.x - 8, position.y + 24);
		} else {
			gl.texCoord(0, 0, tex);
			gl.vertex(position.x, position.y);
			gl.texCoord(1, 0, tex);

			gl.vertex(position.x + 16, position.y);
			gl.texCoord(1, 1, tex);

			gl.vertex(position.x + 16, position.y + 16);
			gl.texCoord(0, 1, tex);

			gl.vertex(position.x, position.y + 16);
		}
		gl.end();

		if(current.getItem() instanceof IItemMultitexture){
			int tex2 = ((IItemMultitexture)current.getItem()).getMultitextureTextureIndex(current);
			gl.begin(GLPrimative.QUADS);

			Color color2 = ((IItemMultitexture)current.getItem()).getMultitextureColor(current);
			gl.color(color2.getRed() / 255F, color2.getGreen() / 255F, color2.getBlue() / 255F);
			if(big){
				gl.texCoord(0, 0, tex2);
				gl.vertex(position.x - 8, position.y - 8);
				gl.texCoord(1, 0, tex2);

				gl.vertex(position.x + 24, position.y - 8);
				gl.texCoord(1, 1, tex2);

				gl.vertex(position.x + 24, position.y + 24);
				gl.texCoord(0, 1, tex2);

				gl.vertex(position.x - 8, position.y + 24);
			} else {
				gl.texCoord(0, 0, tex2);
				gl.vertex(position.x, position.y);
				gl.texCoord(1, 0, tex2);

				gl.vertex(position.x + 16, position.y);
				gl.texCoord(1, 1, tex2);

				gl.vertex(position.x + 16, position.y + 16);
				gl.texCoord(0, 1, tex2);

				gl.vertex(position.x, position.y + 16);
			}
			gl.end();
		}
	}
	public static String intToDisplayAmount(int amount){
		if(amount > 999999){
			return (amount / 1000000) + "M";
		} else if(amount > 9999){
			return (amount / 1000) + "K";
		} else {
			return amount + "";
		}

	}
	public static int displayAmountToInt(String amount){
		if(amount.endsWith("B")){
			return Integer.parseInt(amount.substring(0, amount.length() - 1)) * 1000000000;
		} else if(amount.endsWith("M")){
			return Integer.parseInt(amount.substring(0, amount.length() - 1)) * 1000000;
		} else if(amount.endsWith("K")){
			return Integer.parseInt(amount.substring(0, amount.length() - 1)) * 1000;
		} else {
			return Integer.parseInt(amount);
		}
	}
	private static void displayAmount(){
		displayAmountSurvival();
	}
	private static void displayAmountSurvival(){
		if(current.getAmount() == -1){
			String text = "\u221E";
			float length = font.measureString(text, 14).x;
			font.renderString(position.x + 24 - length, position.y + 6, text);
			font.resetStyles();
			return;
		}
		if(current.getAmount() < 2) return;
		String text = intToDisplayAmount(current.getAmount());
		float length = font.measureString(text, 14).x;
		font.renderString(position.x + 28 - length, position.y + 10, text);
		font.resetStyles();
	}

	private static void renderDurabiliy(){
		if(!current.getItem().isDamagedOnUse()) return;
		if(current.getDamage() == 0) return;
		int damage = current.getDamage();
		int max = current.getItem().getMaxDamage();
		double d = 1D - ((double)damage / (double)max);
		int width = (int) (1 + MathUtils.floor(d * 16D));
		Color c = ColorUtils.differColors(Color.red, Color.green, (float)d);
		Graphics.getClientShaderManager().unbindShader();
		gl.lineWidth(2);
		gl.begin(GLPrimative.LINES);
		gl.color(c);
		gl.vertex(position.x, position.y + 17);
		gl.vertex(position.x + width, position.y + 17);
		gl.end();
		gl.lineWidth(1);
		Graphics.getClientShaderManager().bindShader("texture");
	}

	/**
	 * @param stack The ItemStack to render
	 * @param f The X position to render at
	 * @param g The Y position to render at
	 */
	public static void renderItemStack(ItemStack stack, float f, float g) {
		renderItemStack(stack, (int)f, (int)g);

	}
	
	public static void renderItem3D(ItemStack stack, float x, float y, float z){
		if(gl == null) gl =  Graphics.getOpenGL();
		if(stack.isBlock() && Block.byId(stack.getId()).render3d(stack.getData())){
			Resources.getSheetTexture("blocks").bind();
			Renderer d = new DataRenderer();
			Block.byId(stack.getId()).getRenderer().renderBlock(stack, x - 0.5f, y - 0.5f, z - 0.5f, d);
			d.compile();
			d.render();
			return;
		}
		Item s = stack.getItem();
		if(stack.isBlock()) Resources.getAnimatedTexture("blocks").bind();
		else Resources.getAnimatedTexture("items").bind();
		int tex = s.getTextureIndex(stack);
        gl.color(s.getColor(stack));
        renderItemWithThickness(x, y, z, tex);
        if(s instanceof IItemMultitexture){
        	tex = ((IItemMultitexture)s).getMultitextureTextureIndex(stack);
            gl.color(((IItemMultitexture)s).getMultitextureColor(stack));
            renderItemWithThickness(x, y, z, tex);
        }
	}
	
	protected static void renderItemWithThickness(float x, float y, float z, float tid)
    {
		x -= 0.5F;
		y -= 0.5F;
		z += 0.03125F;
        float f = x + 1.0F;
        float f1 = 0.0625F;
        float smn = 0F;
        //float sector = 1 / 32F;
        gl.begin(GLPrimative.QUADS);
        gl.vertexWithTexture(x, y, z, 0, 1, tid);
        gl.vertexWithTexture(f, y, z, 1, 1, tid);
        gl.vertexWithTexture(f, y + 1F, z, 1, 0, tid);
        gl.vertexWithTexture(x, y + 1F, z, 0, 0, tid);
        gl.vertexWithTexture(x, y + 1, z - f1, 0, 0, tid);
        gl.vertexWithTexture(f, y + 1, z - f1, 1, 0, tid);
        gl.vertexWithTexture(f, y, z - f1, 1, 1, tid);
        gl.vertexWithTexture(x, y, z - f1, 0, 1, tid);

        for (int i = 0; i < 32; i++)
        {
            float f2 = i / 32F;
            float f6 = (0 + (1 - 0) * f2) - smn;
            float f10 = 1 * f2;
            gl.vertexWithTexture(x + f10, y, z - f1, f6, 1, tid);
            gl.vertexWithTexture(x + f10, y, z, f6, 1, tid);
            gl.vertexWithTexture(x + f10, y + 1, z, f6, 0, tid);
            gl.vertexWithTexture(x + f10, y + 1, z - f1, f6, 0, tid);
        }


        for (int j = 0; j < 32; j++)
        {
            float f3 = j / 32F;
            float f7 = (0 + (1 - 0) * f3) - smn;
            float f11 = 1 * f3;
            gl.vertexWithTexture(x + f11, y + 1, z - f1, f7, 0, tid);
            gl.vertexWithTexture(x + f11, y + 1, z, f7, 0, tid);
            gl.vertexWithTexture(x + f11, y, z, f7, 1, tid);
            gl.vertexWithTexture(x + f11, y, z - f1, f7, 1, tid);
        }


        for (int k = 0; k < 32; k++)
        {
            float f4 = k / 32F;
            float f8 = 1 - ((0 + (1 - 0) * f4) - smn);
            float f12 = 1 * f4;
            gl.vertexWithTexture(x, y + f12, z, 0, f8, tid);
            gl.vertexWithTexture(f, y + f12, z, 1, f8, tid);
            gl.vertexWithTexture(f, y + f12, z - f1, 1, f8, tid);
            gl.vertexWithTexture(x, y + f12, z - f1, 0, f8, tid);
        }


        for (int l = 0; l < 32; l++)
        {
            float f5 = l / 32F;
            float f9 = 1 - ((0 + (1 - 0) * f5) - smn);
            float f13 = 1 * f5;
            gl.vertexWithTexture(f, y + f13, z, 1, f9, tid);
            gl.vertexWithTexture(x, y + f13, z, 0, f9, tid);
            gl.vertexWithTexture(x, y + f13, z - f1, 0, f9, tid);
            gl.vertexWithTexture(f, y + f13, z - f1, 1, f9, tid);
        }
        gl.end();

    }


}
