package vc4.tester;

import vc4.api.entity.EntityPlayer;
import vc4.api.packet.Packet;
import vc4.api.server.Group;
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

	@Override
	public int getPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPermission(String permission, int change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermission(String permission, boolean change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChatName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeChatName(String change) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeChatName(String change, boolean addNumbers) {
		// TODO Auto-generated method stub
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
		return 0b100;
	}

}
