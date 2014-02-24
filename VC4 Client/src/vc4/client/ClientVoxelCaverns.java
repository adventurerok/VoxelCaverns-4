package vc4.client;

import vc4.api.VoxelCaverns;
import vc4.api.client.Client;
import vc4.api.io.SaveFormat;
import vc4.api.world.World;
import vc4.impl.io.VCH4SaveFormat;

public class ClientVoxelCaverns extends VoxelCaverns {

	public ClientVoxelCaverns() {
		setInst(this);
	}
	
	@Override
	public World agetCurrentWorld() {
		return Client.getGame().getWorld();
	}

	@Override
	public SaveFormat agetSaveFormat() {
		return VCH4SaveFormat.VCH4_SAVE_FORMAT;
	}

}
