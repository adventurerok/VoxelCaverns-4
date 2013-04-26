package vc4.launcher.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import vc4.launcher.gui.node.RepoNode;
import vc4.launcher.gui.settings.SettingsPanel;
import vc4.launcher.gui.settings.SettingsTree;

public class SettingsTab extends JPanel {
	
	private JToolBar toolBar;
	
	public SettingsTab() {
		setLayout(new BorderLayout(0, 0));
		
		SettingsTree tree = new SettingsTree();
		tree.setRootVisible(false);
		tree.setPreferredSize(new Dimension(150, 64));
		add(new JScrollPane(tree), BorderLayout.WEST);
		SettingsPanel panel = new SettingsPanel();
		add(panel, BorderLayout.CENTER);
		toolBar = new JToolBar();
		setupToolbar();
		add(toolBar, BorderLayout.NORTH);
	}
	
	public void setupToolbar(){
		JButton addRepo = new JButton(new ImageIcon(RepoNode.class.getClassLoader().getResource("resources/icons/repo_add.png")));
		toolBar.add(addRepo);
		JButton deleteRepo = new JButton(new ImageIcon(RepoNode.class.getClassLoader().getResource("resources/icons/repo_delete.png")));
		toolBar.add(deleteRepo);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 215431041641449818L;

}
