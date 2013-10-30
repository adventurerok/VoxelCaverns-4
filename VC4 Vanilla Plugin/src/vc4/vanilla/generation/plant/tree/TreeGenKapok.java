package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.math.MathUtils;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class TreeGenKapok extends TreeGen {

	public TreeGenKapok() {
		// TASK Auto-generated constructor stub
	}

	public TreeGenKapok(World world, Random rand) {
		super(world, rand);
		// TASK Auto-generated constructor stub
	}

	@Override
	public boolean generate(long x, long y, long z, Plant plant) {
		byte data = plant.getData();
		if(rand.nextBoolean()) x -= 1;
		if(rand.nextBoolean()) z -= 1;
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plant)){
			return false;
		}
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, Vanilla.logV.uid, data)) return false;
		int height = 9 + rand.nextInt(35);
		for(long ny = y - 2; ny < y + height - 2; ++ny){
			//Trunk
			setBlockAsWood(x, ny, z, data);
			setBlockAsWood(x + 1, ny, z, data);
			setBlockAsWood(x, ny, z + 1, data);
			setBlockAsWood(x + 1, ny, z + 1, data);
			generateLeaves(x, y + height, z, 2, data);
			//TODO finish
		}
		for (long ry = (y + height) - 2 - rand.nextInt(4); ry > y + height / 2; ry -= 2 + rand.nextInt(rand.nextInt(5) + 1))
        {
            float dir = rand.nextFloat() * (float)Math.PI * 2.0F;
            long px = x + (int)(0.5F + MathUtils.cos(dir) * 4F);
            long pz = z + (int)(0.5F + MathUtils.sin(dir) * 4F);
            boolean big = rand.nextInt(3) == 0;
            generateLeaves(px, ry, pz, big ? 1 : 0, data);
            
            for (int my = 0; my < (big ? 7 : 5); my++)
            {
                long bx = x + (int)(1.5F + MathUtils.cos(dir) * my);
                long bz = z + (int)(1.5F + MathUtils.sin(dir) * my);
                setBlockAsWood(bx, (ry - 3) + my / 2, bz, data);
            }
        }
		return true;
	}

	public void generateLeaves(long x, long y, long z, int reach, byte meta) {
		byte yReach = 2;

		for (long ny = y - yReach; ny <= y; ny++) {
			int yMod = (int) (ny - y);
			int k = (reach + 1) - yMod;

			for (long nx = x - k; nx <= x + k + 1; nx++) {
				int xMod = (int) (nx - x);

				for (long nz = z - k; nz <= z + k + 1; nz++) {
					int zMod = (int) (nz - z);

					if ((xMod >= 0 || zMod >= 0 || xMod * xMod + zMod * zMod <= k * k) && (xMod <= 0 && zMod <= 0 || xMod * xMod + zMod * zMod <= (k + 1) * (k + 1))
							&& (rand.nextInt(4) != 0 || xMod * xMod + zMod * zMod <= (k - 1) * (k - 1))) {
						setBlockAsLeaf(nx, ny, nz, meta);
					}
				}
			}
		}
	}

}
