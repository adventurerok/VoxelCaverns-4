package vc4.api.packet;

import java.io.IOException;

import org.jnbt.Tag;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet40NBT extends Packet {

	public static final int ID = 40;

	/*
	 * Type Index: 0 Client details 1 Server details 2 Dictionary
	 */

	public short type;
	public Tag tag;

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeShort(type);
		out.writeVBT(tag);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		type = in.readShort();
		tag = in.readVBT();
	}

	public Tag getTag() {
		return tag;
	}

	public short getType() {
		return type;
	}

	public Packet40NBT setMessage(int type, Tag tag) {
		this.type = (short) type;
		this.tag = tag;
		return this;
	}

	@Override
	public String toString() {
		return "Type: " + type + "\n\tTag: " + tag.toString();
	}

	@Override
	public int getId() {
		return ID;
	}

}
