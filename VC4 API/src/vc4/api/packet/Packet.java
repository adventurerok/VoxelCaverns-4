/**
 * 
 */
package vc4.api.packet;

import java.io.IOException;
import java.util.HashMap;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;

/**
 * @author Paul Durbaba
 *
 */
public abstract class Packet {

	private static HashMap<Integer, Class<? extends Packet>> loaded = new HashMap<Integer, Class<? extends Packet>>();
	
	public abstract void write(BitOutputStream out) throws IOException;
	public abstract void read(BitInputStream in) throws IOException;
	public abstract int getId();
	
	public boolean processOnMain(){
		return false;
	}
	
	public Packet(){
		
	}
	public Packet(BitInputStream in){
		try {
			read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Packet get(int id) throws Exception{
		return loaded.get(id).newInstance();
	}
	
	
	public static void register(Class<? extends Packet> c) throws Exception{
		int id = c.newInstance().getId();
		loaded.put(id, c);
	}
	static{
		try {
			register(Packet0Accept.class);
			register(Packet1Disconnect.class);
			register(Packet30MessageString.class);
			register(Packet40NBT.class);
			register(Packet4Login.class);
			register(Packet31MessageInt.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
