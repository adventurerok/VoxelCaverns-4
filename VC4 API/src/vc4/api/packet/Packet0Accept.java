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
public class Packet0Accept extends Packet{

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#write(game.vc3d.io.BitOutputStream)
	 */
	@Override
	public void write(BitOutputStream out) throws IOException {
		
		
	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#read(game.vc3d.io.BitInputStream)
	 */
	@Override
	public void read(BitInputStream in) throws IOException {
		
		
	}

	/* (non-Javadoc)
	 * @see game.vc3d.server.Packet#getId()
	 */
	@Override
	public int getId() {
		return 0;
	}

}
