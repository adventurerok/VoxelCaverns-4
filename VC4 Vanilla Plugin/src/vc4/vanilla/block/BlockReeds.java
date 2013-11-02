package vc4.vanilla.block;

import java.awt.Color;
import java.util.*;

import vc4.api.block.Block;
import vc4.api.block.Plant;
import vc4.api.block.render.BlockRendererCross;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockReeds extends Block {

	
	Color[] colors = new Color[]{new Color(0x326308), new Color(0xB4B82F), new Color(0xC1A156)};
	
	Plant[] plants = new Plant[]{new Plant("reed", "water", "normal"), new Plant("cane", "sugar", "crop"), new Plant("bamboo", "bamboo", "crop")};
	
	public BlockReeds(int uid) {
		super(uid, BlockTexture.reeds, "crop");
		renderer = new BlockRendererCross();
	}
	
	@Override
	public boolean render3d(byte data) {
		return false;
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		for(int d = 0; d < 3; ++d) res.add(new ItemStack(uid, d, 1));
		return res;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		switch(item.getDamage()){
			case 1: return "sugarcane";
			case 2: return "bamboo";
			default: return "reeds";
		}
	}
	
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return new AABB[0];
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return colors[current.getDamage()];
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return colors[world.getBlockData(x, y, z)];
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if((world.getBlockId(x, y - 1, z) != uid || world.getBlockData(x, y - 1, z) != item.getData()) && !world.getBlockType(x, y - 1, z).canGrowPlant(plants[item.getDamage()])) return;
		if(item.getDamage() == 0){
			for(int d = 1; d < 3; ++d){
				if(world.getBlockId(x, y - d, z) != uid){
					if(world.getBlockId(x + 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x - 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x, y - d, z + 1) != Vanilla.water.uid && world.getBlockId(x, y - d, z - 1) != Vanilla.water.uid){
						return;
					}
				}
			}
		}
		super.place(world, x, y, z, player, item);
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(dir !=  Direction.DOWN) return;
		byte data = world.getBlockData(x, y, z);
		if(data == 0){
			for(int d = 1; d < 3; ++d){
				if(world.getBlockId(x, y - d, z) != uid){
					if(world.getBlockId(x + 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x - 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x, y - d, z + 1) != Vanilla.water.uid && world.getBlockId(x, y - d, z - 1) != Vanilla.water.uid){
						for(int a = 0; a < d; ++a){
							world.setBlockId(x, y - a, z, 0);
						}
						return;
					}
				}
			}
		}
		if((world.getBlockId(x, y - 1, z) == uid && world.getBlockData(x, y - 1, z) == data) || world.getBlockType(x, y - 1, z).canGrowPlant(plants[data])) return;
		world.setBlockId(x, y, z, 0);
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		for(int d = 1; d < 7; ++d){
			if(world.getBlockId(x, y - d, z) != uid){
				if(data == 0 && world.getBlockId(x + 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x - 1, y - d, z) != Vanilla.water.uid && world.getBlockId(x, y - d, z + 1) != Vanilla.water.uid && world.getBlockId(x, y - d, z - 1) != Vanilla.water.uid){
					for(int a = 0; a < d; ++a){
						world.setBlockId(x, y - a, z, 0);
					}
					return 0;
				}
			}
			if(d == 2 && data != 2) return 0;
			if(d == 6) return 0;
		}
		if(world.getBlockId(x, y + 1, z) != 0) return 0;
		world.setBlockIdData(x, y + 1, z, uid, data);
		return 0;
	}

}
