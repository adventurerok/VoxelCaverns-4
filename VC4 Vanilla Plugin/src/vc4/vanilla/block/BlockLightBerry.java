package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.block.render.BlockRendererCross;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockLightBerry extends BlockMultitexture {

	public BlockLightBerry(int uid) {
		super(uid, BlockTexture.vines, Material.getMaterial("berry"), BlockTexture.lightberries);
		blockLight[uid] = 12;
		blockOpacity[uid] = 1;
		renderer = new BlockRendererCross();
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return Color.green;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return Color.green;
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(dir != Direction.UP && dir != Direction.DOWN) return;
		if(!world.getBlockType(x, y + 1, z).isSolid(world, x, y + 1, z, 5) && !world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)){
			onBlockMined(world, x, y, z, null);
			world.setBlockId(x, y, z, 0);
			
		}
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if(!world.getBlockType(x, y + 1, z).isSolid(world, x, y + 1, z, 5) && !world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) return;
		world.setBlockIdData(x, y, z, uid, item.getData());
		item.decrementAmount();
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return new AABB[0];
	}
	
	@Override
	public boolean render3d(byte data) {
		return false;
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return true;
	}

}
