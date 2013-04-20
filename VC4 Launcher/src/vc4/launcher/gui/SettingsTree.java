package vc4.launcher.gui;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import vc4.launcher.gui.settings.LauncherSettingsPanel;

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
			}
		});
		;
		addNode(new SettingsNode("Launcher", defaultPanel));
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
