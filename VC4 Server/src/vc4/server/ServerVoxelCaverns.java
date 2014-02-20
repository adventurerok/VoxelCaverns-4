package vc4.server;

import vc4.api.VoxelCaverns;
import vc4.api.world.World;

public class ServerVoxelCaverns extends VoxelCaverns{

	public ServerVoxelCaverns() {
		setInst(this);
	}
	
	@Override
	public World agetCurrentWorld() {
		return Console.getConsole().getWorld();
	}

}
