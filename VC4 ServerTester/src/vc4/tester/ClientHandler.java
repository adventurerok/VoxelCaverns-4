/**
 * 
 */
package vc4.tester;

import java.io.EOFException;
import java.io.IOException;
import java.net.*;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.*;



/**
 * @author paul
 *
 */
public class ClientHandler extends Thread{

	private Socket socket;
	private boolean open = true;
	private BitInputStream in;
	private BitOutputStream out;
	
	public ClientHandler(String ip) throws UnknownHostException, IOException{
		Logger.getLogger("TST").info("Connecting to " + ip);
		String parts[] = ip.split(":");
		String address = parts[0];
		int port = 4775;
		if(parts.length > 1){
			try{
				port = Integer.parseInt(parts[1]);
			} catch(Exception e){}
		}
		socket = new Socket(address, port);
		out = new BitOutputStream(socket.getOutputStream());
		in = new BitInputStream(socket.getInputStream());
		setDaemon(true);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(open){
			try {
				Packet p = handlePacket(in.readByte());
				react(p);
			} catch (IOException e) {
				if((e instanceof SocketException) || (e instanceof EOFException)){
					if(open)Logger.getLogger("TST").info("Lost connection to server");
				} else Logger.getLogger(ClientHandler.class).warning("Error while reading from server", e);
				try {
					close();
				} catch (IOException e1) {
					Logger.getLogger(ClientHandler.class).warning("Error while closing", e1);
				}
				break;
			}
		}
	}
	
	public void close() throws IOException {
		open = false;
		socket.close();
	}

	public Packet handlePacket(int id) {
		try {
			Packet p = Packet.get(id);
			p.read(in);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param p
	 * @throws IOException 
	 */
	private void react(Packet p) throws IOException {
		Logger.getLogger("TST").info("Recieved packet of id: " + p.getId());
		Logger.getLogger("TST").info("Payload: " + p.toString());
		if(p.getId() == 0){
			sendPacket(new Packet0Accept());
			sendPacket(new Packet40NBT().setMessage(0, ((TesterConsole)TesterConsole.getConsole()).getClientDetails()));
		}
	}
	
	public void sendPacket(Packet p) throws IOException{
		out.writeBits(p.getId(), (short) 8);
		p.write(out);
		out.flush();
	}
	
}
