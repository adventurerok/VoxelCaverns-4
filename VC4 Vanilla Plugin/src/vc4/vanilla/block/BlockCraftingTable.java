package vc4.vanilla.block;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.Block;
import vc4.api.block.IBlockCraftingTable;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockCraftingTable extends Block implements IBlockCraftingTable {

	public BlockCraftingTable(int uid, int texture, String material) {
		super(uid, texture, material);
		// TASK Auto-generated constructor stub
	}

	@Override
	public int getCraftingSlots(World world, long x, long y, long z) {
		return 4 + world.getBlockData(x, y, z) * 2;
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < 2; ++d){
			result.add(new ItemStack(uid, d, 1));
		}
		return result;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if(side < 4){
			return BlockTexture.craftingTables[item.getDamage()];
		} else return BlockTexture.craftingTop;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return name + ".tier" + (item.getDamage() + 1);
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if(side < 4){
			return BlockTexture.craftingTables[world.getBlockData(x, y, z)];
		} else return BlockTexture.craftingTop;
	}

	@Override
	public Collection<Short> getCraftingItems(World world, long x, long y, long z) {
		ArrayList<Short> result = new ArrayList<>();
		result.add(Vanilla.craftingSaw);
		result.add(Vanilla.craftingTable);
		if(world.getBlockData(x, y, z) > 0) result.add(Vanilla.craftingHammer);
		return result;
	}

}
