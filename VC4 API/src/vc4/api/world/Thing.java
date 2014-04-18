package vc4.api.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import vc4.api.vbt.*;
import vc4.api.vector.Vector3i;

public class Thing {

	Vector3i centre;
	Vector3i size;
	short[] blocks;
	byte[] data;
	ArrayList<TagCompound> special;

	public int arrayPos(int x, int y, int z) {
		return (x * size.y + y) * size.z + z;
	}

	public void save(OutputStream out) throws IOException {
		try (NBTOutputStream ot = new NBTOutputStream(out, true)) {
			ot.writeTag(getSaveCompound());
		}
	}

	public TagCompound getSaveCompound() {
		TagCompound tag = new TagCompound("root");
		tag.addTag(TagCompound.createVector3iTag("centre", centre));
		tag.addTag(TagCompound.createVector3iTag("size", size));
		tag.addTag(new TagShortArray("blocks", blocks));
		tag.addTag(new TagByteArray("data", data));
		tag.addTag(new TagList("special", TagCompound.class, (List<? extends Tag>) special));
		return tag;
	}

	public void loadSaveCompound(TagCompound tag) {
		centre = tag.getCompoundTag("centre").readVector3i();
		size = tag.getCompoundTag("size").readVector3i();
		blocks = tag.getShortArrayTag("blocks").getValue();
		data = tag.getByteArrayTag("data").getValue();
		special = new ArrayList<>();
		for (Tag t : tag.getListTag("special").getValue()) {
			special.add((TagCompound) t);
		}
	}

	public Thing(Vector3i centre, Vector3i size) {
		super();
		this.centre = centre;
		this.size = size;
	}

	public Thing(TagCompound tag) {
		loadSaveCompound(tag);
	}

	public Thing(InputStream in) throws IOException {
		try (NBTInputStream i = new NBTInputStream(in, true)) {
			loadSaveCompound((TagCompound) i.readTag());
		}
	}
}
