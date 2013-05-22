/**
 * 
 */
package vc4.api.packet;

import java.io.IOException;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

/**
 * @author Paul Durbaba
 *
 */
public class Packet1Disconnect extends Packet{

	public short player;
	public String reason = null;
	
	/**
	 * 
	 */
	public Packet1Disconnect() {
	}
	
	public Packet1Disconnect(String reason) {
		super();
		this.reason = reason;
	}

	public Packet1Disconnect(short player, String reason) {
		super();
		this.player = player;
		this.reason = reason;
	}

	public void setData(short player, String reason) {
		this.reason = reason;
	}
	
	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#write(game.vc3d.io.BitOutputStream)
	 */
	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeShort(player);
		out.writeByte((byte) (reason == null ? 0 : 1));
		if(reason != null)out.writeString(reason);
	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#read(game.vc3d.io.BitInputStream)
	 */
	@Override
	public void read(BitInputStream in) throws IOException {
		player = in.readShort();
		if(in.readByte() == 1) reason = in.readString();
	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#getId()
	 */
	@Override
	public int getId() {
		return 1;
	}

}
