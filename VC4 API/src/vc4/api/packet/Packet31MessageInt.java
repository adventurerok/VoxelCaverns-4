package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet31MessageInt extends Packet {
	
	public static final int ID = 31;
	
	/*
	 * Message index:
	 * 0	New SUID (Request a new SUID)
	 */
	
	public int message;

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeInt(message);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		message = in.readInt();
	}
	
	public Packet31MessageInt setMessage(int message) {
		this.message = message;
		return this;
	}
	
	

	@Override
	public int getId() {
		return ID;
	}

	public Packet31MessageInt(int message) {
		super();
		this.message = message;
	}

	public Packet31MessageInt() {
		super();
	}
	
	@Override
	public String toString() {
		return "Message: " + message;
	}

}
