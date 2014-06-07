package vc4.api.block;

import vc4.api.entity.EntityBlock;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class BlockFallable extends Block implements IBlockFallable{

	public BlockFallable(int uid, int texture, Material m) {
		super(uid, texture, m);
	}

	public BlockFallable(int uid, int texture, String material) {
		super(uid, texture, material);
	}

	public BlockFallable(short uid, int texture, Material m) {
		super(uid, texture, m);
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		super.place(world, x, y, z, player, item);
		checkForFall(world, x, y, z);
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		checkForFall(world, x, y, z);
	}

	@Override
	public void checkForFall(World world, long x, long y, long z) {
		if(!canFallableFall) return;
		if(world.getBlockId(x, y - 1, z) == 0){
			byte data = world.getBlockData(x, y, z);
			canFallableFall = false;
			world.setBlockId(x, y, z, 0);
			EntityBlock fall = new EntityBlock(world, uid, data);
			fall.setPosition(new Vector3d(x + 0.5, y + 0.5, z + 0.5));
			fall.addToWorld();
			canFallableFall = true;
		}
	}

}
