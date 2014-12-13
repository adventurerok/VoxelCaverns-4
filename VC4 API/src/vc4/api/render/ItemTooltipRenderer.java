package vc4.api.render;

import vc4.api.client.ClientWindow;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.OverlayRender;
import vc4.api.gui.OverlayRenderer;
import vc4.api.input.Input;
import vc4.api.input.Mouse;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;

public class ItemTooltipRenderer {

	private static OpenGL gl = Graphics.getOpenGL();
	public static FontRenderer renderer = FontRenderer.createFontRenderer("unispaced_14", 14);

	public static void renderTooltip(ItemStack item, ToolTipType type) {
		if (type == ToolTipType.ARMOUR) throw new IllegalArgumentException("Must specify slot");
		renderTooltip(item, type, 0);
	}

	public static void renderTooltip(final ItemStack item, ToolTipType type, int slot) {
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
			int lwidth = MathUtils.ceil(renderer.measureString(firstLine, 14).x) + 32;
			int lwidth1 = MathUtils.ceil(renderer.measureString(description, 14).x);
			width += Math.max(lwidth, lwidth1) + 3;
			height += MathUtils.ceil(renderer.measureString(description, 14).y) + 3;
		} else {
			width += MathUtils.ceil(renderer.measureString(firstLine, 14).x) + 32;
		}

		final int fWidth = width;
		final int fHeight = height;
		final String fFirstLine = firstLine;
		final String fDescription = description;

		OverlayRenderer.addRender(new OverlayRender() {

			@Override
			public void draw() {
				Mouse mouse = Input.getClientMouse();
				int wid = ClientWindow.getClientWindow().getWidth();
				int hei = ClientWindow.getClientWindow().getHeight();
				int minusX = Math.max(0, (mouse.getX() + fWidth + 13) - wid);
				int minusY = Math.max(0, (mouse.getY() + fHeight + 3) - hei);
				Graphics.getClientShaderManager().unbindShader();
				gl.begin(GLPrimitive.QUADS);

				gl.color(0.5f, 0.5f, 0.5f, 0.75f);
				gl.vertex(mouse.getX() + 6 - minusX, mouse.getY() - minusY + 12);
				gl.vertex(mouse.getX() + 6 - minusX + fWidth + 6, mouse.getY() - minusY + 12);
				gl.vertex(mouse.getX() + 6 - minusX + fWidth + 6, mouse.getY() - minusY + fHeight + 12);
				gl.vertex(mouse.getX() + 6 - minusX, mouse.getY() - minusY + fHeight + 12);

				gl.end();

				Graphics.getClientShaderManager().bindShader("texture");
				ItemRenderer.setRenderItemBig();
				ItemRenderer.renderItemStack(item.clone().setAmount(1), mouse.getX() + 6 - minusX + 9, mouse.getY() - minusY + 12 + 9);
				ItemRenderer.setRenderItemSmall();
				Graphics.getClientShaderManager().unbindShader();

				renderer.renderString(mouse.getX() + 6 + 6 - minusX + 32, mouse.getY() + 12 - minusY, fFirstLine);
				if (fDescription != null) {
					renderer.renderString(mouse.getX() + 6 + 6 - minusX, mouse.getY() + 12 - minusY + 22, fDescription);
				}
				renderer.resetStyles();

			}
		});

	}
}
