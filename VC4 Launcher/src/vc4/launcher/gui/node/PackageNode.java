package vc4.launcher.gui.node;

import vc4.launcher.Package;
import vc4.launcher.gui.settings.PackageSettingsPanel;

public class PackageNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3079007254693466614L;

	public PackageNode(Package pack) {
		super(pack.getName(), new PackageSettingsPanel(pack));
	}

}
