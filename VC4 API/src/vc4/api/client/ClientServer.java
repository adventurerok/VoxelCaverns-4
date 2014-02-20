package vc4.api.client;

import java.io.IOException;

import vc4.api.packet.Packet;

public interface ClientServer {

	public void writePacket(Packet pack) throws IOException;
	public Packet readPacket() throws Exception;
	public void close() throws IOException;
	
}
