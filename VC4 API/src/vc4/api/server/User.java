/**
 * 
 */
package vc4.api.server;

import vc4.api.entity.EntityPlayer;
import vc4.api.packet.Packet;

/**
 * @author paul
 * 
 */
public interface User {

	public byte[] getUid();

	public boolean hasPermission(String permission);

	public int getPermission(String permission);

	public void setPermission(String permission, int change);

	public void setPermission(String permission, boolean change);

	public EntityPlayer getPlayer();

	public void message(String message);

	public Server getServer();

	public String getChatName();

	public boolean changeChatName(String change);

	public boolean changeChatName(String change, boolean addNumbers);

	public Group getGroup();

	public void setGroup(Group g);

	public void sendPacket(Packet p);

	public boolean isUser();

	public boolean isPlayer();

	/**
	 * Gets the capabilities level of the user 100 is if the user has console capabilities 010 is if the user has player capabilities 001 is if the user has user capabilies 11 is all
	 * 
	 * @return The user capabilities level
	 */
	public int getUserLevel();

}
