/**
 * 
 */
package vc4.server.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import vc4.api.VoxelCaverns;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.api.packet.Packet0Accept;
import vc4.api.packet.Packet1Disconnect;
import vc4.api.server.Server;
import vc4.api.server.User;
import vc4.server.packet.PacketHandler;
import vc4.server.task.TaskManager;
import vc4.server.user.ServerUser;

/**
 * @author paul
 * 
 */
public class ServerHandler extends Thread implements Server {

	ServerSocket socket;
	PacketHandler packetHandler = new PacketHandler();

	private ArrayList<ServerUser> players = new ArrayList<ServerUser>();

	/**
	 * @throws IOException
	 * 
	 */
	public ServerHandler() throws IOException {
		socket = new ServerSocket(4775);
		Logger.getLogger("VC4").info("Starting server on port 4775");
		setDaemon(true);
		VoxelCaverns.setServer(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Socket user = socket.accept();
				Logger.getLogger("VC4").info("Connection from " + user.getInetAddress().getCanonicalHostName() + ":" + user.getPort());
				ServerUser player = new ServerUser(user, packetHandler);
				players.add(player);
				player.start();
				TaskManager.sendPackets(player, new Packet0Accept());
			}
		} catch (IOException e) {
			Logger.getLogger(ServerHandler.class).warning("Error while accepting connection", e);
		}
	}

	@Override
	public void sendPacket(Packet p) {
		sendPacket(p, null);
	}

	@Override
	public void sendPacket(Packet p, User exclude) {
		for (int d = 0; d < players.size(); ++d) {
			if (players.get(d) == exclude) continue;
			TaskManager.sendPackets(players.get(d), p);
		}
	}

	@Override
	public void exit() {
		sendPacket(new Packet1Disconnect("Server shutting down"));
		while (players.size() > 0) {
			ServerUser u = players.remove(0);
			u.interrupt();
		}
		interrupt();
	}

}
