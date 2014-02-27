package vc4.api.tileentity;

import vc4.api.container.Container;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public abstract class TileEntityContainer extends TileEntity {

	public abstract Container getContainer();

	public TileEntityContainer(World world, Vector3l pos) {
		super(world, pos);
		// TASK Auto-generated constructor stub
	}

	public TileEntityContainer(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}

}
