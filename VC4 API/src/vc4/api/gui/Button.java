/**
 * 
 */
package vc4.api.gui;

import vc4.api.gui.events.MouseEvent;
import vc4.api.gui.listeners.MouseListener;

/**
 * @author paul
 * 
 */
public class Button extends TextComponent implements MouseListener {

	int state = 0;

	public Button(Object text) {
		setText(text);
		addMouseListener(this);
	}

	public Button(Object text, String command) {
		setText(text);
		setCommand(command);
		addMouseListener(this);
	}

	public Button(Object text, String command, String alt) {
		setText(text);
		setCommand(command);
		setAltCommand(alt);
		addMouseListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.listeners.MouseListener#mousePressed(vc4.api.gui.events.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		state = 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.listeners.MouseListener#mouseClicked(vc4.api.gui.events.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (getParent() != null) getParent().action(e.getButtonPressed() == 0 ? getCommand() : getAltCommand());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.listeners.MouseListener#mouseReleased(vc4.api.gui.events.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		state = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.listeners.MouseListener#mouseEntered(vc4.api.gui.events.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		state = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.listeners.MouseListener#mouseExited(vc4.api.gui.events.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		state = 0;
	}

	public boolean isHovering() {
		return state != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component#getAltCommand()
	 */
	@Override
	public String getAltCommand() {
		String s = super.getAltCommand();
		if (s == null) return getCommand();
		else return s;
	}

}
