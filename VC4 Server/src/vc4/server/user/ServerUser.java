package vc4.server.user;

import java.io.IOException;
import java.net.Socket;

import org.jnbt.CompoundTag;

import vc4.api.entity.EntityPlayer;
import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.logging.Logger;
import vc4.api.packet.*;
import vc4.api.server.User;
import vc4.server.packet.PacketHandler;

public class ServerUser extends Thread implements User{
	
	private PacketHandler handler;
	protected Socket socket;
	private BitInputStream input;
	private BitOutputStream output;
	private boolean connected = true;
	private boolean accepted = false;
	private long timer = 0;
	private CompoundTag infoTag;
	
	byte[] uid; //16 byte (128-bit) uid

	public ServerUser(Socket socket, PacketHandler handler) {
		super();
		this.socket = socket;
		try {
			output = new BitOutputStream(socket.getOutputStream());
			input = new BitInputStream(socket.getInputStream());
		} catch (IOException e) {
			Logger.getLogger(ServerUser.class).warning("Failed to create output streams for player", e);
		}
		this.handler = handler;
		this.timer = System.nanoTime();
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
		handler.handlePacket(this, p);
		Logger.getLogger("VC4").info("Recieved packet of id: " + p.getId());
		Logger.getLogger("VC4").info("Payload: " + p.toString());
	}
	
	
	public ServerUser setInfoTag(CompoundTag infoTag) {
		this.infoTag = infoTag;
		return this;
	}
	
	public ServerUser setAccepted(boolean accepted) {
		this.accepted = accepted;
		return this;
	}
	
	public CompoundTag getInfoTag() {
		return infoTag;
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
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public long getTimer() {
		return timer;
	}

	@Override
	public void message(String message) {
		writePacket(new Packet30MessageString(message));
	}
	
	
}