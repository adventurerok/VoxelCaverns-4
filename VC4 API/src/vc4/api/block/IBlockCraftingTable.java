package vc4.api.block;

import java.util.Collection;

import vc4.api.world.World;

public interface IBlockCraftingTable {

	public int getCraftingSlots(World world, long x, long y, long z);

	public Collection<Short> getCraftingItems(World world, long x, long y, long z);
}
