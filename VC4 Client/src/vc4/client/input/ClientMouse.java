/**
 * 
 */
package vc4.client.input;

import vc4.api.client.ClientWindow;
import vc4.api.input.Input;
import vc4.api.input.Mouse;

/**
 * @author paul
 *
 */
public class ClientMouse implements Mouse{

	private static int height;
	
	
	int buttons;
	
	boolean[] pressed;
	
	float scroll;
	
	int x, y, dx, dy;
	
	/**
	 * 
	 */
	public ClientMouse() {
		Input.setMouse(this);
	}
	
	
	
	private ClientMouse(int buttons, boolean[] pressed, float scroll, int x, int y, int dx, int dy) {
		super();
		this.buttons = buttons;
		this.pressed = pressed;
		this.scroll = scroll;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}



	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#leftButtonPressed()
	 */
	@Override
	public boolean leftButtonPressed() {
		return buttonPressed(0);
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#rightButtomPressed()
	 */
	@Override
	public boolean rightButtomPressed() {
		return buttonPressed(1);
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#buttonPressed(int)
	 */
	@Override
	public boolean buttonPressed(int button) {
		return pressed[button];
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getMouseButtons()
	 */
	@Override
	public int getMouseButtons() {
		return buttons;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getDX()
	 */
	@Override
	public int getDX() {
		return dx;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getDY()
	 */
	@Override
	public int getDY() {
		return dy;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#getDWheel()
	 */
	@Override
	public float getDWheel() {
		return scroll;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Mouse#update()
	 */
	@Override
	public void update() {
		height = ClientWindow.getClientWindow().getHeight();
		buttons = org.lwjgl.input.Mouse.getButtonCount();
		pressed = new boolean[buttons];
		for(int i = 0; i < buttons; ++i){
			pressed[i] = org.lwjgl.input.Mouse.isButtonDown(i);
		}
		x = org.lwjgl.input.Mouse.getX();
		y = height - org.lwjgl.input.Mouse.getY() - 1;
		//dx = org.lwjgl.input.Mouse.getDX();
		//dy = -org.lwjgl.input.Mouse.getDY();
		scroll = org.lwjgl.input.Mouse.getDWheel() / 120F;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Mouse clone() {
		return new ClientMouse(buttons, pressed, scroll, x, y, dx, dy);
	}

	

}
