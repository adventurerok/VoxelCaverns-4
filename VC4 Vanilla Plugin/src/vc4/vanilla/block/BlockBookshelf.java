package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.util.WoodBlocks;

public class BlockBookshelf extends BlockMultitexture {

	public BlockBookshelf(int id) {
		super((short) id, 254, Material.getMaterial("wood"), 19);
		
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
	public ItemStack[] getCreativeItems() {
		return WoodBlocks.genCreativeItems(uid);
	}
	
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		return side < 4 ? 19 : 76;
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		return side < 4 ? 19 : 76;
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
