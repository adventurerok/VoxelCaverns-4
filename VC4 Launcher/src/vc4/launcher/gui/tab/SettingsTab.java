package vc4.launcher.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import vc4.launcher.gui.settings.SettingsPanel;
import vc4.launcher.gui.settings.SettingsTree;

public class SettingsTab extends JPanel {
	
	public SettingsTab() {
		setLayout(new BorderLayout(0, 0));
		
		SettingsTree tree = new SettingsTree();
		tree.setRootVisible(false);
		tree.setPreferredSize(new Dimension(150, 64));
		add(tree, BorderLayout.WEST);
		SettingsPanel panel = new SettingsPanel();
		add(panel, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 215431041641449818L;

}
