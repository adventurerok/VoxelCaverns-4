/**
 * 
 */
package vc4.vanilla.block;

import java.util.Collection;

import vc4.api.block.Block;
import vc4.api.block.Material;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.util.WoodBlocks;

/**
 * @author paul
 *
 */
public class BlockLeaf extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockLeaf(short uid, Material m) {
		super(uid, 0, m);
		
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return "leaf." + WoodBlocks.getName(item.getData() & 15);
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		Direction d = Direction.getDirection(side);
		long ox = x + d.getX();
		long oy = y + d.getY();
		long oz = z + d.getZ();
		Block near = world.getBlockType(ox, oy, oz);
		if ((near instanceof BlockLeaf && (side < 4 ? side / 2 == 0 : side % 2 == 0)) || near.isSolid(world, ox, oy, oz, d.opposite().id())) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getRendererToUse(byte, int)
	 */
	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public Collection<ItemStack> getCreativeItems() {
		return WoodBlocks.genCreativeItems(uid);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		switch(data){
			case 0:
				return BlockTexture.oakLeaves;
			case 1:
				return BlockTexture.birchLeaves;
			case 2:
				return BlockTexture.willowLeaves;
			case 3:
				return BlockTexture.ashLeaves;
			case 4:
				return BlockTexture.chestnutLeaves;
			case 5:
				return BlockTexture.redwoodLeaves;
			case 6:
				return BlockTexture.kapokLeaves;
		}
		return BlockTexture.oakLeaves;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		switch(item.getData()){
			case 0:
				return BlockTexture.oakLeaves;
			case 1:
				return BlockTexture.birchLeaves;
			case 2:
				return BlockTexture.willowLeaves;
			case 3:
				return BlockTexture.ashLeaves;
			case 4:
				return BlockTexture.chestnutLeaves;
			case 5:
				return BlockTexture.redwoodLeaves;
			case 6:
				return BlockTexture.kapokLeaves;
		}
		return BlockTexture.oakLeaves;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#isSolid(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

}
