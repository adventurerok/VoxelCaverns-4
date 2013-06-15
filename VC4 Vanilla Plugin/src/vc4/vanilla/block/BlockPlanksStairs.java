package vc4.vanilla.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.render.BlockRendererStairs;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.util.WoodBlocks;

public class BlockPlanksStairs extends BlockPlanks {
	
	int base = 0;

	public BlockPlanksStairs(short uid, int base) {
		super(uid);
		renderer = new BlockRendererStairs();
		this.base = base;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "planksstairs." + WoodBlocks.getName(base + ((item.getDamage() & 12) >> 2));
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		byte data = world.getBlockData(x, y, z);
		int dir = data & 3;
		boolean upside = (data & 16) != 0;
		AABB[] res = new AABB[3];
		res[0] = AABB.getBoundingBox(0, 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, 0, 1);
		res[1] = AABB.getBoundingBox(dir == 2 ? 1/3d : 0, dir == 0 ? 2/3d : 1, 1/3d, 2/3d, dir == 3 ? 1/3d : 0, dir == 1 ? 2/3d : 1);
		res[2] = AABB.getBoundingBox(dir == 2 ? 2/3d : 0, dir == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, dir == 3 ? 2/3d : 0, dir == 1 ? 1/3d : 1);
		return res;
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		double hitY = player.getRays().vector.y - y;
		int add = 0;
		if(hitY > 0.5) add = 16;
		world.setBlockIdData(x, y, z, uid, add + item.getData() + Direction.getDirection(player.getSimpleFacing()).opposite().id());
		item.decrementAmount();
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(uid, world.getBlockData(x, y, z) & 28, 1)};
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		int dir = data & 3;
		boolean upside = (data & 16) != 0;
		if(side == 4 && upside) return true;
		else if(!upside && side == 5) return true;
		return Direction.getDirection(dir).opposite().id() == side;
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < 4; ++d){
			if(d + base > 6) continue;
			result.add(new ItemStack(uid, d << 2));
		}
		return result;
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return backColors[base + ((current.getDamage() & 12) >> 2)];
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return backColors[base + ((world.getBlockData(x, y, z) & 12) >> 2)];
	}
	
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return frontColors[base + ((item.getDamage() & 12) >> 2)];
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return frontColors[base + ((world.getBlockData(x, y, z) & 12) >> 2)];
	}
	

}
