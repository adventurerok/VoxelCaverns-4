package vc4.vanilla.block;

import java.awt.Color;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.block.render.BlockRendererTable;
import vc4.vanilla.util.WoodBlocks;

public class BlockTable extends BlockMultitexture {

	public BlockTable(short uid) {
		super(uid, 1, Material.getMaterial("wood"), BlockTexture.woodFront);
		renderer = new BlockRendererTable();
	}

	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return side == 4;
	}

	@Override
	protected String getModifiedName(ItemStack item) {
		return "table." + WoodBlocks.getName(item.getDamage() & 15);
	}

	@Override
	public Color getColor(ItemStack current, int side) {
		return BlockPlanks.backColors[current.getDamage() & 15];
	}

	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return BlockPlanks.backColors[world.getBlockData(x, y, z) & 15];
	}

	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return BlockPlanks.frontColors[item.getDamage() & 15];
	}

	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return BlockPlanks.frontColors[world.getBlockData(x, y, z) & 15];
	}

	@Override
	public Collection<ItemStack> getCreativeItems() {
		return WoodBlocks.genCreativeItems(uid);
	}

}
