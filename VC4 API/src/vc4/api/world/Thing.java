package vc4.api.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jnbt.*;

import vc4.api.vector.Vector3i;

public class Thing {

	Vector3i centre;
	Vector3i size;
	short[] blocks;
	byte[] data;
	ArrayList<CompoundTag> special;
	
	public int arrayPos(int x, int y, int z){
		return (x * size.y + y) * size.z + z;
	}
	
	
	
	public void save(OutputStream out) throws IOException{
		try(NBTOutputStream ot = new NBTOutputStream(out, true)){
			ot.writeTag(getSaveCompound());
		}
	}
	
	public CompoundTag getSaveCompound(){
		CompoundTag tag = new CompoundTag("root");
		tag.addTag(CompoundTag.createVector3iTag("centre", centre));
		tag.addTag(CompoundTag.createVector3iTag("size", size));
		tag.addTag(new ShortArrayTag("blocks", blocks));
		tag.addTag(new ByteArrayTag("data", data));
		tag.addTag(new ListTag("special", CompoundTag.class, (List<? extends Tag>)special));
		return tag;
	}
	
	public void loadSaveCompound(CompoundTag tag){
		centre = tag.getCompoundTag("centre").readVector3i();
		size = tag.getCompoundTag("size").readVector3i();
		blocks = tag.getShortArrayTag("blocks").getValue();
		data = tag.getByteArrayTag("data").getValue();
		special = new ArrayList<>();
		for(Tag t : tag.getListTag("special").getValue()){
			special.add((CompoundTag) t);
		}
	}



	public Thing(Vector3i centre, Vector3i size) {
		super();
		this.centre = centre;
		this.size = size;
	}
	
	public Thing(CompoundTag tag){
		loadSaveCompound(tag);
	}
	
	public Thing(InputStream in) throws IOException{
		try(NBTInputStream i = new NBTInputStream(in, true)){
			loadSaveCompound((CompoundTag) i.readTag());
		}
	}
}
