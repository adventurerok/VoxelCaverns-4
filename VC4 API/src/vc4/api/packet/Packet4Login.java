package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet4Login extends Packet {

	public static final int ID = 4;

	public byte[] uid;

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.write(uid, 0, uid.length);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		uid = new byte[16];
		in.read(uid, 0, 16);
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
