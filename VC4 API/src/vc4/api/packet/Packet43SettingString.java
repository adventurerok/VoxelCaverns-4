package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

public class Packet43SettingString extends Packet{
	
	public static final int ID = 43;
	
	/*
	 * Settings:
	 * 	0	chatname
	 * 
	 */
	
	public int setting;
	public String change;

	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeInt(setting);
		out.writeString(change);
	}

	@Override
	public void read(BitInputStream in) throws IOException {
		setting = in.readInt();
		change = in.readString();
	}
	
	public Packet43SettingString() {
	}

	public Packet43SettingString(int setting, String change) {
		super();
		this.setting = setting;
		this.change = change;
	}

	@Override
	public int getId() {
		return ID;
	}
	
}
