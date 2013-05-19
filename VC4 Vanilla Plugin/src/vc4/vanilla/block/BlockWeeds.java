package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererCross;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockWeeds extends Block {

	public static Color weeds = new Color(0x267F00);
	
	public BlockWeeds(int uid) {
		super(uid, 0, "weeds");
		renderer = new BlockRendererCross();
		mineData = new MiningData().setTimes(0.01, 0.01, 0.01).setPowers(0, 1, 25);
	}
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return null;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		Biome bio = world.getBiome(x, z);
		if(bio != null) return bio.plantColor;
		return weeds;
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		return weeds;
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		return BlockTexture.weeds[item.getDamage()];
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return BlockTexture.weeds[world.getBlockData(x, y, z)];
	}
	
	@Override
	public boolean canBeReplaced(int id, byte data) {
		return id != uid && id != 0;
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantWeed)){
			world.setBlockId(x, y, z, 0);
		}
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(Vanilla.plantWeed)){
			super.place(world, x, y, z, player, item);
		}
	}
	
	@Override
	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return bid != uid && bid != 0;
	}

}
