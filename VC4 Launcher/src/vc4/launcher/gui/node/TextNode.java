package vc4.launcher.gui.node;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import vc4.launcher.gui.settings.TextSettingsPanel;

public class TextNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8542306290145517344L;
	private static Icon ico = new ImageIcon(PackageNode.class.getClassLoader().getResource("resources/icons/text.png"));

	public TextNode(String name, URL url) {
		super(name, new TextSettingsPanel(url));
		setIcon(ico);
	}

}
