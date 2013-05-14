/**
 * 
 */
package vc4.client.gui;

import vc4.api.GameState;
import vc4.api.client.Client;
import vc4.api.gui.*;
import vc4.impl.gui.GuiInventory;

/**
 * @author paul
 *
 */
public class IngameGui extends Component {

	GuiInventory invGui;
	ScreenDebug debug;
	
	/**
	 * 
	 */
	public IngameGui() {
		invGui = new GuiInventory();
		add(invGui);
		debug = new ScreenDebug();
		add(debug);
		add(new OverlayRenderer());
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#draw()
	 */
	@Override
	public void draw() {
		if(!isVisible()) return;
		super.draw();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#update()
	 */
	@Override
	public void update() {
		if(!isVisible()) return;
		super.update();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return Client.getGame().getGameState() == GameState.SINGLEPLAYER;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isClickable()
	 */
	@Override
	public boolean isClickable() {
		return isVisible();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#getBorderToAttach()
	 */
	@Override
	public Border getBorderToAttach() {
		return Border.FILL;
	}
}
