package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererQuad;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockStakes extends Block {

	public BlockStakes(int uid) {
		super(uid, BlockTexture.stakes, "crops");
		renderer = new BlockRendererQuad();
		blockOpacity[uid] = 1;
	}

	@Override
	public boolean render3d(byte data) {
		return false;
	}

	AABB cBounds = AABB.getBoundingBox(0.1, 0.9, 0, 0.4, 0.1, 0.9);

	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return cBounds;
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if (!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantGrassWheat)) return;
		super.place(world, x, y, z, player, item);
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if (dir != Direction.DOWN) return;
		if (world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantGrassWheat)) return;
		dropItems(world, x, y, z, null);
		world.setBlockId(x, y, z, 0);
	}

	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return new AABB[0];
	}

}
