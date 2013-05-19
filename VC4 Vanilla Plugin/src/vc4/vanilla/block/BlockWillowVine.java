package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.item.ItemStack;
import vc4.api.world.World;

public class BlockWillowVine extends BlockVine {
	
	public Color willow = new Color(0xCED237);

	public BlockWillowVine(int uid, int texture, String material) {
		super(uid, texture, material);
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return willow;
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return willow;
	}

}
