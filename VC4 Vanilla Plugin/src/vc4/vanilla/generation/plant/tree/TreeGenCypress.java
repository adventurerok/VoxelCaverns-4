package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class TreeGenCypress extends TreeGen {

	public TreeGenCypress(World world, Random rand) {
		super(world, rand);
	}

	public TreeGenCypress() {
		// TASK Auto-generated constructor stub
	}

	@Override
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant) {
		if (y < -1) return;
		int cx = rand.nextInt(32);
		int cz = rand.nextInt(32);
		long height = data.getGenHeight(cx, cz);
		if (height >> 5 != y) return;
		this.world = world;
		this.rand = rand;
		generate((x << 5) + cx, height + 1, (z << 5) + cz, plant);
	}

	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = plant.getData();
		if (!world.getBlockType(x, y - 1, z).canGrowPlant(plant)) { return false; }
		if (!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 6 + rand.nextInt(4);
		int base = 2 + rand.nextInt(1);
		for (int d = -1; d < base; ++d) {
			for (int ox = -1; ox < 2; ++ox) {
				for (int oz = -1; oz < 2; ++oz) {
					if (ox != 0 && oz != 0) continue;
					if (!world.getBlockType(x + ox, y + d, z + oz).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) continue;
					setBlockAsWood(x + ox, y + d, z + oz, data);
				}
			}
		}
		if (plant.getVariantId() == STUMP_VARIANT) {
			setBlockAsWood(x, y + base, z, data);
			return true;
		}
		for (int dofor = base; dofor < height; ++dofor) {
			setBlockAsWood(x, y + dofor, z, data);
		}
		if (height > 8 && rand.nextInt(2) == 0) {
			int at = rand.nextInt(8);
			int dir = rand.nextInt(4);
			setBlockAsWoodRelative(x, y + at, z, dir, data);
		}
		if (plant.getVariantId() != DEAD_VARIANT) {
			int range = 2 + rand.nextInt(2);
			int ax, az;
			for (int ay = 0; ay < 2; ++ay) {
				for (ax = 0; ax <= range * 2; ++ax) {
					if (ay == 1 && (ax == 0 || ax == range * 2)) continue;
					for (az = 0; az <= range * 2; ++az) {
						if (ay == 1 && (az == 0 || az == range * 2)) continue;
						if (Math.abs(ax - range) == range && Math.abs(az - range) == range) continue;
						setBlockAsLeaf(x - range + ax, y + height - 1 + ay, z - range + az, data);
					}
				}
			}
			range++;
			int bot = 3 + rand.nextInt(2);
			for (int ay = height - 1; ay >= bot; --ay) {
				for (ax = 0; ax <= range * 2; ++ax) {
					for (az = 0; az <= range * 2; ++az) {
						if (Math.abs(ax - range) == range && Math.abs(az - range) == range) continue;
						if (ax == range && Math.abs(az - range) < 2) continue;
						if (az == range && Math.abs(ax - range) < 2) continue;
						setBlockVine(x - range + ax, y + ay, z - range + az);
					}
				}
			}
		}
		return true;
	}

}
