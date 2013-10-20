package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;
import vc4.vanilla.block.render.BlockRendererTorch;

public class BlockTorch extends Block {

	AABB rt_dirs[] = new AABB[] { AABB.getBoundingBox(0.375, 0.625, 0, 0.71875, 0.375, 0.625), AABB.getBoundingBox(0, 0.4, 0.109375, 0.890625, 0.375, 0.625), AABB.getBoundingBox(0.375, 0.625, 0.109375, 0.890625, 0, 0.4),
			AABB.getBoundingBox(0.6, 1, 0.109375, 0.890625, 0.375, 0.625), AABB.getBoundingBox(0.375, 0.625, 0.109375, 0.890625, 0.6, 1) };

	public BlockTorch(int uid) {
		super(uid, BlockTexture.torch, "torch");
		blockOpacity[uid] = 1;
		blockLight[uid] = 14;
		mineData = new MiningData().setFistDestroyTime(0.001);
		renderer = new BlockRendererTorch();
	}

	@Override
	public boolean render3d(byte data) {
		return false;
	}

	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

	@Override
	public AABB[] getCollisionBounds(World world, long x, long y, long z) {
		return new AABB[0];
	}

	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[] { new ItemStack(uid, 0, 1) };
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return rt_dirs[world.getBlockData(x, y, z)];
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		byte data = world.getBlockData(x, y, z);
		if(data == 0){
			if(dir.id() != 5) return;
			int bid = world.getBlockId(x, y - 1, z);
			if(bid == Vanilla.glass.uid || world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) return;
			
		} else {
			dir = Direction.getDirection(data - 1).opposite();
			long ox = x + dir.getX();
			long oz = z + dir.getZ();
			if(world.getBlockType(ox, y, oz).isSolid(world, ox, y, oz, dir.opposite().id())) return;
		}
		
		dropItems(world, x, y, z, null);
		world.setBlockId(x, y, z, 0);
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		int dir = player.getRays().side;
		if (dir == 4) dir = 5;
		if (dir == 5) {
			int bid = world.getBlockId(x, y - 1, z);
			if (bid == Vanilla.glass.uid || world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) {
				world.setBlockIdData(x, y, z, item.getId(), (byte) 0);
				item.decrementAmount();
				return;
			}
			dir = 0;
		}
		for (int d = 0; d < 4; ++d) {
			long ox = x + Direction.getDirection(d).getX();
			long oz = z + Direction.getDirection(d).getZ();
			if (world.getBlockType(ox, y, oz).isSolid(world, ox, y, oz, Direction.getOpposite(d).id())) {
				byte data = (byte) (Direction.getOpposite(d).id() + 1);
				world.setBlockIdData(x, y, z, item.getId(), data);
				item.decrementAmount();
				return;
			}
			++dir;
			if (dir > 3) dir = 0;
		}
		int bid = world.getBlockId(x, y - 1, z);
		if (bid == Vanilla.glass.uid || world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) {
			world.setBlockIdData(x, y, z, item.getId(), (byte) 0);
			item.decrementAmount();
			return;
		}
	}

}
