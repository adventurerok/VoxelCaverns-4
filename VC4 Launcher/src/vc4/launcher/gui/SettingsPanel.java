package vc4.launcher.gui;

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
		add(SettingsTree.getSingleton().getSelectedPanel());
	}
	
	public void setPanel(JPanel panel){
		removeAll();
		add(panel);
	}
}