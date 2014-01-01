package vc4.client.server;

import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.impl.server.ConsoleUser;

public class ClientUser extends ConsoleUser{
	
	public static ClientUser CLIENT_USER = new ClientUser();

	
	@Override
	public EntityPlayer getPlayer() {
		return Client.getPlayer();
	}
	
	@Override
	public void message(String message) {
		Client.getGame().printChatLine(message);
	}
	
	@Override
	public String getChatName() {
		return "player";
	}
	
	@Override
	public boolean isPlayer() {
		return true;
	}
	
	@Override
	public int getUserLevel() {
		return 0b110;
	}
}
