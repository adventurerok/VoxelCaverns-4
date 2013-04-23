package vc4.launcher.gui.node;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class SettingsNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8307904378617855544L;
	
	private JPanel settingsPanel;

	private Icon icon;
	
	
	public SettingsNode(String name, JPanel settingsPanel) {
		super(name);
		this.settingsPanel = settingsPanel;
	}

	public JPanel getSettingsPanel() {
		return settingsPanel;
	}
	
	public void setSettingsPanel(JPanel settingsPanel) {
		this.settingsPanel = settingsPanel;
	}
	
	public Icon getIcon(){
		return icon;
	}
	
	public SettingsNode setIcon(Icon icon) {
		this.icon = icon;
		return this;
	}
	

}
