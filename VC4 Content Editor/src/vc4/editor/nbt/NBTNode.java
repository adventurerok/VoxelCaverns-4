package vc4.editor.nbt;

import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.NBTConstants;
import org.jnbt.NBTUtils;
import org.jnbt.Tag;

public class NBTNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7951144350739450797L;
	
	private Tag tag;
	
	public Tag getTag() {
		return tag;
	}

	public NBTNode(Tag tag) {
		super(getString(tag));
		this.tag = tag;
		if(NBTUtils.getTypeCode(tag.getClass()) == NBTConstants.TYPE_COMPOUND){
			calculateChildren();
		} else if(NBTUtils.getTypeCode(tag.getClass()) == NBTConstants.TYPE_LIST){
			calculateList();
		}
	}

	private void calculateList() {
		ListTag list = (ListTag) tag;
		for(Tag t : list.getValue()){
			add(new NBTNode(t));
		}
		
	}

	@Override
	public Object getUserObject() {
		return tag.getValue();
	}
	
	@Override
	public void setUserObject(Object userObject) {
		try{
			tag.setValue(Double.parseDouble(userObject.toString()));
		} catch(Exception e){
			tag.setValue(userObject);
		}
		super.setUserObject(getString(tag));
	}

	private void calculateChildren() {
		CompoundTag cm = (CompoundTag) tag;
		for(Entry<String, Tag> o : cm.getValue().entrySet()){
			add(calculateTag(o.getValue()));
		}
		
	}

	private NBTNode calculateTag(Tag value) {
		return new NBTNode(value);
	}

	private static String getString(Tag tag) {
		switch(NBTUtils.getTypeCode(tag.getClass())){
			case NBTConstants.TYPE_BOOLEAN:
			case NBTConstants.TYPE_BYTE:
			case NBTConstants.TYPE_DOUBLE:
			case NBTConstants.TYPE_EBYTE:
			case NBTConstants.TYPE_EINT:
			case NBTConstants.TYPE_ESHORT:
			case NBTConstants.TYPE_FLOAT:
			case NBTConstants.TYPE_INT:
			case NBTConstants.TYPE_LONG:
			case NBTConstants.TYPE_NIBBLE:
			case NBTConstants.TYPE_SHORT:
			case NBTConstants.TYPE_STRING:
				if(tag.getName() != null && !tag.getName().isEmpty()) return tag.getName() + ": " + tag.getValue();
				else return tag.getValue().toString();
			case NBTConstants.TYPE_COMPOUND:
				if(tag.getName() != null && !tag.getName().isEmpty()) return tag.getName() + ": " + ((CompoundTag)tag).getValue().size() + " entries";
				else return ((CompoundTag)tag).getValue().size() + " entries";
			case NBTConstants.TYPE_LIST:
				if(tag.getName() != null && !tag.getName().isEmpty()) return tag.getName() + ": " + ((ListTag)tag).getValue().size() + " entries of type " + NBTUtils.getSimpleName(((ListTag)tag).getType());
				else return ((ListTag)tag).getValue().size() + " entries of type " + NBTUtils.getSimpleName(((ListTag)tag).getType());
			default:
				if(tag.getName() != null && !tag.getName().isEmpty()) return tag.getName() + ": " + NBTUtils.getTypeName(tag.getClass());
				else return NBTUtils.getTypeName(tag.getClass());
		}
	}

	public String getToolTipText() {
		return NBTUtils.getTypeName(tag.getClass());
	}

	public void addTag(Tag t) {
		if(tag instanceof CompoundTag){
			((CompoundTag)tag).addTag(t);
			add(new NBTNode(t));
		} else if(tag instanceof ListTag){
			ListTag l = (ListTag) tag;
			if(!l.getType().isInstance(t)) return;
			l.addTag(t);
			NBTEditor.getSingleton().getTreeModel().insertNodeInto(new NBTNode(t), this, getChildCount());
		}
		super.setUserObject(getString(tag));
	}

	public void removeNode(NBTNode node) {
		if(tag instanceof CompoundTag){
			((CompoundTag)tag).remove(node.getTag().getName());
		} else if(tag instanceof ListTag){
			ListTag l = (ListTag) tag;
			l.getValue().remove(node.getTag());
		}
		NBTEditor.getSingleton().getTreeModel().removeNodeFromParent(node);
		super.setUserObject(getString(tag));
		
	}

	public void setName(String name) {
		tag.setName(name);
		super.setUserObject(getString(tag));
		NBTEditor.getSingleton().getTreeModel().nodeChanged(this);
		
	}
	
	

}
