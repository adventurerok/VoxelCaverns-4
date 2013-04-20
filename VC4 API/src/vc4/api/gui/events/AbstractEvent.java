package vc4.api.gui.events;

import vc4.api.gui.Component;

public class AbstractEvent {

	public static final int USER_INPUT = 873;
	public static final int INTERNAL = 874;
	public static final int OTHER = 875;
	
	private Component _comp;
	private int _cause = INTERNAL;
	
	public AbstractEvent(Component c, int cause){
		_comp = c;
		_cause = cause;
	}
	
	public Component getComponent(){
		return _comp;
	}
	public int getCause(){
		return _cause;
	}
}
