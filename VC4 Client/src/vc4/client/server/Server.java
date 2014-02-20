package vc4.client.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.*;

import vc4.api.client.Client;
import vc4.api.client.ClientServer;
import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;

public class Server extends Thread implements ClientServer{

	public Socket socket;
	BitInputStream in;
	BitOutputStream out;
	private boolean open = true;
	
	public Server(String host) throws UnknownHostException, IOException{
		Logger.getLogger("VC4").info("Connecting to " + host);
		int colon = host.lastIndexOf(":");
		String address = colon == -1 ? host : host.substring(0, colon);
		int port = 4775;
		if(colon != -1){
			try{
				port = Integer.parseInt(host.substring(colon + 1));
			} catch(Exception e){}
		}
		socket = new Socket(address, port);
		out = new BitOutputStream(socket.getOutputStream());
		in = new BitInputStream(socket.getInputStream());
		setDaemon(true);
	}
	
	public Server(String ip, int port) throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
		out = new BitOutputStream(socket.getOutputStream());
		in = new BitInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		while(open){
			try {
				Packet pack = readPacket();
				Client.getGame().handlePacket(pack);
			} catch (Exception e) {
				if((e instanceof SocketException) || (e instanceof EOFException)){
					if(open)Logger.getLogger("VC4").info("Lost connection to server");
				} else Logger.getLogger(Server.class).warning("Error while reading from server", e);
				try {
					close();
				} catch (IOException e1) {
					Logger.getLogger(Server.class).warning("Error while closing", e1);
				}
				break;
			}
		}
	}
	
	@Override
	public void writePacket(Packet pack) throws IOException{
		out.writeByte((byte) pack.getId());
		pack.write(out);
		//out.flush();
	}
	
	@Override
	public Packet readPacket() throws Exception{
		Packet packet = Packet.get(in.readByte());
		packet.read(in);
		return packet;
	}
	
	@Override
	public void close() throws IOException {
		open = false;
		socket.close();
	}
}
