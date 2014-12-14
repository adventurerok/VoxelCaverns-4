/**
 * 
 */
package vc4.impl.gui;

import java.awt.Rectangle;
import java.util.*;
import java.util.Map.Entry;

import vc4.api.GameState;
import vc4.api.client.Client;
import vc4.api.client.ClientWindow;
import vc4.api.gui.*;
import vc4.api.text.Strings;

/**
 * @author paul
 * 
 */
public class ScreenTypeMenu implements ScreenType {

	int startX = 10, startY = 10;
	int rows = 0, columns = 1;
	int bWidth = 187, bHeight = 20, bGapX = 10, bGapY = 10;
	float fontSize = 14;
	int currentPos = 0;

	private long state = System.nanoTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.GuiType#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return "menu";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.GuiType#addComponentTo(vc4.api.gui.Component, vc4.api.gui.Component, java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addComponentTo(Screen parent, LinkedHashMap<String, ?> yaml) {
		String clazz = null, text = null, action = null, alt = null;
		ArrayList<Object> args = null;
		float fontSize = this.fontSize;
		for (Entry<String, ?> e : yaml.entrySet()) {
			switch(e.getKey()){
				case "class":
					clazz = e.getValue().toString();
					break;
				case "text":
					text = e.getValue().toString();
					break;
				case "action":
					action = e.getValue().toString();
					break;
				case "alt":
					alt = e.getValue().toString();
					break;
				case "args":
					args = (ArrayList<Object>) e.getValue();
					break;
				case "fontsize":
					fontSize = Float.parseFloat(e.getValue().toString());
					break;
			}
		}
		Object[] arguments;
		if (args == null) arguments = new Object[0];
		else {
			arguments = new Object[args.size()];
			for (int d = 0; d < args.size(); ++d) {
				arguments[d] = args.get(d);
			}
		}

		switch (clazz) {
			case "button": {
				Button b = new Button(Strings.chatLocalization(text, arguments), action, alt);
				b.setBounds(getNextBounds());
				b.setFontSize(fontSize);
				parent.add(b);
				break;
			}
			case "settingscroller": {
				SettingScroller b = new SettingScroller(Strings.chatLocalization(text, arguments), action, alt);
				b.setBounds(getNextBounds());
				b.setFontSize(fontSize);
				parent.add(b);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.GuiType#setup(java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setup(Screen c, LinkedHashMap<String, ?> yaml) {
		for (Entry<String, ?> e : yaml.entrySet()) {
			if (e.getKey().equals("name")) c.setName(e.getValue().toString());
			else if (e.getKey().equals("columns")) columns = ((Number) e.getValue()).intValue();
			else if (e.getKey().equals("rows")) rows = ((Number) e.getValue()).intValue();
			else if (e.getKey().equals("button")) {
				LinkedHashMap<String, ?> bDat = (LinkedHashMap<String, ?>) e.getValue();
				for (Entry<String, ?> f : bDat.entrySet()) {
					int val = ((Number) f.getValue()).intValue();
					if (f.getKey().equals("width")) bWidth = val;
					else if (f.getKey().equals("height")) bHeight = val;
					else if (f.getKey().equals("gapx")) bGapX = val;
					else if (f.getKey().equals("gapy")) bGapY = val;
				}
			} else if (e.getKey().equals("fontsize")) fontSize = Float.parseFloat(e.getValue().toString());
			else if (e.getKey().equals("border")) c.setResizer(new ResizerBorder(Border.valueOf(e.getValue().toString().toUpperCase())));
		}
	}

	/**
	 * @return the state
	 */
	public long getState() {
		return state;
	}

	private Rectangle getNextBounds() {
		Rectangle start = new Rectangle(startX, startY, bWidth, bHeight);
		int xp;
		int yp;
		if (rows == 0) {
			xp = (currentPos % columns) * (bWidth + bGapX);
			yp = (currentPos / columns) * (bHeight + bGapY);
		} else if (columns == 0) {
			yp = (currentPos % rows) * (bHeight + bGapY);
			xp = (currentPos / rows) * (bWidth + bGapX);
		} else {
			int rn = currentPos % rows;
			int cn = (currentPos / rows) % rows;
			xp = cn * (bWidth + bGapX);
			yp = rn * (bHeight + bGapY);
		}
		start.x += xp;
		start.y += yp;
		++currentPos;
		return start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.GuiType#isClickable(vc4.api.gui.Gui)
	 */
	@Override
	public boolean isClickable(Screen c) {
		return ClientWindow.getClientWindow().getGame().getMenuState() == state && Client.getGame().getGameState() == GameState.MENU;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.GuiType#isVisible(vc4.api.gui.Gui)
	 */
	@Override
	public boolean isVisible(Screen c) {
		return isClickable(c);
	}

}
