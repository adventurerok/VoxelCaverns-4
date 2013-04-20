/**
 * 
 */
package vc4.api.input;

import java.awt.Rectangle;

/**
 * @author paul
 *
 */
public class MouseSet {

	private Mouse _old;
	private Mouse _current;
	
	/**
	 * 
	 */
	public MouseSet() {
		_old = _current = Input.getClientMouse();
	}
	
	/**
	 * @return the current
	 */
	public Mouse getCurrent() {
		return _current;
	}
	
	/**
	 * @return the old
	 */
	public Mouse getOld() {
		return _old;
	}
	
	public void update(){
		_old = _current;
		_current = Input.getClientMouse().clone();
	}
	
	public int getX(){
		return getCurrent().getX();
	}
	public int getY(){
		return getCurrent().getY();
	}
	
	public boolean buttonPressed(int button){
		return _current.buttonPressed(button) && !_old.buttonPressed(button);
	}
	
	public boolean buttonReleased(int button){
		return !_current.buttonPressed(button) && _old.buttonPressed(button);
	}
	
	public Rectangle getMouseRectangle(){
		return new Rectangle(_current.getX(), _current.getY(), 1, 1);
	}

}
