package vc4.server;

import java.io.IOException;
import java.net.Socket;

import vc4.api.entity.EntityPlayer;
import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.api.server.User;

class ServerUser extends Thread implements User{
	
	protected Socket socket;
	private BitInputStream input;
	private BitOutputStream output;
	private boolean connected = true;
	
	byte[] uid; //16 byte (128-bit) uid

	ServerUser(Socket socket) {
		super();
		this.socket = socket;
		try {
			output = new BitOutputStream(socket.getOutputStream());
			input = new BitInputStream(socket.getInputStream());
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
				int pid = input.readByte();
				if(pid == -1){
					Logger.getLogger("VC4").info("Recieved end of stream");
					break;
				}
				Packet p = Packet.get(pid);
				p.read(input);
				handlePacket(p);
			}
		} catch (Exception e) {
			Logger.getLogger(ServerUser.class).warning("Failed while reading packages", e);
		}
	}
	
	protected void handlePacket(Packet p){
		Logger.getLogger("VC4").info("Recieved package (id=" + p.getId() + ")");
	}
	
	public boolean writePacket(Packet p){
		try {
			output.writeByte((byte) p.getId());
			p.write(output);
			return true;
		} catch (IOException e) {
			Logger.getLogger(ServerUser.class).warning("Failed to write package (id=" + p.getId() + ")", e);
			return false;
		}
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public byte[] getUid() {
		return uid;
	}

	@Override
	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public void message(String message) {
		//TASK
	}
	
	
}