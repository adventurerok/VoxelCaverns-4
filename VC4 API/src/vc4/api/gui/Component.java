/**
 * 
 */
package vc4.api.gui;

import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import vc4.api.gui.events.MouseEvent;
import vc4.api.gui.listeners.MouseListener;
import vc4.api.input.MouseSet;
import vc4.api.logging.Logger;

/**
 * @author paul
 * 
 */
public class Component {

	private static ComponentUtils utils;
	private ArrayList<Component> subComponents = new ArrayList<Component>();
	private ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>();

	private Component _parent;
	private Rectangle _bounds = new Rectangle(0, 0, 100, 100);
	private Rectangle _dBounds = new Rectangle(0, 0, 100, 100);
	private boolean _visible = true;

	private boolean _focus = false;

	private String _command = null;
	private String _altCommand = null;

	private Resizer resizer;

	/**
	 * @param utils
	 *            the utils to set
	 */
	public static void setUtils(ComponentUtils utils) {
		Component.utils = utils;
	}

	/**
	 * @return the utils
	 */
	public static ComponentUtils getUtils() {
		return utils;
	}

	public void setResizer(Resizer resizer) {
		this.resizer = resizer;
	}

	public Resizer getResizer() {
		return resizer;
	}

	public Component getFocusComponent() {
		if (!hasFocus()) return null;
		for (Component c : subComponents) {
			if (c.hasFocus()) return c.getFocusComponent();
		}
		return this;
	}

	private String _name;

	public boolean isClickable() {
		return _visible;
	}

	/**
	 * Performs the components action with the given name, if no such action is found, the command goes to the parent
	 * 
	 * @param action
	 */
	public void action(String action) {
		if (_parent != null) _parent.action(action);
	}

	public boolean isVisible() {
		return _visible;
	}

	public void draw() {
		if (!isVisible()) return;
		utils.renderComponent(this);
		for (Component c : subComponents) {
			c.draw();
		}
	}

	public void update() {
		for (Component c : subComponents) {
			c.update();
		}
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return _bounds;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 */

	public Component setBounds(Rectangle bounds) {
		_bounds = bounds;
		return this;
	}

	public void resized() {
		for (Component c : subComponents) {
			if (c.resizer != null) c.resizer.resize(c);
			c.resized();
		}
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		_visible = visible;
	}

	public void add(Component c) {
		subComponents.add(c);
		if (c._parent != null) {
			c._parent.remove(c);
		}
		c._parent = this;
		if (c.resizer != null) c.resizer.resize(c);
	}

	public void add(Component c, int index) {
		subComponents.add(index, c);
		if (c._parent != null) {
			c._parent.remove(c);
		}
		c._parent = this;
		if (c.resizer != null) c.resizer.resize(c);
	}

	public void remove(Component c) {
		subComponents.remove(c);
		c._parent = null;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		_command = command;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return _command;
	}

	public void addMouseListener(MouseListener l) {
		mouseListeners.add(l);
	}

	public void removeMouseListener(MouseListener l) {
		mouseListeners.remove(l);
	}

	public void bringToFront() {
		if (_parent == null) return;
		Component par = _parent;
		par.remove(this);
		par.add(this);
	}

	public void bringToBack() {
		if (_parent == null) return;
		Component par = _parent;
		par.remove(this);
		par.add(this, 0);
	}

	public void fireMouseEvent(String type, int button, int x, int y) {
		MouseEvent event = new MouseEvent(this, button, x, y);
		String name = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		try {
			Method method = MouseListener.class.getMethod("mouse" + name, MouseEvent.class);
			for (int dofor = 0; dofor < mouseListeners.size(); ++dofor) {
				method.invoke(mouseListeners.get(dofor), event);
			}
		} catch (Exception e) {
			Logger.getLogger(Component.class).warning("Exception occured while firing mouse event", e);
		}

	}

	public MouseSet getMouseSet() {
		if (_parent == null) return null;
		else return _parent.getMouseSet();
	}

	public Component getHovering(Rectangle rect) {
		for (int dofor = subComponents.size() - 1; dofor >= 0; --dofor) {
			if (!subComponents.get(dofor).isClickable()) continue;
			if (rect.intersects(subComponents.get(dofor)._bounds)) {
				Component result = subComponents.get(dofor).getHovering(rect);
				if (result != null) return result;
			}
		}
		return this;
	}

	public boolean hasFocus() {
		return _focus;
	}

	public void setFocus(boolean focus) {
		if (focus && _parent != null) {
			_parent.setFocus(true);
			for (int dofor = 0; dofor < _parent.subComponents.size(); ++dofor) {
				_parent.subComponents.get(dofor).setFocus(false);
			}
		} else if (!focus) {
			for (int dofor = 0; dofor < subComponents.size(); ++dofor) {
				subComponents.get(dofor).setFocus(false);
			}
		}
		_focus = focus;
	}

	/**
	 * @return the parent
	 */
	public Component getParent() {
		return _parent;
	}

	public int getX() {
		return getBounds().x;
	}

	public int getY() {
		return getBounds().y;
	}

	public int getWidth() {
		return getBounds().width;
	}

	public int getHeight() {
		return getBounds().height;
	}

	public static interface ComponentUtils {

		public void attachBorder(Rectangle bounds, Component child, Border b);

		public void renderComponent(Component c);
	}

	/**
	 * @param altCommand
	 *            the altCommand to set
	 */
	public void setAltCommand(String altCommand) {
		_altCommand = altCommand;
	}

	/**
	 * @return the altCommand
	 */
	public String getAltCommand() {
		return _altCommand;
	}

	public Rectangle getDefaultBounds() {
		return _dBounds;
	}

	public void setDefaultBounds(Rectangle dbounds) {
		_dBounds = dbounds;
	}

	/**
	 * @return the subComponents
	 */
	public List<Component> getSubComponents() {
		return subComponents;
	}

}
