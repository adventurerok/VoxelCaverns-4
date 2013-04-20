/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;


/**
 * @author paul
 *
 */
public class BlockBrick extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockBrick(short uid) {
		super(uid, 31, Material.getMaterial("brick"));
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		switch (world.getBlockData(x, y, z)) {
			case 1:
				return 12;
			case 2:
				return 28;
			case 3:
				return 44;
			case 4:
				return 60;
			case 5:
				return 94;
			case 14:
				return 182;
			case 15:
				return textureIndex + 16;
		}

		return textureIndex;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public ItemStack[] getCreativeItems() {
		ItemStack[] result = new ItemStack[8];
		for(int d = 0; d < 6; ++d){
			result[d] = new ItemStack(uid, d, 1);
		}
		result[6] = new ItemStack(uid, 14, 1);
		result[7] = new ItemStack(uid, 15, 1);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		switch (item.getDamage()) {
			case 1:
				return 12;
			case 2:
				return 28;
			case 3:
				return 44;
			case 4:
				return 60;
			case 5:
				return 94;
			case 14:
				return 182;
			case 15:
				return textureIndex + 16;
		}

		return textureIndex;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		switch (item.getDamage()) {
			case 0:
				return "brick.clay";
			case 1:
				return "brick.sandstone";
			case 2:
				return "brick.gold";
			case 3:
				return "brick.adamantite";
			case 4:
				return "brick.stone";
			case 5:
				return "brick.obsidian";
			case 14:
			case 15:
				return "cobblestone";
		}
		return name;
	}

}
