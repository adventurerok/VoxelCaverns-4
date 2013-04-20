/**
 * 
 */
package vc4.api.server;

import java.io.*;
import java.util.HashMap;

import vc4.api.logging.Logger;

/**
 * @author paul
 *
 */
public abstract class Packet {

	private static HashMap<Integer, Class<? extends Packet>> idMap = new HashMap<Integer, Class<? extends Packet>>();
	private static HashMap<Class<? extends Packet>, Integer> classMap = new HashMap<Class<? extends Packet>, Integer>();
	
	public abstract void readData(DataInputStream in) throws IOException;
	public abstract void writeData(DataOutputStream out) throws IOException;
	
	public static void register(int id, Class<? extends Packet> packet){
		idMap.put(id, packet);
		classMap.put(packet, id);
	}
	
	public static Packet getPacket(int id){
		try {
			return idMap.get(id).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.getLogger(Packet.class).warning("Failed to create package", e);
		}
		return null;
	}
	
	public int getId(){
		return classMap.get(getClass());
	}

}
