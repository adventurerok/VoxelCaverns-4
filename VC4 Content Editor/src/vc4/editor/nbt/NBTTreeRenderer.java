package vc4.editor.nbt;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jnbt.NBTUtils;

public class NBTTreeRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6132129731738086370L;

	private ImageIcon[] icons = new ImageIcon[256];

	public NBTTreeRenderer() {
		Arrays.fill(icons, new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_unknown.png")));
		icons[1] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_byte.png"));
		icons[2] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_short.png"));
		icons[3] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_int.png"));
		icons[4] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_long.png"));
		icons[5] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_float.png"));
		icons[6] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_double.png"));
		icons[7] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_byte_array.png"));
		icons[8] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_string.png"));
		icons[9] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_list.png"));
		icons[10] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_compound.png"));
		icons[11] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_int_array.png"));
		icons[12] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_nibble.png"));
		icons[13] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_ebyte.png"));
		icons[14] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_eshort.png"));
		icons[15] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_eint.png"));
		icons[16] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_short_array.png"));
		icons[17] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_boolean.png"));
		icons[18] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_long_array.png"));
		icons[19] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_eshort_array.png"));
		icons[20] = new ImageIcon(NBTNode.class.getClassLoader().getResource("vc4/resources/icons/nbt_eint_array.png"));
	}

	public ImageIcon geticon(int id) {
		return icons[id];
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value instanceof NBTNode) {
			NBTNode node = (NBTNode) value;
			setIcon(icons[NBTUtils.getTypeCode(node.getTag().getClass())]);
		}
		return this;
	}

}
