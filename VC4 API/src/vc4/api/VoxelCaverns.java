package vc4.api;

import vc4.api.server.Server;
import vc4.api.world.World;

public abstract class VoxelCaverns {

	private static VoxelCaverns inst;
	private static Server server;
	
	protected static void setInst(VoxelCaverns inst) {
		VoxelCaverns.inst = inst;
	}
	
	
	public static void setServer(Server server) {
		VoxelCaverns.server = server;
	}
	public abstract World agetCurrentWorld();
	
	public static World getCurrentWorld(){
		return inst.agetCurrentWorld();
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static boolean isServer(){
		return server != null;
	}
}
