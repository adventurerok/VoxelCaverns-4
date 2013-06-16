package vc4.api;

import vc4.api.world.World;

public abstract class VoxelCaverns {

	private static VoxelCaverns inst;
	
	protected static void setInst(VoxelCaverns inst) {
		VoxelCaverns.inst = inst;
	}
	
	public abstract World agetCurrentWorld();
	
	public static World getCurrentWorld(){
		return inst.agetCurrentWorld();
	}
}
