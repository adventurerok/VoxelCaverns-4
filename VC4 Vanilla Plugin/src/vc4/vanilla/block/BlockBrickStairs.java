package vc4.vanilla.block;

import java.util.ArrayList;

import vc4.api.block.render.BlockRendererStairs;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;

public class BlockBrickStairs extends BlockBrick {
	
	int base = 0;

	public BlockBrickStairs(short uid, int base) {
		super(uid);
		renderer = new BlockRendererStairs();
		this.base = base;
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		byte data = world.getBlockData(x, y, z);
		int dir = data & 3;
		boolean upside = (data & 16) != 0;
		AABB[] res = new AABB[3];
		res[0] = AABB.getBoundingBox(0, 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, 0, 1);
		res[1] = AABB.getBoundingBox(dir == 2 ? 1/3d : 1, dir == 0 ? 2/3d : 1, 1/3d, 2/3d, dir == 3 ? 1/3d : 0, dir == 1 ? 2/3d : 1);
		res[2] = AABB.getBoundingBox(dir == 2 ? 2/3d : 1, dir == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, dir == 3 ? 2/3d : 0, dir == 1 ? 1/3d : 1);
		return res;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		int dir = data & 3;
		boolean upside = (data & 16) != 0;
		if(side == 4 && upside) return true;
		else if(!upside && side == 5) return true;
		return Direction.getDirection(dir).opposite().getId() == side;
	}
	
	@Override
	public ItemStack[] getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < 4; ++d){
			if(d + base > 6) continue;
			result.add(new ItemStack(uid, d << 2));
		}
		return result.toArray(new ItemStack[result.size()]);
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		switch (base + ((world.getBlockData(x, y, z) & 12) >> 2)) {
			case 1:
				return 12;
			case 2:
				return 28;
			case 3:
				return 44;
			case 4:
				return 60;
			case 5:
				return 94;
			case 14:
				return 182;
			case 15:
				return textureIndex + 16;
		}

		return textureIndex;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		switch (base + ((item.getDamage() & 12) >> 2)) {
			case 1:
				return 12;
			case 2:
				return 28;
			case 3:
				return 44;
			case 4:
				return 60;
			case 5:
				return 94;
			case 14:
				return 182;
			case 15:
				return textureIndex + 16;
		}

		return textureIndex;
	}
	
	
	

}
