package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

public class Packet3SUID extends Packet {

	
	public static final int ID = 3;
	
	public Packet3SUID() {
		super();
	}

	public byte message;
	public byte[] suid;
	
	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeByte(message);
		out.writeBytes(suid);
	}

	@Override
	public void read(BitInputStream in) throws IOException {
		message = in.readByte();
		suid = new byte[16];
		in.readBytes(suid);
	}

	public Packet3SUID(int message, byte[] suid) {
		super();
		this.message = (byte) message;
		this.suid = suid;
	}

	@Override
	public int getId() {
		return ID;
	}

}
