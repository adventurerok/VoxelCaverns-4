package vc4.impl.server;

import vc4.api.entity.EntityPlayer;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.api.server.Group;
import vc4.api.server.Server;
import vc4.api.server.User;

public class ConsoleUser implements User {
	
	private static Server server;
	
	public static void setServer(Server server) {
		ConsoleUser.server = server;
	}
	
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

	@Override
	public Server getServer() {
		return server;
	}

	@Override
	public int getPermission(String permission) {
		return 1;
	}

	@Override
	public void setPermission(String permission, int change) {
	}

	@Override
	public void setPermission(String permission, boolean change) {
	}

	@Override
	public String getChatName() {
		return "console";
	}

	@Override
	public boolean changeChatName(String change) {
		return false;
	}

	@Override
	public boolean changeChatName(String change, boolean addNumbers) {
		return false;
	}

	@Override
	public Group getGroup() {
		return null;
	}

	@Override
	public void setGroup(Group g) {
	}

	@Override
	public void sendPacket(Packet p) {
	}

	@Override
	public boolean isUser() {
		return false;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public int getUserLevel() {
		return 4;
	}

}
