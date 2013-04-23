package vc4.launcher.gui.node;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import vc4.launcher.Package;
import vc4.launcher.Repo;
import vc4.launcher.gui.settings.RepoSettingsPanel;

public class RepoNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542093662709505459L;
	private static Icon ico = new ImageIcon(RepoNode.class.getClassLoader().getResource("resources/icons/repo.png"));

	public RepoNode(Repo repo) {
		super(repo.getName(), new RepoSettingsPanel(repo));
		setIcon(ico);
		for(Package p : repo.getPackages()){
			add(new PackageNode(p));
		}
	}

}
