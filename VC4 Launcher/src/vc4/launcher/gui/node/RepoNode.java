package vc4.launcher.gui.node;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import vc4.launcher.gui.settings.RepoSettingsPanel;
import vc4.launcher.repo.Package;
import vc4.launcher.repo.Repo;

public class RepoNode extends SettingsNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542093662709505459L;
	private static Icon ico = new ImageIcon(RepoNode.class.getClassLoader().getResource("resources/icons/repo.png"));

	public RepoNode(Repo repo) {
		super(repo.getName(), new RepoSettingsPanel(repo));
		setIcon(ico);
		for(String s : repo.getTextFiles()){
			String r = s.contains(".") ? s.substring(0, s.lastIndexOf(".")) : s;
			add(new TextNode(r, repo.getUrl(s)));
		}
		for(Package p : repo.getPackages()){
			add(new PackageNode(p));
		}
	}

}
