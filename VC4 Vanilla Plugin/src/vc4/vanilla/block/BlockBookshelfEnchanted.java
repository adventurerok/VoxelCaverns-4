package vc4.vanilla.block;

import vc4.api.item.ItemStack;
import vc4.api.world.World;

public class BlockBookshelfEnchanted extends BlockBookshelf {

	public BlockBookshelfEnchanted(int id) {
		super(id);
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return side < 4 ? 20 : 76;
	}
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return side < 4 ? 20 : 76;
	}

}
