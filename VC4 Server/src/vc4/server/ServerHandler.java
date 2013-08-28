/**
 * 
 */
package vc4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import vc4.api.logging.Logger;
import vc4.api.server.Server;

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

}
