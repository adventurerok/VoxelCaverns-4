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
public class GuiTypeMenu implements GuiType {

	int startx = 10, starty = 10;
	int rows = 0, columns = 1;
	int bwidth = 187, bheight = 20, bgapx = 10, bgapy = 10;
	float fontSize = 14;
	int currentPos = 0;
	
	private long state = new Random().nextLong();
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.GuiType#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return "menu";
	}

	/* (non-Javadoc)
	 * @see vc4.api.gui.GuiType#addComponentTo(vc4.api.gui.Component, vc4.api.gui.Component, java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addComponentTo(Gui parent, LinkedHashMap<String, ?> yaml) {
		String clazz = null, text = null, action = null, alt = null;
		ArrayList<Object> args = null;
		float fontSize = this.fontSize;
		for(Entry<String, ?> e : yaml.entrySet()){
			if(e.getKey().equals("class")) clazz = e.getValue().toString();
			else if(e.getKey().equals("text")) text = e.getValue().toString();
			else if(e.getKey().equals("action")) action = e.getValue().toString();
			else if(e.getKey().equals("alt")) alt = e.getValue().toString();
			else if(e.getKey().equals("args")) args = (ArrayList<Object>) e.getValue();
			else if(e.getKey().equals("fontsize")) fontSize = Float.parseFloat(e.getValue().toString());
		}
		Object[] agmts;
		if(args == null) agmts = new Object[0];
		else{
			agmts = new Object[args.size()];
			for(int d = 0; d < args.size(); ++d){
				agmts[d] = args.get(d);
			}
		}
		if(clazz.equals("button")){
			Button b = new Button(Strings.chatLocalization(text, agmts), action, alt);
			b.setBounds(getNextBounds());
			b.setFontSize(fontSize);
			parent.add(b);
		} else if(clazz.equals("settingscroller")){
			SettingScroller b = new SettingScroller(Strings.chatLocalization(text, agmts), action, alt);
			b.setBounds(getNextBounds());
			b.setFontSize(fontSize);
			parent.add(b);
		}
	}

	/* (non-Javadoc)
	 * @see vc4.api.gui.GuiType#setup(java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setup(Gui c, LinkedHashMap<String, ?> yaml) {
		for(Entry<String, ?> e : yaml.entrySet()){
			if(e.getKey().equals("name")) c.setName(e.getValue().toString());
			else if(e.getKey().equals("columns")) columns = ((Number)e.getValue()).intValue();
			else if(e.getKey().equals("rows")) rows = ((Number)e.getValue()).intValue();
			else if(e.getKey().equals("button")){
				LinkedHashMap<String, ?> bDat = (LinkedHashMap<String, ?>) e.getValue();
				for(Entry<String, ?> f : bDat.entrySet()){
					int val = ((Number)f.getValue()).intValue();
					if(f.getKey().equals("width")) bwidth = val;
					else if(f.getKey().equals("height")) bheight = val;
					else if(f.getKey().equals("gapx")) bgapx = val;
					else if(f.getKey().equals("gapy")) bgapy = val;
				}
			} else if(e.getKey().equals("fontsize")) fontSize = Float.parseFloat(e.getValue().toString());
			else if(e.getKey().equals("border")) c.setBorderToAttach(Border.valueOf(e.getValue().toString().toUpperCase()));
		}
	}
	
	/**
	 * @return the state
	 */
	public long getState() {
		return state;
	}
	
	private Rectangle getNextBounds(){
		Rectangle start = new Rectangle(startx, starty, bwidth, bheight);
		int xp;
		int yp;
		if(rows == 0){
			xp = (currentPos % columns) * (bwidth + bgapx);
			yp = (currentPos / columns) * (bheight + bgapy);
		} else if(columns == 0){
			yp = (currentPos % rows) * (bheight + bgapy);
			xp = (currentPos / rows) * (bwidth + bgapx);
		} else {
			int rn = currentPos % rows;
			int cn = (currentPos / rows) % rows;
			xp = cn * (bwidth + bgapx);
			yp = rn * (bheight + bgapy);
		}
		start.x += xp;
		start.y += yp;
		++currentPos;
		return start;
	}

	/* (non-Javadoc)
	 * @see vc4.api.gui.GuiType#isClickable(vc4.api.gui.Gui)
	 */
	@Override
	public boolean isClickable(Gui c) {
		return ClientWindow.getClientWindow().getGame().getMenuState() == state && Client.getGame().getGameState() == GameState.MENU;
	}

	/* (non-Javadoc)
	 * @see vc4.api.gui.GuiType#isVisible(vc4.api.gui.Gui)
	 */
	@Override
	public boolean isVisible(Gui c) {
		return isClickable(c);
	}


}
