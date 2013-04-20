package vc4.api.gui.events;

import vc4.api.gui.Component;

public class MouseEvent extends AbstractEvent {

	private int button;
	private int x, y;
	
	public MouseEvent(Component c, int button, int x, int y) {
		super(c, AbstractEvent.USER_INPUT);
		this.button = button;
	}
	
	public int getButtonPressed(){
		return button;
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	
}
