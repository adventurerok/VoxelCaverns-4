package vc4.vanilla.block;

import java.awt.Color;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.util.WoodBlocks;

public class BlockPlanks extends BlockMultitexture {

	
	public BlockPlanks(short uid) {
		super(uid, 1, Material.getMaterial("wood"), BlockTexture.woodFront);
	}
	public static Color[] backColors = new Color[]{
		new Color(0x7F3300), //Oak
		new Color(0xFCE1C4), //Birch
		new Color(0x6D5E37), //Willow
		new Color(0xDDCDB4), //Ash
		new Color(0xC78145), //Chestnut
		new Color(0xDA7A48), //Redwood
		new Color(0xC3A078), //Kapok
	};
	public static Color[] frontColors = new Color[]{
		new Color(0x934714), //Oak
		new Color(0xB97536), //Birch
		new Color(0x352D18), //Willow
		new Color(0xAB9C87), //Ash
		new Color(0x704029), //Chestnut
		new Color(0x943215), //Redwood
		new Color(0xAF8C64), //Kapok
	};
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "planks." + WoodBlocks.getName(item.getDamage() & 15);
	}
	
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return backColors[current.getDamage() & 15];
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return backColors[world.getBlockData(x, y, z) & 15];
	}
	
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return frontColors[item.getDamage() & 15];
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return frontColors[world.getBlockData(x, y, z) & 15];
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		return WoodBlocks.genCreativeItems(uid);
	}
	
}
