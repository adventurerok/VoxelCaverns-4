/**
 * 
 */
package vc4.vanilla.block;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		switch (world.getBlockData(x, y, z) & 15) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>(8);
		for (int d = 0; d < 6; ++d) {
			result.add(new ItemStack(uid, d, 1));
		}
		result.add(new ItemStack(uid, 14, 1));
		result.add(new ItemStack(uid, 15, 1));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		switch (item.getDamage() & 15) {
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
			case 6:
				return "brick.hell";
			case 14:
			case 15:
				return "cobblestone";
		}
		return name;
	}

}
