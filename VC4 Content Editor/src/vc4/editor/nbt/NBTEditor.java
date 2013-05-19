package vc4.editor.nbt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.jnbt.*;

import vc4.api.logging.Logger;

public class NBTEditor extends JFrame {

	private class MenuListener implements ActionListener, ItemListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("save")) save(file);
			else if (e.getActionCommand().equals("saveas")) save();
			else if (e.getActionCommand().equals("open")) load();
			else if (e.getActionCommand().equals("new")) clear();
			else if(e.getActionCommand().equals("reload")) load(file);
			else if (e.getActionCommand().equals("delete")) {
				if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) return;
				if (nbtTree.getSelectionPath().getPathCount() <= 1) return;
				NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
				NBTNode parent = (NBTNode) node.getParent();
				parent.removeNode(node);
			} else if (e.getActionCommand().equals("rename")) {
				if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) return;
				NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
				String name = JOptionPane.showInputDialog("New name");
				if (name == null || name.isEmpty()) return;
				node.setName(name);
				changes = true;
			} else if (e.getActionCommand().equals("changevalue")) {
				if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) return;
				NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
				editNode(node);
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!(e.getSource() instanceof JCheckBoxMenuItem)) return;
			JCheckBoxMenuItem itm = (JCheckBoxMenuItem) e.getSource();
			if (itm.getActionCommand().equals("gzip")) {
				gzip = itm.isSelected();
			} else if (itm.getActionCommand().equals("mc")) {
				if(mcCheck.isSelected() && hasExtras()){
					int res = JOptionPane.showConfirmDialog(null, "All VC-Extra tags will be deleted", "Your Document contains VC-Extras", JOptionPane.OK_CANCEL_OPTION);
					if(res == JOptionPane.CANCEL_OPTION){
						minecraft = false;
						mcCheck.setSelected(false);
						return;
					}
				}
				minecraft = itm.isSelected();
				minecraftChanged();
			}
		}

	}
	private class NBTListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changes = true;
			if (e.getActionCommand().startsWith("TAG_")) {
				int num = Integer.parseInt(e.getActionCommand().substring(4));
				Class<? extends Tag> t = NBTUtils.getTypeClass(num);
				String name = null;
				if (!selectedList()) {
					name = JOptionPane.showInputDialog("Tag name");
					if (name == null || name.isEmpty()) return;
				}
				try {
					Tag tag = t.getConstructor(String.class).newInstance(name);
					if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) {
						root.addTag(tag);
						getTreeModel().insertNodeInto(new NBTNode(tag), rootNode, rootNode.getChildCount());
						// rootNode.add(new NBTNode(tag));
						((DefaultTreeModel) nbtTree.getModel()).reload(rootNode);
					} else {
						NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
						if (node.getTag() instanceof CompoundTag || node.getTag() instanceof ListTag) {
							node.addTag(tag);
							((DefaultTreeModel) nbtTree.getModel()).reload(node);
						} else {
							if (node.getParent() instanceof NBTNode) {
								NBTNode parent = (NBTNode) node.getParent();
								parent.addTag(tag);
								((DefaultTreeModel) nbtTree.getModel()).reload(parent);
							} else {
								root.addTag(tag);
								getTreeModel().insertNodeInto(new NBTNode(tag), rootNode, rootNode.getChildCount());
								((DefaultTreeModel) nbtTree.getModel()).reload(rootNode);
							}
						}
					}
					// nbtTree.repaint();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					Logger.getLogger(NBTListener.class).warning("Exception occured", e1);
				}
			} else if (e.getActionCommand().startsWith("LIST_")) {
				int num = Integer.parseInt(e.getActionCommand().substring(5));
				Class<? extends Tag> t = NBTUtils.getTypeClass(num);
				String name = JOptionPane.showInputDialog("Tag name");
				ListTag tag = new ListTag(name, t);
				if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) {
					root.addTag(tag);
					getTreeModel().insertNodeInto(new NBTNode(tag), rootNode, rootNode.getChildCount());
					// rootNode.add(new NBTNode(tag));
					((DefaultTreeModel) nbtTree.getModel()).reload(rootNode);
				} else {
					NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
					if (node.getTag() instanceof CompoundTag || node.getTag() instanceof ListTag) {
						node.addTag(tag);
						((DefaultTreeModel) nbtTree.getModel()).reload(node);
					} else {
						if (node.getParent() instanceof NBTNode) {
							NBTNode parent = (NBTNode) node.getParent();
							parent.addTag(tag);
							((DefaultTreeModel) nbtTree.getModel()).reload(parent);
						} else {
							root.addTag(tag);
							getTreeModel().insertNodeInto(new NBTNode(tag), rootNode, rootNode.getChildCount());
							((DefaultTreeModel) nbtTree.getModel()).reload(rootNode);
						}
					}
				}
			} else if (e.getActionCommand().equals("DELETE")) {
				if (nbtTree.getSelectionPath() == null || !(nbtTree.getSelectionPath().getLastPathComponent() instanceof NBTNode)) return;
				if (nbtTree.getSelectionPath().getPathCount() <= 1) return;
				NBTNode node = (NBTNode) nbtTree.getSelectionPath().getLastPathComponent();
				NBTNode parent = (NBTNode) node.getParent();
				parent.removeNode(node);
			}

		}

	}

	private class NBTTree extends JTree {
		private static final long serialVersionUID = 4713486822324717642L;

		public NBTTree(TreeNode root) {
			super(root);
		}

		@Override
		public String getToolTipText() {
			if (getParent() == null || getParent().getMousePosition() == null) return "NBT Tree";
			int selRow = getRowForLocation(getParent().getMousePosition().x, getParent().getMousePosition().y);
			TreePath selPath = getPathForLocation(getParent().getMousePosition().x, getParent().getMousePosition().y);
			if (selRow != -1) {
				if (selPath.getLastPathComponent() instanceof NBTNode) {
					NBTNode node = (NBTNode) selPath.getLastPathComponent();
					return node.getToolTipText();
				}
			}

			return "NBT Tree";
		}

	}

	/**
	 * 
	 */
	private static NBTEditor singleton;
	private static final long serialVersionUID = 7396838719143219232L;
	public static NBTEditor getSingleton() {
		return singleton;
	}
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		NBTEditor e = new NBTEditor();
		for(int d = 0; d < args.length; ++d){
			String s = args[d].toLowerCase();
			if(s.equals("-d")) e.exitOnClose = false;
			else if(s.equals("-o")){
				if(args.length > d + 1){
					String file = args[d + 1];
					e.file = new File(file);
					e.load(e.file);
					++d;
				}
			}
		}
		e.setVisible(true);
	}
	private CompoundTag root = new CompoundTag("root");
	private NBTNode rootNode;
	private NBTTree nbtTree;
	private JToolBar addBar = new JToolBar();
	private JMenuBar menuBar = new JMenuBar();
	private boolean gzip = true;
	private boolean minecraft = false;
	private JPopupMenu list;
	private JToggleButton listButton;
	private NBTTreeRenderer nbtRender;
	private JMenuItem rename;
	private boolean changes = false;
	private boolean exitOnClose = true;

	private File file;

	JFileChooser chooser = new JFileChooser();

	private JCheckBoxMenuItem gzipCheck;

	private JCheckBoxMenuItem mcCheck;

	private ArrayList<AbstractButton> vcExtras = new ArrayList<>();

	public NBTEditor() {
		singleton = this;
		setTitle("VC4 VBT/NBT Editor");
		rootNode = new NBTNode(root);
		initTree();
		setIconImage(nbtRender.geticon(10).getImage());
		addToolbarButtons();
		add(addBar, BorderLayout.WEST);
		addMenubarButtons();
		add(menuBar, BorderLayout.NORTH);
		nbtTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selRow = nbtTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = nbtTree.getPathForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 2) {
						nodeClick(selRow, (DefaultMutableTreeNode) selPath.getLastPathComponent());
					}
				}
			}
		});
		setSize(400, 625);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(changes){
					int i = JOptionPane.showConfirmDialog(null, "You will lose these changes if you press no", "Your document contains unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION);
					if(i == JOptionPane.CANCEL_OPTION) return;
					else if(i == JOptionPane.YES_OPTION){
						if(!save(file)) return;
					}
				}
				dispose();
				if(exitOnClose){
					System.exit(0);
				}
			}
		});
	}

	private void addMenubarButtons() {
		JMenu file = new JMenu("File");
		JMenuItem clear = new JMenuItem("New");
		clear.setActionCommand("new");
		clear.addActionListener(new MenuListener());
		clear.setAccelerator(KeyStroke.getKeyStroke("control N"));
		clear.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/document_new.png")));
		file.add(clear);
		JMenuItem open = new JMenuItem("Open");
		open.setActionCommand("open");
		open.addActionListener(new MenuListener());
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));
		open.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/document_open.png")));
		file.add(open);
		JMenuItem reload = new JMenuItem("Reload");
		reload.setActionCommand("reload");
		reload.addActionListener(new MenuListener());
		reload.setAccelerator(KeyStroke.getKeyStroke("control L"));
		reload.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/document_reload.png")));
		file.add(reload);
		file.add(new JSeparator());
		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand("save");
		save.addActionListener(new MenuListener());
		save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		save.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/document_save.png")));
		file.add(save);
		JMenuItem saveas = new JMenuItem("Save As");
		saveas.setActionCommand("saveas");
		saveas.addActionListener(new MenuListener());
		saveas.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		saveas.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/document_save_as.png")));
		file.add(saveas);
		menuBar.add(file);
		JMenu edit = new JMenu("Edit");
		JMenuItem delete = new JMenuItem("Delete");
		delete.setActionCommand("delete");
		delete.addActionListener(new MenuListener());
		delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		delete.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/edit_remove.png")));
		edit.add(delete);
		edit.add(new JSeparator());
		rename = new JMenuItem("Rename");
		rename.setActionCommand("rename");
		rename.addActionListener(new MenuListener());
		rename.setAccelerator(KeyStroke.getKeyStroke("control R"));
		//name.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/edit_rename.png")));
		edit.add(rename);
		JMenuItem value = new JMenuItem("Change Value");
		value.setActionCommand("changevalue");
		value.addActionListener(new MenuListener());
		value.setAccelerator(KeyStroke.getKeyStroke("ENTER"));
		value.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/edit_edit.png")));
		edit.add(value);
		edit.add(new JSeparator());
		gzipCheck = new JCheckBoxMenuItem("G-Zip");
		gzipCheck.setActionCommand("gzip");
		gzipCheck.setSelected(gzip);
		gzipCheck.addItemListener(new MenuListener());
		gzipCheck.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/edit_gzip.png")));
		gzipCheck.setToolTipText("If unchecked this will not load with VoxelCaverns");
		edit.add(gzipCheck);
		mcCheck = new JCheckBoxMenuItem("Minecraft Fomat");
		mcCheck.setActionCommand("mc");
		mcCheck.setSelected(minecraft);
		mcCheck.addItemListener(new MenuListener());
		mcCheck.setIcon(new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/edit_minecraft.png")));
		mcCheck.setToolTipText("If checked this will not load with VoxelCaverns");
		edit.add(mcCheck);
		menuBar.add(edit);
	}

	private void addToolbarButtons() {
		addBar.setFloatable(false);
		addBar.setOrientation(JToolBar.VERTICAL);
		for (int d = 1; d < 21; ++d) {
			if (d == NBTConstants.TYPE_LIST) continue;
			JButton button = new JButton(nbtRender.geticon(d));
			button.setActionCommand("TAG_" + d);
			button.addActionListener(new NBTListener());
			button.setToolTipText("Create " + NBTUtils.getSimpleName(NBTUtils.getTypeClass(d)) + " Tag");
			addBar.add(button);
			if (d > 11) vcExtras.add(button);
		}
		list = new JPopupMenu("List");
		listButton = new JToggleButton(nbtRender.geticon(NBTConstants.TYPE_LIST));
		listButton.setToolTipText("Create List Tag");
		listButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					list.show(addBar, addBar.getWidth(), listButton.getY() - list.getHeight() / 2);
				}
			}
		});
		listButton.setFocusable(false);
		listButton.setHorizontalTextPosition(SwingConstants.LEADING);
		for (int d = 1; d < 21; ++d) {
			if (d == NBTConstants.TYPE_LIST) continue;
			JMenuItem button = new JMenuItem(nbtRender.geticon(d));
			button.setActionCommand("LIST_" + d);
			button.addActionListener(new NBTListener());
			button.setToolTipText("Create List of " + NBTUtils.getSimpleName(NBTUtils.getTypeClass(d)) + " Tags");
			list.add(button);
			if (d > 11) vcExtras.add(button);
		}
		list.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				listButton.setSelected(false);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				listButton.setSelected(false);
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
		});
		addBar.add(listButton);

	}

	private boolean checkCompoundTagForExtras(CompoundTag tag){
		for(Tag t : tag.getValue().values()){
			if(NBTUtils.getTypeCode(t.getClass()) > 11) return true;
			if(t instanceof CompoundTag){
				if(checkCompoundTagForExtras((CompoundTag) t)) return true;
			} else if(t instanceof ListTag){
				if(NBTUtils.getTypeCode(((ListTag)t).getType()) > 11) return true;
			}
		}
		return false;
	}

	public void clear() {
		root = new CompoundTag("root");
		rootNode = new NBTNode(root);
		nbtTree.setModel(new DefaultTreeModel(rootNode));
		file = null;
		minecraft = false;
		gzip = true;
		changes = false;
	}

	public void editNode(NBTNode node){
		if (NBTUtils.getTypeCode(node.getTag().getClass()) == NBTConstants.TYPE_COMPOUND || NBTUtils.getTypeCode(node.getTag().getClass()) == NBTConstants.TYPE_LIST) return;
		if (NBTUtils.getTypeName(node.getTag().getClass()).contains("Array")) return;
		if(NBTUtils.getTypeCode(node.getTag().getClass()) == NBTConstants.TYPE_BOOLEAN){
			int i = JOptionPane.showOptionDialog(null, "New value", "Editing boolean tag", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"True", "False", "Cancel"}, "False");
			if(i == 0) node.setUserObject(true);
			else if(i == 1) node.setUserObject(false);
			else return;
		} else {
			String result = JOptionPane.showInputDialog(null, "New value");
			if (result == null || result.isEmpty()) return;
			node.setUserObject(result);
		}
		((DefaultTreeModel) nbtTree.getModel()).nodeChanged(node);
		changes = true;
	}

	public NBTTree getNbtTree() {
		return nbtTree;
	}

	public DefaultTreeModel getTreeModel() {
		return ((DefaultTreeModel) nbtTree.getModel());
	}
	
	public boolean hasExtras() {
		return checkCompoundTagForExtras(root);
	}

	public void initTree() {
		nbtTree = new NBTTree(rootNode);
		nbtTree.setCellRenderer(nbtRender = new NBTTreeRenderer());
		DefaultTreeSelectionModel d = new DefaultTreeSelectionModel();
		d.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		nbtTree.setSelectionModel(d);
		nbtTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				rename.setEnabled(!selectedListItem());
			}
		});
		JScrollPane treeView = new JScrollPane(nbtTree);
		add(treeView);
		ToolTipManager.sharedInstance().registerComponent(nbtTree);
	}

	private void load() {
		chooser.setPreferredSize(new Dimension(800, 600));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			load(f);
		}
	}

	private void load(File f) {
		if(f == null) return;
		try {
			NBTInputStream in = new NBTInputStream(new FileInputStream(f));
			root = (CompoundTag) in.readTag();
			in.close();
			file = f;
			rootNode = new NBTNode(root);
			nbtTree.setModel(new DefaultTreeModel(rootNode));
			gzip = true;
			minecraft = false;
		} catch (IOException e) {
			System.out.println("Failed to load with GZip, attempting without");
			e.printStackTrace();
			try {
				NBTInputStream in = new NBTInputStream(new FileInputStream(f), false);
				root = (CompoundTag) in.readTag();
				nbtTree.setModel(new DefaultTreeModel(rootNode));
				in.close();
				file = f;
				gzip = false;
				minecraft = false;
			} catch (IOException e1) {
				System.out.println("Failed to load file with VC format, attempting minecraft");
				try {
					MCInputStream in = new MCInputStream(new FileInputStream(f));
					root = (CompoundTag) in.readTag();
					in.close();
					file = f;
					rootNode = new NBTNode(root);
					nbtTree.setModel(new DefaultTreeModel(rootNode));
					gzip = true;
					minecraft = true;
				} catch (IOException e2) {
					System.out.println("Failed to load with GZip MC, attempting without");
					try {
						MCInputStream in = new MCInputStream(new FileInputStream(f), false);
						root = (CompoundTag) in.readTag();
						rootNode = new NBTNode(root);
						nbtTree.setModel(new DefaultTreeModel(rootNode));
						in.close();
						file = f;
						gzip = false;
						minecraft = true;
					} catch (IOException e3) {
						System.out.println("Failed to load file with MC format");
					}
				}
			}
		}
		changes = false;
		gzipCheck.setSelected(gzip);
		mcCheck.setSelected(minecraft);
		minecraftChanged();
	}

	public void minecraftChanged() {
		for (AbstractButton b : vcExtras) {
			b.setEnabled(!minecraft);
		}
		if(minecraft) removeExtras();
	}

	protected void nodeClick(int selRow, DefaultMutableTreeNode lastPathComponent) {
		if (!(lastPathComponent instanceof NBTNode)) return;
		NBTNode node = (NBTNode) lastPathComponent;
		editNode(node);

	}

	public void removeExtras(){
		removeExtras(rootNode);
	}

	private void removeExtras(NBTNode node) {
		ArrayList<NBTNode> toRemove = new ArrayList<>();
		for(int d = 0; d < node.getChildCount(); ++d){
			NBTNode kid = (NBTNode) node.getChildAt(d);
			if(NBTUtils.getTypeCode(kid.getTag().getClass()) > 11) toRemove.add(kid);
			if(kid.getTag() instanceof CompoundTag){
				removeExtras(kid);
			} else if(kid.getTag() instanceof ListTag){
				if(NBTUtils.getTypeCode(((ListTag)kid.getTag()).getType()) > 11) toRemove.add(kid);
			}
		}
		for(NBTNode s : toRemove){
			node.removeNode(s);
		}
	}
	
	private boolean save() {
		chooser.setPreferredSize(new Dimension(800, 600));
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			save(f);
			return true;
		}
		return false;
	}
	
	private boolean save(File f) {
		if(f == null){
			return save();
		}
		if (!minecraft) {
			NBTOutputStream out;
			try {
				out = new NBTOutputStream(new FileOutputStream(f), gzip);
				out.writeTag(root);
				out.close();
				file = f;
			} catch (IOException e) {
				Logger.getLogger(NBTEditor.class).warning("Exception occured", e);
			}
		} else {
			MCOutputStream out;
			try {
				out = new MCOutputStream(new FileOutputStream(f), gzip);
				out.writeTag(root);
				out.close();
				file = f;
			} catch (IOException e) {
				Logger.getLogger(NBTEditor.class).warning("Exception occured", e);
			}
		}
		changes = false;
		return true;
	}

	public boolean selectedList() {
		TreePath selPath = nbtTree.getSelectionPath();
		if (selPath == null) return false;
		if (!(selPath.getLastPathComponent() instanceof NBTNode)) return false;
		NBTNode node = (NBTNode) selPath.getLastPathComponent();
		if (node.getTag() instanceof ListTag) return true;
		if (node.getTag() instanceof CompoundTag) return false;
		if (!(node.getParent() instanceof NBTNode)) return false;
		node = (NBTNode) node.getParent();
		if (node.getTag() instanceof ListTag) return true;
		return false;
	}
	
	public boolean selectedListItem(){
		TreePath selPath = nbtTree.getSelectionPath();
		if (selPath == null) return false;
		if (!(selPath.getLastPathComponent() instanceof NBTNode)) return false;
		NBTNode node = (NBTNode) selPath.getLastPathComponent();
		if (!(node.getParent() instanceof NBTNode)) return false;
		node = (NBTNode) node.getParent();
		if (node.getTag() instanceof ListTag) return true;
		return false;
	}

}
