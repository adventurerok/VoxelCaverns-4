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
public class Packet3Login extends Packet {

	public String name;
	public byte[] password; //hashed password
	
	public void setData(String name, byte[] password){
		this.name = name;
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#write(game.vc3d.io.BitOutputStream)
	 */
	@Override
	public void write(BitOutputStream out) throws IOException {
		out.writeString(name);
		out.writeByte((byte) password.length);
		out.writeBytes(password);

	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#read(game.vc3d.io.BitInputStream)
	 */
	@Override
	public void read(BitInputStream in) throws IOException {
		name = in.readString();
		password = new byte[in.readByte()];
		in.readBytes(password);
	
	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#getId()
	 */
	@Override
	public int getId() {
		return 3;
	}

}
