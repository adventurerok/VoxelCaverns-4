package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

public class Packet5Chat extends Packet {
	
	public static final int ID = 5;
	
	public String name;
	public String prefix;
	public String msg;
	
	public Packet5Chat() {
	}
	
	public Packet5Chat(String name, String prefix, String msg) {
		super();
		this.name = name;
		this.prefix = prefix;
		this.msg = msg;
	}

	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeString(name);
		out.writeString(prefix);
		out.writeString(msg);
	}

	@Override
	public void read(BitInputStream in) throws IOException {
		name = in.readString();
		prefix = in.readString();
		msg = in.readString();
	}

	@Override
	public int getId() {
		return ID;
	}

}
