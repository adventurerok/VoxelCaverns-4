package vc4.vanilla.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.block.render.BlockRendererChair;

public class BlockChair extends BlockMultitexture {

	int woodType = 0;
	
	public BlockChair(int uid) {
		super(uid, 1, Material.getMaterial("wood"), BlockTexture.woodFront);
		renderer = new BlockRendererChair();
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if(item.getDamage() == 0) world.setBlockIdData(x, y, z, uid, item.getData());
		else if(item.getDamage() == 1){
			int dir = player.getSimpleFacing();
			int base = 1 << dir;
			world.setBlockIdData(x, y, z, uid, base);
		}
		item.decrementAmount();
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		byte data = world.getBlockData(x, y, z);
		if(data == 0) return new ItemStack[]{new ItemStack(uid, 0, 1)};
		else return new ItemStack[]{new ItemStack(uid, 1, 1)};
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		if(item.getDamage() != 0) return "chair";
		else return "stool";
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return BlockPlanks.backColors[woodType];
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return BlockPlanks.backColors[woodType];
	}
	
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return BlockPlanks.frontColors[woodType];
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return BlockPlanks.frontColors[woodType];
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		res.add(new ItemStack(uid, 0, 1));
		res.add(new ItemStack(uid, 1, 1));
		return res;
	}

}
