package vc4.impl.server;

import vc4.api.entity.EntityPlayer;
import vc4.api.logging.Logger;
import vc4.api.server.User;

public class ConsoleUser implements User {
	
	public static ConsoleUser CONSOLE = new ConsoleUser();

	static byte[] CONSOLE_UID = new byte[]{ 67, 79, 78, 83, 79, 76, 69, 32, 85, 83, 69, 82, 0, 0, 0, 0}; //CONSOLE USER
	
	@Override
	public byte[] getUid() {
		return CONSOLE_UID;
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
		Logger.getLogger("CMD").info(message);
	}

}
