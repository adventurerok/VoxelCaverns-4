package vc4.vanilla.generation.village;

import vc4.api.math.MathUtils;
import vc4.api.vector.Vector2l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class Path {

	Vector2l start;
	Vector2l end;
	double m;
	double c;
	double n;
	double d;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Path other = (Path) obj;
		if (end == null) {
			if (other.end != null) return false;
		} else if (!end.equals(other.end)) return false;
		if (start == null) {
			if (other.start != null) return false;
		} else if (!start.equals(other.start)) return false;
		return true;
	}

	public Vector2l getStart() {
		return start;
	}

	public Vector2l getEnd() {
		return end;
	}

	public void generate(World world) {
		if (Double.isNaN(m)) {
			for (long z = start.y - 1; z <= end.y + 1; ++z) {
				long x = start.x;
				try {
					long y = world.getMapData(x >> 5, z >> 5).getGenHeight((int) (x & 31), (int) (z & 31));
					world.setBlockId(x, y, z, Vanilla.gravel.uid);
					if (z != start.y - 1 && z != end.y + 1) {
						world.setBlockId(x + 1, y, z, Vanilla.gravel.uid);
						world.setBlockId(x - 1, y, z, Vanilla.gravel.uid);
					}
				} catch (Exception e) {
				}
			}
		} else if (Math.abs(m) <= 1) {
			for (long x = start.x - 1; x <= end.x + 1; ++x) {
				long z = MathUtils.round(m * x + c);
				try {
					long y = world.getMapData(x >> 5, z >> 5).getGenHeight((int) (x & 31), (int) (z & 31));
					world.setBlockId(x, y, z, Vanilla.gravel.uid);
					if (x != start.x - 1 && x != end.x + 1) {
						world.setBlockId(x, y, z + 1, Vanilla.gravel.uid);
						world.setBlockId(x, y, z - 1, Vanilla.gravel.uid);
					}
				} catch (Exception e) {
				}
			}
		} else {

			for (long z = start.y - 1; z <= end.y + 1; ++z) {
				long x = MathUtils.round(n * z + d);
				try {
					long y = world.getMapData(x >> 5, z >> 5).getGenHeight((int) (x & 31), (int) (z & 31));
					world.setBlockId(x, y, z, Vanilla.gravel.uid);
					if (z != start.y - 1 && z != end.y + 1) {
						world.setBlockId(x + 1, y, z, Vanilla.gravel.uid);
						world.setBlockId(x - 1, y, z, Vanilla.gravel.uid);
					}
				} catch (NullPointerException e) {
				}
			}
		}
	}

	public Path(Vector2l start, Vector2l end) {
		super();
		if (end.x - start.x == 0) m = Double.NaN;
		else m = (end.y - start.y) / (double) (end.x - start.x);
		c = start.y - m * start.x;
		if (m == m && m != 0) n = 1d / m;
		else n = Double.NaN;
		d = start.x - n * start.y;
		this.start = new Vector2l(Math.min(start.x, end.x), Math.min(start.y, end.y));
		this.end = new Vector2l(Math.max(start.x, end.x), Math.max(start.y, end.y));
	}
}
