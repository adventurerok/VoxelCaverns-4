package vc4.api.biome;

import vc4.api.world.World;

public class ZoomGenSmooth extends ZoomGenerator {

	public ZoomGenSmooth(World world, ZoomGenerator parent) {
		super(world, parent);
		// TASK Auto-generated constructor stub
	}

	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int[] result = parent.generate(x - 1, z - 1, zoom + 2, size);
		return result;
	}

}
