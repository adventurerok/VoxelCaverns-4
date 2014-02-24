/**
 * 
 */
package vc4.api.packet;

import java.io.IOException;
import java.util.HashMap;

import vc4.api.io.*;
import vc4.api.logging.Logger;

/**
 * @author Paul Durbaba
 *
 */
public abstract class Packet {

	private static HashMap<Integer, Class<? extends Packet>> loaded = new HashMap<Integer, Class<? extends Packet>>();
	
	public abstract void write(SwitchOutputStream out) throws IOException;
	public abstract void read(SwitchInputStream in) throws IOException;
	public abstract int getId();
	
	public boolean processOnMain(){
		return false;
	}
	
	public Packet(){
		
	}
	public Packet(SwitchInputStream in){
		try {
			read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Packet get(int id) throws Exception{
		try{
			return loaded.get(id).newInstance();
		} catch(NullPointerException e){
			Logger.getLogger(Packet.class).severe("Server sent unknown packet id: " + id);
			return null;
		}
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
			register(Packet3SUID.class);
			register(Packet41SettingInt.class);
			register(Packet42SettingDouble.class);
			register(Packet43SettingString.class);
			register(Packet5Chat.class);
			register(Packet6Chunk.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
