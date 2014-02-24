package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet41SettingInt extends Packet {
	
	public static final int ID = 41;
	
	int setting;
	int change;

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeInt(setting);
		out.writeInt(change);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		setting = in.readInt();
		change = in.readInt();
	}
	
	public Packet41SettingInt() {
	}

	public Packet41SettingInt(int setting, int change) {
		super();
		this.setting = setting;
		this.change = change;
	}

	@Override
	public int getId() {
		return ID;
	}

}
