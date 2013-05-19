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
//			register(Packet0Accept.class);
//			register(Packet1Disconnect.class);
//			register(Packet2Message.class);
//			register(Packet3Login.class);
//			register(Packet4Chat.class);
//			register(Packet5Position.class);
//			register(Packet6SpecialMessage.class);
//			register(Packet7PlayerMove.class);
//			register(Packet8SpecialMessage.class);
//			register(Packet9Name.class);
//			register(Packet10UserMessage.class);
//			register(Packet11UserMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
