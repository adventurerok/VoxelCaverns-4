package vc4.launcher.gui.settings;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import vc4.launcher.Launcher;
import vc4.launcher.gui.node.RepoNode;
import vc4.launcher.gui.node.SettingsNode;
import vc4.launcher.repo.Repo;

public class SettingsTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = -577911560947309754L;
	private static SettingsTree singleton;
	
	private static JPanel defaultPanel;
	private static SettingsNode root;
	
	public static SettingsTree getSingleton() {
		return singleton;
	}
	
	public SettingsTree() {
		super(root = new SettingsNode("ROOT", defaultPanel = new LauncherSettingsPanel()));
		singleton = this;
		addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				SettingsPanel.getSingleton().setPanel(getSelectedPanel());
				SettingsPanel.getSingleton().setBorder(BorderFactory.createTitledBorder(getSelectedTitle()));
				SettingsPanel.getSingleton().repaint();
			}
		});
		setCellRenderer(new DefaultTreeCellRenderer(){

			private static final long serialVersionUID = 8860739105196982193L;
			
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				if(value instanceof SettingsNode){
					setIcon(((SettingsNode)value).getIcon());
				}
				return this;
			}
			
		});
		addNode(new SettingsNode("Launcher", defaultPanel).setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/icons/settings.png"))));
		for(Repo r : Launcher.getSingleton().getRepos()){
			addNode(new RepoNode(r));
		}
	}
	
	protected String getSelectedTitle() {
		TreePath selPath = getSelectionPath();
		if(selPath == null || selPath.getPathCount() < 1) return "Launcher";
		SettingsNode node = (SettingsNode) selPath.getLastPathComponent();
		return node.getUserObject().toString();
	}

	public void addNode(SettingsNode node){
		((DefaultTreeModel)getModel()).insertNodeInto(node, root, root.getChildCount());
		expandPath(new TreePath(((DefaultTreeModel)getModel()).getPathToRoot(root)));
	}
	
	public static JPanel getDefaultPanel() {
		return defaultPanel;
	}
	
	public static void setDefaultPanel(JPanel defaultPanel) {
		SettingsTree.defaultPanel = defaultPanel;
	}
	
	public JPanel getSelectedPanel(){
		TreePath selPath = getSelectionPath();
		if(selPath == null || selPath.getPathCount() < 1) return defaultPanel;
		SettingsNode node = (SettingsNode) selPath.getLastPathComponent();
		return node.getSettingsPanel();
	}

}
