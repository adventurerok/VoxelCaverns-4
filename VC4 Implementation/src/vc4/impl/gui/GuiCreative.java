package vc4.impl.gui;

import java.awt.Color;
import java.awt.Rectangle;

import vc4.api.client.Client;
import vc4.api.client.ClientWindow;
import vc4.api.container.ContainerCreative;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.Component;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.MouseSet;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;
import vc4.api.render.*;
import vc4.api.text.Localization;
import vc4.api.util.ColorUtils;

public class GuiCreative extends Component {

	private static OpenGL gl;
	public static ContainerCreative def = new ContainerCreative();

	public ContainerCreative inventory = def;
	public static FontRenderer renderer = FontRenderer.createFontRenderer("unispaced_14", 14f);

	public int pages = 1;
	public int rows = 1;
	public int stacksPerPage = 16;
	public static int columnsPerPage = 12;
	public int currentPage = 0;

	public int listId[];
	public boolean builtList[];

	boolean show = true;

	@Override
	public void resized() {
		recalculateDisplayLists();
	}

	public void recalculateDisplayLists() {
		int width = getParent().getWidth();
		int height = getParent().getHeight();
		calculateColumns();
		setBounds(new Rectangle(width - getRequiredWidth(), 0, getRequiredWidth(), height));
		calculateRows();
		calculatePages();
		if (listId != null) {
			try {
				for (int dofor = 0; dofor < listId.length; ++dofor) {
					if (listId[dofor] != 0) gl.deleteLists(listId[dofor], 1);
				}
			} catch (Exception e) {
			}
		}
		listId = new int[pages];
		builtList = new boolean[pages];
	}

	protected void calculateColumns() {
		float width = getParent().getWidth() / 2F;
		width -= 192F;
		int c = MathUtils.floor(width / 32F);
		columnsPerPage = Math.min(12, Math.max(5, c));

	}

	public int getRequiredWidth() {
		return columnsPerPage * 32;
	}

	protected void calculateRows() {
		int i = getHeight();
		i -= 128;

		rows = Math.max(5, i / 32);
		stacksPerPage = rows * columnsPerPage;
	}

	protected void calculatePages() {
		pages = 1;

		ItemStack stack = inventory.getItem(stacksPerPage * pages);
		while (stack != null) {
			++pages;
			stack = inventory.getItem(stacksPerPage * pages);
		}
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void draw() {
		if (!Client.getGame().isPaused()) return;
		// if (!SingleplayerUtils.getPlayer().getGameMode().isCreativeGui()) return;
		gl = Graphics.getOpenGL();
		ColorScheme scheme = Client.getGame().getColorScheme(Client.getGame().getColorSchemeSetting().getString());
		int cx = getX();
		int cy = getY();
		int cw = getWidth();
		// int ch = getHeight();
		MouseSet mice = Client.getGame().getMouseSet();
		int mx = mice.getX();
		int my = mice.getY();

		int wx = ClientWindow.getClientWindow().getWidth() - 384;

		Graphics.getClientShaderManager().unbindShader();
		gl.begin(GLPrimative.QUADS);
		gl.color(scheme.backgroundNormal);
		gl.vertex(wx - 36, cy);
		gl.vertex(wx + 384, cy);
		gl.vertex(wx + 384, cy + 28);
		gl.vertex(wx - 36, cy + 28);
		gl.end();

		String s = Localization.getLocalization("gui.creative.pages", currentPage + 1, pages);
		float l = renderer.measureString(s, 14).x;
		renderer.renderString(cx + cw - 8 - l, 6, s);
		renderer.resetStyles();
		Graphics.getClientShaderManager().unbindShader();

		float px = wx - 2 - 16 - 8;
		float py = 6;

		Rectangle mouse = mice.getMouseRectangle();
		boolean down = mice.buttonPressed(0);
		Rectangle sh = new Rectangle((int) px, (int) py, 16, 16);
		boolean hova = mouse.intersects(sh);

		if (show) {
			gl.begin(GLPrimative.TRIANGLES);
			if (!hova) gl.color(1F, 0F, 0F, 0.88F);
			else gl.color(1F, 0F, 0F, 1F);
			gl.vertex(px + 8, py);
			gl.vertex(px + 16, py + 16);
			gl.vertex(px, py + 16);
			gl.end();
		} else {
			gl.begin(GLPrimative.TRIANGLES);
			if (!hova) gl.color(0F, 1F, 0F, 0.88F);
			else gl.color(0F, 1F, 0F, 1F);
			gl.vertex(px, py);
			gl.vertex(px + 16, py);
			gl.vertex(px + 8, py + 16);
			gl.end();
		}

		if (hova && down) show = !show;

		boolean hover = false;
		if (pages > 1 && show) {
			boolean doLast = currentPage != 0;
			boolean doNext = currentPage != pages - 1;

			int nx = cx - 34;
			int ny = cy + 48;

			Color top = doLast ? new Color(255, 0, 0) : new Color(0, 0, 0);
			Color bottom = doNext ? new Color(255, 0, 0) : new Color(0, 0, 0);

			Rectangle t = new Rectangle(nx, ny, 32, 32);
			Rectangle b = new Rectangle(nx, ny + 48, 32, 32);

			if (mouse.intersects(t) && doLast) {
				top = Color.yellow;
				hover = true;
				if (down) --currentPage;
			} else if (mouse.intersects(b) && doNext) {
				bottom = Color.yellow;
				hover = true;
				if (down) ++currentPage;
			}

			gl.begin(GLPrimative.TRIANGLES);
			gl.color(ColorUtils.brighterLinear(80, top));
			gl.vertex(nx + 16, ny);
			gl.color(top);
			gl.vertex(nx + 32, ny + 32);
			gl.vertex(nx, ny + 32);
			gl.color(bottom);
			gl.vertex(nx, ny + 48);
			gl.vertex(nx + 32, ny + 48);
			gl.color(ColorUtils.brighterLinear(80, bottom));
			gl.vertex(nx + 16, ny + 80);
			gl.end();
		}

		int ix = MathUtils.floor((mx - cx) / 32F);
		int iy = MathUtils.floor((my - (cy + 32)) / 32F);
		ItemStack sel = null;
		if (!hover && show) sel = renderSelectedStack(ix, iy);
		if (currentPage >= pages) currentPage = pages - 1;
		if (!builtList[currentPage]) createListForPage(currentPage);
		if (show) {
			Graphics.getClientShaderManager().bindShader("texture");
			gl.callList(listId[currentPage]);
		}

		if (sel != null && show) ItemTooltipRenderer.renderTooltip(sel, ToolTipType.CREATIVE);

	}

	private ItemStack renderSelectedStack(int ix, int iy) {
		if (ix < 0 || iy < 0) return null;
		int cx = getX();
		int cy = getY();
		if (ix > -1 && iy > -1 && ix < columnsPerPage && iy < rows) {
			int at = (iy * columnsPerPage) + ix + (currentPage * stacksPerPage);
			Graphics.getClientShaderManager().unbindShader();
			gl.begin(GLPrimative.QUADS);
			gl.color(0.9F, 0.9F, 0.1F);
			gl.vertex(cx + ix * 32, cy + iy * 32 + 32);
			gl.vertex(cx + ix * 32 + 32, cy + iy * 32 + 32);
			gl.vertex(cx + ix * 32 + 32, cy + iy * 32 + 64);
			gl.vertex(cx + ix * 32, cy + iy * 32 + 64);
			gl.end();
			MouseSet mice = Client.getGame().getMouseSet();
			if (mice.buttonPressed(0) && inventory.getItem(at) != null) {
				ItemStack stack = inventory.getItem(at).clone();
				stack.setAmount(-1);
				Client.getGame().getPlayer().getInventory().addItemStack(stack);
			}
			if (mice.buttonPressed(1) && inventory.getItem(at) != null) {
				ItemStack stack = inventory.getItem(at).clone();
				stack.setAmount(1);
				Client.getGame().getPlayer().getInventory().addItemStack(stack);
			}
			if (mice.buttonPressed(2) && inventory.getItem(at) != null) {
				ItemStack stack = inventory.getItem(at).clone();
				stack.setAmount(stack.getItem().getMaxStack(stack.getDamage()));
				Client.getGame().getPlayer().getInventory().addItemStack(stack);
			}
			ItemStack hover = inventory.getItem(at);
			if (hover != null && Client.getGame().getPlayer().getInventory().getHeldItemStack() == null) {
				try {
					return hover;
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	private void createListForPage(int page) {
		if (listId[page] != 0) gl.deleteLists(listId[page], 1);
		listId[page] = gl.genLists(1);
		gl.newList(listId[page], GLCompileFunc.COMPILE);
		int cx = getX();
		int cy = getY();
		for (int y = rows - 1; y > -1; --y) {
			for (int x = 0; x < columnsPerPage; ++x) {
				int at = (y * columnsPerPage) + x + (page * stacksPerPage);
				if (inventory.getItem(at) == null) {
					continue;
				}
				ItemRenderer.renderItemStack(inventory.getItem(at), (8 + cx + x * 32), (8 + cy + y * 32) + 32);
			}
		}
		gl.endList();
		builtList[page] = true;
	}

	@Override
	public void update() {
		if (!Client.getGame().isPaused()) return;
		// if (inventory.search(TextInputCreative.getInputText())) {
		// recalculateDisplayLists();
		// }
		super.update();
	}

	public void reloadItems() {
		inventory = new ContainerCreative();
		recalculateDisplayLists();
	}

}
