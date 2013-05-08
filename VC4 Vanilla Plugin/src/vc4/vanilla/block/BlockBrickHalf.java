package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.IBlockHalf;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;

public class BlockBrickHalf extends BlockBrick implements IBlockHalf{

	public BlockBrickHalf(short uid) {
		super(uid);
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		double hitY = player.getRays().vector.y - y;
		int add = hitY > 0.5 ? 16 : 0;
		world.setBlockIdData(x, y, z, uid, item.getData() + add);
		item.decrementAmount();
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(uid, world.getBlockData(x, y, z) & 15, 1)};
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		boolean upper = (world.getBlockData(x, y, z) & 16) != 0;
		if(upper && side == 5) return true;
		if(!upper && side == 4) return true;
		Direction dir = Direction.getDirection(side);
		Block b = world.getNearbyBlockType(x, y, z, dir);
		if(b == null) return true;
		boolean sol = !b.isSolid(world, x + dir.getX(), y + dir.getY(), z + dir.getZ(), dir.opposite().getId());
		if(side > 3 || sol == true) return sol;
		if(b instanceof IBlockHalf){
			boolean up = (world.getNearbyBlockData(x, y, z, dir) & 16) != 0;
			return up != upper;
		}
		return false;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		boolean upper = (world.getBlockData(x, y, z) & 16) != 0;
		if(upper && side == 4) return true;
		if(!upper && side == 5) return true;
		return false;
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		if((world.getBlockData(x, y, z) & 16) != 0){
			return new AABB[]{UPPER_HALF};
		} else return new AABB[]{LOWER_HALF};
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		if((world.getBlockData(x, y, z) & 16) != 0){
			return UPPER_HALF;
		} else return LOWER_HALF;
	}
	
	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		if((world.getBlockData(x, y, z) & 16) != 0){
			return UPPER_HALF;
		} else return LOWER_HALF;
	}
	
	@Override
	public AABB getRenderSize(ItemStack item) {
		if((item.getDamage() & 16) != 0){
			return UPPER_HALF;
		} else return LOWER_HALF;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		switch (item.getDamage()) {
			case 0:
				return "halfbrick.clay";
			case 1:
				return "halfbrick.sandstone";
			case 2:
				return "halfbrick.gold";
			case 3:
				return "halfbrick.adamantite";
			case 4:
				return "halfbrick.stone";
			case 5:
				return "halfbrick.obsidian";
			case 6:
				return "halfbrick.hell";
			case 14:
			case 15:
				return "halfcobblestone";
		}
		return name;
	}

}
