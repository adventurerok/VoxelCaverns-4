package vc4.launcher.gui.node;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import vc4.launcher.Package;
import vc4.launcher.gui.settings.PackageSettingsPanel;

public class PackageNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3079007254693466614L;
	private static Icon ico = new ImageIcon(PackageNode.class.getClassLoader().getResource("resources/icons/package.png"));

	public PackageNode(Package pack) {
		super(pack.getName(), new PackageSettingsPanel(pack));
		setIcon(ico);
	}

}
