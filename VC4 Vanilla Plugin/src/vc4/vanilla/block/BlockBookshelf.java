package vc4.vanilla.block;

import java.awt.Color;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.util.WoodBlocks;

public class BlockBookshelf extends BlockMultitexture {

	public BlockBookshelf(int id) {
		super((short) id, 1, Material.getMaterial("wood"), 19);
		
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "bookshelf." + WoodBlocks.getName(item.getDamage() & 15);
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		return BlockPlanks.backColors[data];
	}
	
	@Override
	public Color getColor(ItemStack item, int side) {
		return BlockPlanks.backColors[item.getDamage()];
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		return WoodBlocks.genCreativeItems(uid);
	}
	
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return side < 4 ? BlockTexture.bookshelf : BlockTexture.woodFront;
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return side < 4 ? BlockTexture.bookshelf : BlockTexture.woodFront;
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return side < 4 ? Color.white : BlockPlanks.frontColors[world.getBlockData(x, y, z)];
	}
	
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return side < 4 ? Color.white : BlockPlanks.frontColors[item.getData()];
	}

}
