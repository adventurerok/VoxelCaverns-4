package vc4.vanilla.generation.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public abstract class Furnature {

	Adjustment pos;

	public abstract void place(World world, Vector3l start, Direction dir);

	public Furnature(Adjustment pos) {
		super();
		this.pos = pos;
	}
}
