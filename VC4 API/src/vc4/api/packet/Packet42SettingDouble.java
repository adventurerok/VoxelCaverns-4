package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;

public class Packet42SettingDouble extends Packet {

	public static final int ID = 42;

	int setting;
	double change;

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeInt(setting);
		out.writeDouble(change);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		setting = in.readInt();
		change = in.readDouble();
	}

	public Packet42SettingDouble() {
	}

	public Packet42SettingDouble(int setting, double change) {
		super();
		this.setting = setting;
		this.change = change;
	}

	@Override
	public int getId() {
		return ID;
	}

}
