package vc4.api.block;

import java.util.Arrays;
import java.util.HashSet;

import vc4.api.entity.Entity;
import vc4.api.util.ArrayUtils;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class CraftingTable {

	public short[] items = new short[0];
	public int slots = 2;

	public void update(Entity player){
		HashSet<Short> newItems = new HashSet<>();
		int newSlots = 2;
		World world = player.getWorld();
		Vector3l pos = player.getBlockPos();
		for(long x = pos.x - 4; x < pos.x + 5; ++x){
			for(long z = pos.z - 4; z < pos.z + 5; ++z){
				for(long y = pos.y - 2; y < pos.y + 3; ++y){
					if(world.getBlockType(x, y, z) instanceof IBlockCraftingTable){
						IBlockCraftingTable b = (IBlockCraftingTable)world.getBlockType(x, y, z);
						newSlots = Math.max(newSlots, b.getCraftingSlots(world, x, y, z));
						newItems.addAll(b.getCraftingItems(world, x, y, z));
					}
				}
			}
		}
		Arrays.sort(items = ArrayUtils.toPrimatives(newItems.toArray(new Short[newItems.size()])));
		slots = newSlots;
	}

}
