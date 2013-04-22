package vc4.launcher.gui.node;

import vc4.launcher.Package;
import vc4.launcher.Repo;
import vc4.launcher.gui.settings.RepoSettingsPanel;

public class RepoNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542093662709505459L;

	public RepoNode(Repo repo) {
		super(repo.getName(), new RepoSettingsPanel(repo));
		for(Package p : repo.getPackages()){
			add(new PackageNode(p));
		}
	}

}
