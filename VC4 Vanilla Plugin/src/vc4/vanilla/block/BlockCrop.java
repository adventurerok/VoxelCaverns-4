package vc4.vanilla.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import vc4.api.block.*;
import vc4.api.block.render.BlockRendererQuad;
import vc4.api.entity.EntityItem;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.*;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockCrop extends BlockMultitexture{

	public static Color[][] crops = new Color[4][];
	public static Plant[] plants = new Plant[4];
	
	AABB cBounds = AABB.getBoundingBox(0.1, 0.9, 0, 0.4, 0.1, 0.9);
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return cBounds;
	}
	
	static{
		crops[0] = new Color[8];
		crops[1] = new Color[8];
		plants[0] = new Plant("grass", "wheat", "crop");
		plants[1] = new Plant("grass", "barley", "crop");
		for(int d = 0; d < 8; ++d){
			crops[0][d] = new Color(d * 30, 200, 0);
			crops[1][d] = new Color(d * 30, 200 - (d * 20), 0);
		}
	}
	
	int type;
	
	public BlockCrop(int uid, int type) {
		super(uid, BlockTexture.cropsGrown, "crops");
		renderer = new BlockRendererQuad();
		this.type = type;
	}
	
	@Override
	public boolean overrideRightClick(World world, long x, long y, long z) {
		return world.getBlockData(x, y, z) > 15;
	}
	
	@Override
	public void onRightClick(World world, long x, long y, long z, int side, EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.1) return;
		if(world.getBlockData(x, y, z) == 23){
			new EntityItem(world).setItem(new ItemStack(Vanilla.crop.id, type, 1)).setPosition(x + 0.5, y + 0.5, z + 0.5).setVelocity((rand.nextDouble() - 0.5) / 2d, 0, (rand.nextDouble() - 0.5) / 2d).addToWorld();
			world.setBlockData(x, y, z, 16);
		}
		player.setCoolDown(200);
	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		byte data = world.getBlockData(x, y, z);
		Random rand = new XORShiftRandom();
		ArrayList<ItemStack> res = new ArrayList<>();
		if(data > 15) res.add(new ItemStack(Vanilla.stakes.uid, 0, 1));
		if((data & 15) == 7) res.add(new ItemStack(Vanilla.crop.id, type, 1));
		res.add(new ItemStack(Vanilla.seeds.id, type, 1 + rand.nextInt(Math.max(1, data / 3))));
		return res.toArray(new ItemStack[res.size()]);
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
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		return world.getBlockData(x, y, z) > 15;
	}
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		if(data > 15) return BlockTexture.stakes;
		if(data < 7) return BlockTexture.crops[data];
		else return BlockTexture.cropsGrown;
	}
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		int data = world.getBlockData(x, y, z) & 15;
		if(data < 7) return BlockTexture.crops[data];
		else return BlockTexture.cropsGrown;
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		if((item.getDamage() & 15) < 7) return BlockTexture.crops[item.getDamage() & 15];
		else return BlockTexture.cropsGrown;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		byte dat = world.getBlockData(x, y, z);
		if(dat > 15) return Color.white;
		return crops[type][dat];
	}
	
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return crops[type][world.getBlockData(x, y, z) & 15];
	}
	
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return crops[type][item.getDamage() & 15];
	}
	
	@Override
	public Color getColor(ItemStack current, int side) {
		if(current.getDamage() > 15) return Color.white;
		return crops[type][current.getDamage()];
	}
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data) {
		if((data & 15) > 6) return 0;
		if(world.getBlockId(x, y - 1, z) == Vanilla.farmland.uid || rand.nextBoolean()) world.setBlockData(x, y, z, ++data);
		return 0;
	}
	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plants[type])) return;
		world.setBlockIdData(x, y, z, uid, item.getData());
		item.decrementAmount();
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(dir != Direction.DOWN) return;
		if(world.getBlockType(x, y - 1, z).canGrowPlant(plants[type])) return;
		dropItems(world, x, y, z, null);
		world.setBlockId(x, y, z, 0);
	}

}
