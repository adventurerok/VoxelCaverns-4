package vc4.api.render;

import vc4.api.Resources;
import vc4.api.client.ClientWindow;
import vc4.api.font.FontRenderer;
import vc4.api.font.RenderedText;
import vc4.api.graphics.*;
import vc4.api.gui.OverlayRender;
import vc4.api.gui.OverlayRenderer;
import vc4.api.input.Input;
import vc4.api.input.Mouse;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;

import java.util.HashMap;
import java.util.LinkedList;

public class ItemTooltipRenderer {

	private static int CACHE_SIZE = 128;
	private static LinkedList<CachedTooltip> CACHED_LIST = new LinkedList<>();
	private static HashMap<TooltipData, CachedTooltip> CACHED_MAP = new HashMap<>();


	private static class CachedTooltip {
		int itemVbo;
		int itemLength;
		int width;
		int height;
		boolean block;
		RenderedText firstLine;
		RenderedText description;


		public void draw(int mx, int my){
			int wid = ClientWindow.getClientWindow().getWidth();
			int hei = ClientWindow.getClientWindow().getHeight();
			if(mx + width + 15 > wid) mx = wid - width - 15;
			if(my + height + 15 > hei) my = hei - height - 15;

			gl.translate(mx, my);
			gl.bindShader("texture");

			if(block){
				Resources.getAnimatedTexture("blocks").bind();
			} else Resources.getAnimatedTexture("items").bind();

			VertexBufferRenderer.render(gl, itemVbo, GLPrimitive.TRIANGLES, 0, itemLength);

			firstLine.draw();
			if(description != null) description.draw();
			gl.translate(-mx, -my);
		}
	}

	private static class TooltipData {
		ItemStack stack;
		ToolTipType type;
		int slot;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			TooltipData that = (TooltipData) o;

			if (slot != that.slot) return false;
			if (stack != null ? !stack.equals(that.stack) : that.stack != null) return false;
			if (type != that.type) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = stack != null ? stack.hashCode() : 0;
			result = 31 * result + (type != null ? type.hashCode() : 0);
			result = 31 * result + slot;
			return result;
		}
	}

	private static OpenGL gl = Graphics.getOpenGL();
	public static FontRenderer renderer = FontRenderer.createFontRenderer("unispaced_14", 14);

	public static void renderTooltip(ItemStack item, ToolTipType type) {
		if (type == ToolTipType.ARMOUR) throw new IllegalArgumentException("Must specify slot");
		renderTooltip(item, type, 0);
	}

	public static void renderTooltip(final ItemStack item, ToolTipType type, int slot) {
		TooltipData data = new TooltipData();
		data.stack = item;
		data.type = type;
		data.slot = 0; //only change if tooltip affected by slot, which it is never yet

		final CachedTooltip previous = CACHED_MAP.get(data);
		if(previous != null){
			OverlayRenderer.addRender(new OverlayRender() {

				@Override
				public void draw() {
					Mouse mouse = Input.getClientMouse();
					previous.draw(mouse.getX(), mouse.getY());

				}
			});

			return;
		}

		String text = null;
		switch (type) {
		// case ARMOUR:
		// text = item.getArmourSlotText(slot);
		// break;
			case CREATIVE:
				text = item.getCreativeTooltipText();
				break;
			case INVENTORY:
				text = item.getTooltipText();
				break;
		}

		final CachedTooltip tip = createTooltip(item, text);

		if(CACHED_LIST.size() > CACHE_SIZE){
			CachedTooltip dead = CACHED_LIST.removeFirst();
			CACHED_MAP.keySet().remove(dead);
		}

		CACHED_LIST.add(tip);
		CACHED_MAP.put(data, tip);

		OverlayRenderer.addRender(new OverlayRender() {


			@Override
			public void draw() {
				Mouse mouse = Input.getClientMouse();
				tip.draw(mouse.getX(), mouse.getY());

			}
		});

	}

	private static CachedTooltip createTooltip(ItemStack item, String text){
		CachedTooltip output = new CachedTooltip();

		int pos = text.indexOf("\n");
		String firstLine, description = null;
		if (pos == -1) {
			firstLine = text;
		} else {
			firstLine = text.substring(0, pos);
			description = text.substring(pos);
		}
		if (description != null && description.trim().isEmpty()) description = null;
		int width = 0, height = 36;
		if (description != null) {
			int lWidth = MathUtils.ceil(renderer.measureString(firstLine, 14).x) + 32;
			int lWidthL = MathUtils.ceil(renderer.measureString(description, 14).x);
			width += Math.max(lWidth, lWidthL) + 3;
			height += MathUtils.ceil(renderer.measureString(description, 14).y) + 3;
		} else {
			width += MathUtils.ceil(renderer.measureString(firstLine, 14).x) + 32;
		}


		int mx = 0;
		int my = 0;

		int wid = ClientWindow.getClientWindow().getWidth();
		int hei = ClientWindow.getClientWindow().getHeight();
		int minusX = Math.max(0, (mx + width + 13) - wid);
		int minusY = Math.max(0, (my + height + 3) - hei);

		VertexBufferRenderer render = new VertexBufferRenderer();
		render.useQuadInputMode(true);

		render.color(0.5f, 0.5f, 0.5f, 0.75f);

		render.tex(0, 0, 1); //1 is index of white square in all AnimatedTextures that have "prefix" enabled
		render.vertex(mx + 6 - minusX, my - minusY + 12, 0);
		render.vertex(mx + 6 - minusX + width + 6, my - minusY + 12, 0);
		render.vertex(mx + 6 - minusX + width + 6, my - minusY + height + 12, 0);
		render.vertex(mx + 6 - minusX, my - minusY + height + 12, 0);

		ItemRenderer.setRenderItemBig();
		if(item.isBlock()){
			ItemBlockRenderer.renderJustBlock(item.clone().setAmount(1), mx + 6 - minusX + 9, my - minusY + 12 + 9, render);
		} else {
			ItemRenderer.renderJustItem(item.clone().setAmount(1), mx + 6 - minusX + 9, my - minusY + 12 + 9, render);
		}
		ItemRenderer.setRenderItemSmall();

		render.compile();
		output.block = item.isBlock();
		output.itemVbo = render.getBufferId();
		output.itemLength = render.getVertexCount();
		output.width = width;
		output.height = height;

		RenderedText rFirstLine = renderer.renderString(mx + 6 + 6 - minusX + 32, my + 12 - minusY, firstLine);
		output.firstLine = rFirstLine;

		if (description != null) {
			RenderedText rDescription = renderer.renderString(mx + 6 + 6 - minusX, my + 12 - minusY + 22, description);
			output.description = rDescription;
		}
		renderer.resetStyles();

		return output;
	}
}
