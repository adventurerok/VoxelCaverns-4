package vc4.launcher.gui.popup;

import javax.swing.JPopupMenu;

import vc4.launcher.repo.Package;

public class RunnablePopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 430287943097312490L;

	public RunnablePopupMenu(Package pack) {
		for (int d = 0; d < pack.getRuns().length; ++d) {
			add(new RunnableMenuItem(pack.getRuns()[d]));
		}
	}

}
