/**
 * 
 */
package vc4.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import vc4.api.logging.Logger;
import vc4.api.server.*;

/**
 * @author paul
 *
 */
public class ServerHandler extends Thread implements Server {

	ServerSocket socket;
	
	private ArrayList<ServerUser> players = new ArrayList<ServerUser>();
	
	/**
	 * @throws IOException 
	 * 
	 */
	public ServerHandler() throws IOException {
		socket = new ServerSocket(4775);
		Logger.getLogger("VC4").info("Starting server on port 4775");
		setDaemon(true);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while(true){
				Socket user = socket.accept();
				Logger.getLogger("VC4").info("Connection from " + user.getInetAddress().getCanonicalHostName() + ":" + user.getPort());
				ServerUser player = new ServerUser(user);
				players.add(player);
				player.start();
				//TASK send user message saying accepted
			}
		} catch (IOException e) {
			Logger.getLogger(ServerHandler.class).warning("Error while accepting connection", e);
		}
	}
	
	protected static class ServerUser extends Thread implements NetUser{
		
		protected Socket socket;
		private DataInputStream input;
		private DataOutputStream output;
		private boolean connected = true;

		private ServerUser(Socket socket) {
			super();
			this.socket = socket;
			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				Logger.getLogger(ServerUser.class).warning("Failed to create output streams for player", e);
			}
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				while(connected){
					int pid = input.read();
					if(pid == -1){
						Logger.getLogger("VC4").info("Recieved end of stream");
						break;
					}
					Packet p = Packet.getPacket(pid);
					p.readData(input);
					handlePacket(p);
				}
			} catch (IOException e) {
				Logger.getLogger(ServerUser.class).warning("Failed while reading packages", e);
			}
		}
		
		protected void handlePacket(Packet p){
			Logger.getLogger("VC4").info("Recieved package (id=" + p.getId() + ")");
		}
		
		public boolean writePacket(Packet p){
			try {
				output.write(p.getId());
				p.writeData(output);
				return true;
			} catch (IOException e) {
				Logger.getLogger(ServerUser.class).warning("Failed to write package (id=" + p.getId() + ")", e);
				return false;
			}
		}
		
		
	}

}