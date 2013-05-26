/**
 * 
 */
package vc4.api.input;

import vc4.api.vector.Vector2i;

/**
 * @author paul
 *
 */
public interface Mouse extends Cloneable{

	public boolean leftButtonPressed();
	public boolean rightButtomPressed();
	public boolean buttonPressed(int button);
	public int getMouseButtons();
	public int getX();
	public int getY();
	public int getDX();
	public int getDY();
	public Vector2i getPos();
	
	/**
	 * Gets the scroll wheel rotation since last update
	 * @return The scroll wheel rotation change
	 */
	public float getDWheel();
	
	public void update();
	public Mouse clone();
}
