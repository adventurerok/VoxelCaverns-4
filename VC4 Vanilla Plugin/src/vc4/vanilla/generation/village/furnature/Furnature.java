package vc4.vanilla.generation.village.furnature;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.vanilla.generation.village.Village;

public abstract class Furnature {

	Adjustment pos;

	public abstract void place(Village ville, Vector3l start, Direction dir);

	public Furnature(Adjustment pos) {
		super();
		this.pos = pos;
	}
}
