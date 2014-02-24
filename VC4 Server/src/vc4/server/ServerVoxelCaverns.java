package vc4.server;

import vc4.api.VoxelCaverns;
import vc4.api.io.SaveFormat;
import vc4.api.world.World;
import vc4.impl.io.VCH4SaveFormat;

public class ServerVoxelCaverns extends VoxelCaverns{

	public ServerVoxelCaverns() {
		setInst(this);
	}
	
	@Override
	public World agetCurrentWorld() {
		return Console.getConsole().getWorld();
	}

	@Override
	public SaveFormat agetSaveFormat() {
		return VCH4SaveFormat.VCH4_SAVE_FORMAT;
	}

}
