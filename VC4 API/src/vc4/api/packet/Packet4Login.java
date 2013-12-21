package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

public class Packet4Login extends Packet{
	
	public static final int ID = 4;
	
	byte[] uid;

	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeBytes(uid);
	}

	@Override
	public void read(BitInputStream in) throws IOException {
		uid = new byte[16];
		in.readBytes(uid);
	}

	@Override
	public int getId() {
		return ID;
	}

	public Packet4Login(byte[] uid) {
		super();
		this.uid = uid;
	}

	public Packet4Login() {
		super();
	}
	
	public Packet4Login setUid(byte[] uid) {
		this.uid = uid;
		return this;
	}

}
