package vc4.vanilla.generation.plant.tree;

import vc4.api.block.Plant;

public class TreeGenBush extends TreeGen {

	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = plant.getData();
		if (!world.getBlockType(x, y - 1, z).canGrowPlant(plant)) { return false; }
		if (!world.getBlockType(x, y, z).replacableBy(world, x, y, z, (short) 8, data)) return false;
		setBlockAsWood(x, y, z, data);
		for (int ny = 0; ny < 2; ++ny) {
			for (int nx = -2; nx < 3; ++nx) {
				for (int nz = -2; nz < 3; ++nz) {
					if (Math.abs(nx) + Math.abs(nz) > 3) continue;
					else if (ny == 1 && Math.abs(nx) + Math.abs(nz) > 1) continue;
					setBlockAsLeaf(x + nx, y + ny, z + nz, data);
				}
			}
		}
		setBlockAsLeafWithChance(x, y + 2, z, data, 50);
		return true;
	}

}
