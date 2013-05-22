package vc4.client.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import vc4.api.client.Client;
import vc4.api.io.BitOutputStream;

import vc4.api.io.BitInputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;

public class Server extends Thread{

	public Socket socket;
	BitInputStream in;
	BitOutputStream out;
	
	public Server(String ip, int port) throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
		out = new BitOutputStream(socket.getOutputStream());
		in = new BitInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Packet pack = readPacket();
				Client.getGame().handlePacket(pack);
			} catch (Exception e) {
				Logger.getLogger(Server.class).warning("Exception occured", e);
			}
		}
	}
	
	public void writePacket(Packet pack) throws IOException{
		out.writeByte((byte) pack.getId());
		pack.write(out);
		//out.flush();
	}
	
	public Packet readPacket() throws Exception{
		Packet packet = Packet.get(in.readByte());
		packet.read(in);
		return packet;
	}
}
