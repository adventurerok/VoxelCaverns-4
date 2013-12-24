/**
 * 
 */
package vc4.api.server;

import vc4.api.entity.EntityPlayer;

/**
 * @author paul
 *
 */
public interface User {

	public byte[] getUid();
	public boolean hasPermission(String permission);
	public EntityPlayer getPlayer();
	public void message(String message);
	public Server getServer();
	
}
