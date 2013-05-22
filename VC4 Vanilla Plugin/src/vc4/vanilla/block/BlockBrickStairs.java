package vc4.vanilla.block;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.render.BlockRendererStairs;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

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
		res[1] = AABB.getBoundingBox(dir == 2 ? 1/3d : 0, dir == 0 ? 2/3d : 1, 1/3d, 2/3d, dir == 3 ? 1/3d : 0, dir == 1 ? 2/3d : 1);
		res[2] = AABB.getBoundingBox(dir == 2 ? 2/3d : 0, dir == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, dir == 3 ? 2/3d : 0, dir == 1 ? 1/3d : 1);
		return res;
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		double hitY = player.getRays().vector.y - y;
		int add = 0;
		if(hitY > 0.5) add = 16;
		world.setBlockIdData(x, y, z, uid, add + item.getData() + Direction.getDirection(player.getSimpleFacing()).opposite().getId());
		item.decrementAmount();
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[]{new ItemStack(uid, world.getBlockData(x, y, z) & 12, 1)};
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		switch (base + ((item.getDamage() & 12) >> 2)) {
			case 0:
				return "brickstairs.clay";
			case 1:
				return "brickstairs.sandstone";
			case 2:
				return "brickstairs.gold";
			case 3:
				return "brickstairs.adamantite";
			case 4:
				return "brickstairs.stone";
			case 5:
				return "brickstairs.obsidian";
			case 6:
				return "brickstairs.hell";
			case 14:
			case 15:
				return "cobblestonestairs";
		}
		return name;
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
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < 4; ++d){
			if(d + base > 6) continue;
			result.add(new ItemStack(uid, d << 2));
		}
		return result;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		switch (base + ((world.getBlockData(x, y, z) & 12) >> 2)) {
			case 0:
				return BlockTexture.brick;
			case 1:
				return BlockTexture.sandstoneBrick;
			case 2:
				return BlockTexture.goldBrick;
			case 3:
				return BlockTexture.adamantiteBrick;
			case 4:
				return BlockTexture.stoneBrick;
			case 5:
				return BlockTexture.obsidianBrick;
			case 14:
				return BlockTexture.hellCobble;
			case 15:
				return BlockTexture.cobble;
		}

		return BlockTexture.brick;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		switch (base + ((item.getDamage() & 12) >> 2)) {
			case 0:
				return BlockTexture.brick;
			case 1:
				return BlockTexture.sandstoneBrick;
			case 2:
				return BlockTexture.goldBrick;
			case 3:
				return BlockTexture.adamantiteBrick;
			case 4:
				return BlockTexture.stoneBrick;
			case 5:
				return BlockTexture.obsidianBrick;
			case 14:
				return BlockTexture.hellCobble;
			case 15:
				return BlockTexture.cobble;
		}

		return BlockTexture.brick;
	}
	
	
	

}
