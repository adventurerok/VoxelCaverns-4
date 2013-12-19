package vc4.api.packet;

import java.io.IOException;

import org.jnbt.Tag;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

public class Packet3NBT extends Packet {
	
	/*
	 * Type Index:
	 * 	0	Client details
	 * 	1	Server details
	 */
	
	public short type;
	public Tag tag;

	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeShort(type);
		out.writeNbt(tag);
	}

	@Override
	public void read(BitInputStream in) throws IOException {
		type = in.readShort();
		tag = in.readNbt();
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public short getType() {
		return type;
	}
	
	public Packet3NBT setMessage(int type, Tag tag){
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
		return 3;
	}

}
