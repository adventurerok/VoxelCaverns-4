/**
 * 
 */
package vc4.tester;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.api.packet.Packet0Accept;
import vc4.api.packet.Packet30MessageString;
import vc4.api.packet.Packet40NBT;
import vc4.api.packet.Packet43SettingString;
import vc4.api.packet.Packet4Login;
import vc4.api.packet.Packet5Chat;
import vc4.api.server.ServerConsole;



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
		//Logger.getLogger("TST").info("Recieved packet of id: " + p.getId());
		//Logger.getLogger("TST").info("Payload: " + p.toString());
		if(p.getId() == Packet0Accept.ID){
			sendPacket(new Packet0Accept());
			sendPacket(new Packet40NBT().setMessage(0, ((TesterConsole)TesterConsole.getConsole()).getClientDetails()));
			sendPacket(new Packet4Login(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
			sendPacket(new Packet43SettingString(0, "adventurerok"));
		} else if(p.getId() == Packet5Chat.ID){
			Packet5Chat chat = (Packet5Chat) p;
			ServerConsole.getConsole().writeLine(chat.prefix + chat.name + ": " + chat.msg);
		} else if(p.getId() == Packet30MessageString.ID){
			ServerConsole.getConsole().writeLine(((Packet30MessageString)p).message);
		}
	}
	
	public void sendPacket(Packet p) throws IOException{
		out.writeBits(p.getId(), (short) 8);
		p.write(out);
		out.flush();
	}
	
}
