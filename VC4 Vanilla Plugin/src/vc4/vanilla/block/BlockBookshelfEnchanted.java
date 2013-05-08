package vc4.vanilla.block;

import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.util.WoodBlocks;

public class BlockBookshelfEnchanted extends BlockBookshelf {

	public BlockBookshelfEnchanted(int id) {
		super(id);
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return side < 4 ? BlockTexture.enchantedBookshelf : BlockTexture.woodFront;
	}
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return side < 4 ? BlockTexture.enchantedBookshelf : BlockTexture.woodFront;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "bookshelfench." + WoodBlocks.getName(item.getDamage() & 15);
	}

}
