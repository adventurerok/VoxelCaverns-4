/**
 * 
 */
package vc4.api.gui.listeners;

import vc4.api.gui.events.MouseEvent;

/**
 * @author paul
 * 
 */
public interface MouseListener {

	public void mousePressed(MouseEvent e);

	public void mouseClicked(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseEntered(MouseEvent e);

	public void mouseExited(MouseEvent e);
}
