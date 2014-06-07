package vc4.api.biome;


public class ZoomGenSmooth extends ZoomGenerator {

	public ZoomGenSmooth(long worldSeed, ZoomGenerator parent) {
		super(worldSeed, parent);
	}

	@Override
	public int[] generate(long x, long z, int size) {
		long px = x - 1;
		long pz = z - 1;
		int psize = size + 2;
		int[] pints = parent.generate(px, pz, psize);
		int[] ints = new int[size * size];

		for (int az = 0; az < size; ++az) {
			for (int ax = 0; ax < size; ++ax) {
				int dp = pints[ax + 1 + (az + 1) * psize];
				int dn = pints[ax + 2 + (az + 1) * psize]; // north
				int de = pints[ax + 1 + (az + 2) * psize]; // east
				int ds = pints[ax + 0 + (az + 1) * psize]; // south
				int dw = pints[ax + 1 + (az + 0) * psize]; // west

				if (ds == dn && dw == de) {
					initSeed((long) (ax + x), (long) (az + z));
					if (nextInt(2) == 0) {
						dp = ds;
					} else {
						dp = dw;
					}
				} else {
					if (ds == dn) {
						dp = ds;
					}

					if (dw == de) {
						dp = dw;
					}
				}

				ints[ax + az * size] = dp;
			}
		}

		return ints;
	}

}
