package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet3SUID extends Packet {

	
	public static final int ID = 3;
	
	public Packet3SUID() {
		super();
	}
	
	/*
	 * Message:
	 * 	0	You requested a new ID, here it is
	 * 	1	Server ID (The server's unique ID, shoudn't change)
	 * 
	 */

	public byte message;
	public byte[] suid;
	
	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeByte(message);
		out.write(suid, 0, suid.length);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		message = in.readByte();
		suid = new byte[16];
		in.read(suid, 0, suid.length);
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
