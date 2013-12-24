/**
 * 
 */
package vc4.api.server;

import vc4.api.packet.Packet;

/**
 * @author paul
 *
 */
public interface Server {

	public void sendPacket(Packet p);
	public void sendPacket(Packet p, User exclude);
	public void exit();
}
