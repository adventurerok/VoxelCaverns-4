package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockPresent extends Block {

	public BlockPresent(int uid) {
		super(uid, 1, "present");
		
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		if(side != 5) return Color.white;
		return Color.blue;
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		if(side != 5) return Color.white;
		return Color.blue;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if(side < 4) return BlockTexture.presentSideBlue;
		if(side == 4) return BlockTexture.presentTopBlue;
		return 1;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if(side < 4) return BlockTexture.presentSideBlue;
		if(side == 4) return BlockTexture.presentTopBlue;
		return 1;
	}

}
