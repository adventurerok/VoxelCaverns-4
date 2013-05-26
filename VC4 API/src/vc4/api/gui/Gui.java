package vc4.api.gui;

import java.awt.Color;
import java.awt.Rectangle;

import vc4.api.client.Client;
import vc4.api.graphics.*;
import vc4.api.gui.events.MouseEvent;
import vc4.api.gui.listeners.MouseListener;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.Input;
import vc4.api.vector.Vector2i;

public abstract class Gui extends Component implements MouseListener {

	private static OpenGL gl;

	public Component innerGui = new Panel();

	public static int SIDES_WIDTH = 8;
	public static int TOP_WIDTH = 16;

	boolean mousePressed;
	GuiBorder mouseBorder;
	Vector2i mouseOriginal;
	Rectangle boundsOriginal;

	public enum GuiBorder {
		MOVE, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	}

	public Gui() {
		addMouseListener(this);
	}

	@Override
	public boolean isClickable() {
		return isVisible();
	}

	@Override
	public void resized() {
		Rectangle inner = getBounds();
		inner = new Rectangle(inner.x + SIDES_WIDTH, inner.y + TOP_WIDTH, inner.width - 2 * SIDES_WIDTH, inner.height - SIDES_WIDTH - TOP_WIDTH);
		innerGui.setBounds(inner);
		innerGui.resized();

	}

	public abstract Vector2i getMinSize();

	public abstract Vector2i getMaxSize();

	public abstract Vector2i getDefaultSize();

	@Override
	public void draw() {
		if (!isVisible()) return;
		GuiBorder bord = getBorder(Input.getClientMouse().getX(), Input.getClientMouse().getY());
		if(Client.getGame().getHoveringComponent() != this) bord = null;
		if (gl == null) gl = Graphics.getClientOpenGL();
		ColorScheme scheme = Client.getGame().getColorScheme(Client.getGame().getColorSchemeSetting().toString());
		gl.begin(GLPrimative.QUADS);
		gl.color(scheme.backgroundNormal);
		if (bord != null) {
			gl.vertex(getX(), getY());
			gl.vertex(getX() + getWidth(), getY());
			gl.vertex(getX() + getWidth(), getY() + getHeight());
			gl.vertex(getX(), getY() + getHeight());
		} else {
			gl.vertex(innerGui.getX(), innerGui.getY());
			gl.vertex(innerGui.getX() + innerGui.getWidth(), innerGui.getY());
			gl.vertex(innerGui.getX() + innerGui.getWidth(), innerGui.getY() + innerGui.getHeight());
			gl.vertex(innerGui.getX(), innerGui.getY() + innerGui.getHeight());
		}
		gl.end();
		if(bord != null){
			gl.begin(GLPrimative.LINE_LOOP);
			gl.color(scheme.outlineNormal);
			gl.vertex(getX(), getY());
			gl.vertex(getX() + getWidth(), getY());
			gl.vertex(getX() + getWidth(), getY() + getHeight());
			gl.vertex(getX(), getY() + getHeight());
			gl.end();
		}
		gl.begin(GLPrimative.LINE_LOOP);
		gl.color(scheme.outlineNormal);
		gl.vertex(innerGui.getX(), innerGui.getY());
		gl.vertex(innerGui.getX() + innerGui.getWidth(), innerGui.getY());
		gl.vertex(innerGui.getX() + innerGui.getWidth(), innerGui.getY() + innerGui.getHeight());
		gl.vertex(innerGui.getX(), innerGui.getY() + innerGui.getHeight());
		gl.end();
		if(bord != null){
			gl.begin(GLPrimative.QUADS);
			gl.color(Color.red);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH, getY() + TOP_WIDTH - 2);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY() + TOP_WIDTH - 2);
			gl.color(Color.blue);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 3.5f, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY() + TOP_WIDTH - 2);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 3.5f, getY() + TOP_WIDTH - 2);
			gl.end();
			gl.begin(GLPrimative.LINE_LOOP);
			gl.color(scheme.outlineNormal);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 3.5f, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH, getY() + TOP_WIDTH - 2);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 3.5f, getY() + TOP_WIDTH - 2);
			gl.end();
			gl.begin(GLPrimative.LINES);
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY());
			gl.vertex(getX() + getWidth() - SIDES_WIDTH - TOP_WIDTH * 2, getY() + TOP_WIDTH - 2);
			gl.end();
		}
		innerGui.draw();
	}

	@Override
	public void update() {
		if (mousePressed && isVisible()) {
			Vector2i mouse = Input.getClientMouse().getPos();
			Vector2i diff = mouse.subtract(mouseOriginal);
			Vector2i wind = Client.getGame().getWindow().getDimensions();
			Vector2i min = getMinSize();
			Vector2i max = getMaxSize();
			int nx = getX();
			int ny = getY();
			int nw = getWidth();
			int nh = getHeight();
			if (mouseBorder == GuiBorder.MOVE) {
				nx = Math.max(0, Math.min(wind.x - getWidth(), boundsOriginal.x + diff.x));
				ny = Math.max(0, Math.min(wind.y - getHeight(), boundsOriginal.y + diff.y));
			} else if (mouseBorder == GuiBorder.SOUTH) {
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height + diff.y));
			} else if (mouseBorder == GuiBorder.EAST) {
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width + diff.x));
			} else if (mouseBorder == GuiBorder.SOUTHEAST) {
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width + diff.x));
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height + diff.y));
			} else if (mouseBorder == GuiBorder.WEST) {
				if (diff.x > 0) diff.x = 0;
				nx = Math.max(0, Math.min(wind.x - getWidth(), boundsOriginal.x + diff.x));
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width - diff.x));
			} else if (mouseBorder == GuiBorder.SOUTHWEST) {
				if (diff.x > 0) diff.x = 0;
				nx = Math.max(0, Math.min(wind.x - getWidth(), boundsOriginal.x + diff.x));
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width - diff.x));
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height + diff.y));
			} else if (mouseBorder == GuiBorder.NORTHWEST) {
				// if(diff.x > 0) diff.x = 0;
				// if(diff.y > 0) diff.y = 0;
				nx = Math.max(0, Math.min(wind.x - getWidth(), boundsOriginal.x + diff.x));
				ny = Math.max(0, Math.min(wind.y - getHeight(), boundsOriginal.y + diff.y));
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width - diff.x));
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height - diff.y));
			} else if (mouseBorder == GuiBorder.NORTH) {
				// if(diff.y > 0) diff.y = 0;
				ny = Math.max(0, Math.min(wind.y - getHeight(), boundsOriginal.y + diff.y));
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height - diff.y));
			} else if (mouseBorder == GuiBorder.NORTHEAST) {
				// if(diff.y > 0) diff.y = 0;
				ny = Math.max(0, Math.min(wind.y - getHeight(), boundsOriginal.y + diff.y));
				nw = Math.max(min.x, Math.min(Math.min(wind.x - getX(), max.x), boundsOriginal.width + diff.x));
				nh = Math.max(min.y, Math.min(Math.min(wind.y - getY(), max.y), boundsOriginal.height - diff.y));
			}
			setBounds(new Rectangle(nx, ny, nw, nh));
			resized();
		} else mousePressed = false;
		if(isVisible() && Client.getGame().getHoveringComponent() == this){
			GuiBorder bord = getBorder(Input.getClientMouse().getX(), Input.getClientMouse().getY());
			if (checkClose(Input.getClientMouse().getX(), Input.getClientMouse().getY())) bord = null;
			if (checkDefault(Input.getClientMouse().getX(), Input.getClientMouse().getY())) bord = null;
			if (bord == GuiBorder.MOVE) Client.getGame().getCursor("move").bind();
			else if (bord == GuiBorder.NORTH || bord == GuiBorder.SOUTH) Client.getGame().getCursor("resize_ns").bind();
			else if (bord == GuiBorder.EAST || bord == GuiBorder.WEST) Client.getGame().getCursor("resize_we").bind();
			else if (bord == GuiBorder.NORTHEAST || bord == GuiBorder.SOUTHWEST) Client.getGame().getCursor("resize_swne").bind();
			else if (bord == GuiBorder.NORTHWEST || bord == GuiBorder.SOUTHEAST) Client.getGame().getCursor("resize_nwse").bind();
		}
		innerGui.update();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (checkClose(e.getX(), e.getY())) {
			close();
			return;
		}
		if (checkDefault(e.getX(), e.getY())) {
			setBounds(getDefaultBounds());
			resized();
			return;
		}
		mouseBorder = getBorder(e.getX(), e.getY());
		mouseOriginal = new Vector2i(e.getX(), e.getY());
		boundsOriginal = (Rectangle) getBounds().clone();
		mousePressed = true;
	}

	public abstract void close();

	private boolean checkClose(int x, int y) {
		y -= getY();
		if (y < 0) return false;
		y -= TOP_WIDTH - 2;
		if (y > 0) return false;
		x -= getX();
		if (x > getWidth() - SIDES_WIDTH) return false;
		if (x < getWidth() - SIDES_WIDTH - TOP_WIDTH * 2) return false;
		return true;
	}

	private boolean checkDefault(int x, int y) {
		y -= getY();
		if (y < 0) return false;
		y -= TOP_WIDTH - 2;
		if (y > 0) return false;
		x -= getX();
		if (x > getWidth() - SIDES_WIDTH - TOP_WIDTH * 2) return false;
		if (x < getWidth() - SIDES_WIDTH - TOP_WIDTH * 3.5) return false;
		return true;
	}

	public GuiBorder getBorder(int mx, int my) {
		Rectangle mouse = new Rectangle(mx, my, 1, 1);
		if (!mouse.intersects(getBounds())) return null;
		return getBorder1(mx, my);
	}

	private GuiBorder getBorder1(int mx, int my) {
		mx -= innerGui.getX();
		my -= innerGui.getY();
		int horiz = mx < 0 ? 0 : (mx >= innerGui.getWidth() ? 2 : 1);
		int vert = my < 0 ? 0 : (my >= innerGui.getHeight() ? 2 : 1);
		if (horiz == 0) {
			if (vert == 0) return GuiBorder.NORTHWEST;
			if (vert == 1) return GuiBorder.WEST;
			if (vert == 2) return GuiBorder.SOUTHWEST;
		}
		if (horiz == 1) {
			if (vert == 0) {
				if (my < -11) return GuiBorder.NORTH;
				else return GuiBorder.MOVE;
			}
			if (vert == 1) return null;
			if (vert == 2) return GuiBorder.SOUTH;
		}
		if (horiz == 2) {
			if (vert == 0) return GuiBorder.NORTHEAST;
			if (vert == 1) return GuiBorder.EAST;
			if (vert == 2) return GuiBorder.SOUTHEAST;
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TASK Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TASK Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TASK Auto-generated method stub

	}
}
