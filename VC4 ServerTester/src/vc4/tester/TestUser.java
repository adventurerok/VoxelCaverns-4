package vc4.tester;

import vc4.api.entity.EntityPlayer;
import vc4.api.server.Server;
import vc4.api.server.User;

public class TestUser implements User {

	@Override
	public byte[] getUid() {
		return new byte[]{0};
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public void message(String message) {
		TesterConsole.getConsole().writeLine("MSG> " + message);
	}

	@Override
	public Server getServer() {
		return null;
	}

}
