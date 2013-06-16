package vc4.client;

import vc4.api.VoxelCaverns;
import vc4.api.client.Client;
import vc4.api.world.World;

public class ClientVoxelCaverns extends VoxelCaverns {

	public ClientVoxelCaverns() {
		setInst(this);
	}
	
	@Override
	public World agetCurrentWorld() {
		return Client.getGame().getWorld();
	}

}
