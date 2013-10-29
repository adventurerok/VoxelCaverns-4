package vc4.launcher.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import vc4.launcher.Launcher;
import vc4.launcher.gui.node.RepoNode;
import vc4.launcher.gui.settings.SettingsPanel;
import vc4.launcher.gui.settings.SettingsTree;
import vc4.launcher.repo.Repo;
import vc4.launcher.task.UpdateGuiTask;

public class SettingsTab extends JPanel {
	
	private JToolBar toolBar;
	SettingsTree tree;
	
	public SettingsTab() {
		setLayout(new BorderLayout(0, 0));
		
		tree = new SettingsTree();
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
		addRepo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String nRepo = JOptionPane.showInputDialog("URL of new repository: ");
				final Repo rep = new Repo();
				URL url;
				try {
					url = new URL(nRepo);
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Please input a proper URL");
					return;
				}
				if(rep.loadInfo(url)){
					if(Launcher.getSingleton().getRepos().contains(rep)) JOptionPane.showMessageDialog(null, "You already have that repository");
					else{
						Launcher.getSingleton().getRepos().add(new Repo());
						java.lang.Runnable run = new java.lang.Runnable() {

							@Override
							public void run() {
								tree.addNode(new RepoNode(rep));
							}
						};
						Launcher.getSingleton().getTasks().addTask(new UpdateGuiTask(run));
					}
				}
				else JOptionPane.showMessageDialog(null, "That is not a proper repository");
			}
			
		});
		toolBar.add(addRepo);
		JButton deleteRepo = new JButton(new ImageIcon(RepoNode.class.getClassLoader().getResource("resources/icons/repo_delete.png")));
		toolBar.add(deleteRepo);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 215431041641449818L;

}
