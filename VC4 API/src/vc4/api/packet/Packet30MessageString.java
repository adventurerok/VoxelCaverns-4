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
public class Packet30MessageString extends Packet {
	
	public static final int ID = 30;

	public String message;
	
	/**
	 * 
	 */
	public Packet30MessageString() {
	}
	
	
	
	public Packet30MessageString(String message) {
		super();
		this.message = message;
	}



	public void setData(String message) {
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#write(game.vc3d.io.BitOutputStream)
	 */
	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeString(message);

	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#read(game.vc3d.io.BitInputStream)
	 */
	@Override
	public void read(BitInputStream in) throws IOException {
		message = in.readString();

	}

	@Override
	public String toString() {
		return "Message: " + message;
	}
	
	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#getId()
	 */
	@Override
	public int getId() {
		return ID;
	}

}
