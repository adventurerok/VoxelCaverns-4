package vc4.launcher.gui.settings;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class SettingsPanel extends JPanel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4806263256152930350L;
	private static SettingsPanel singleton;
	
	public static SettingsPanel getSingleton() {
		return singleton;
	}
	
	public SettingsPanel() {
		singleton = this;
		setLayout(new BorderLayout());
		add(SettingsTree.getSingleton().getSelectedPanel());
		setBorder(BorderFactory.createTitledBorder("Launcher"));
	}
	
	public void setPanel(JPanel panel){
		removeAll();
		add(panel);
		repaint();
		validate();
	}
}
