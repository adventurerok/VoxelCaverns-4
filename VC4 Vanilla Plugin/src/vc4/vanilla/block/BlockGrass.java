/**
 * 
 */
package vc4.vanilla.block;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.block.BlockMultitexture;
import vc4.api.block.Material;
import vc4.api.block.Plant;
import vc4.api.item.ItemStack;
import vc4.api.tool.MiningData;
import vc4.api.tool.ToolType;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

/**
 * @author paul
 *
 */
public class BlockGrass extends BlockMultitexture {

	public static Color grass = new Color(0x1C8F1C);
	
	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockGrass(short uid, Material m) {
		super(uid, BlockTexture.grassTop, m);
		mineData = new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 25).setTimes(0.5, 0.02, 0.25);
	}
	
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#getColorMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		if(world.getBlockId(x, y + 1, z) == Vanilla.snow.uid) return Color.white;
		Biome bio = world.getBiome(x, z);
		if(bio != null) return bio.grassColor;
		return grass;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#getColorMultitexture(vc4.api.item.ItemStack, int)
	 */
	@Override
	public Color getColorMultitexture(ItemStack item, int side) {
		return grass;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#getTextureIndexMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		byte data = world.getBlockData(x, y, z);
		if(side < 4 && world.getNearbyBlockId(x, y - 1, z, Direction.getDirection(side)) != uid){
			if(data == 0)return BlockTexture.grassSide;
			else return 178 + data;
		}
		else{
			if(data == 0) return BlockTexture.grassTop;
			else return 175 + data;
		}
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		if(side == 4 && world.getBlockData(x, y, z) == 0){
			if(world.getBlockId(x, y + 1, z) == Vanilla.snow.uid) return Color.white;
			Biome bio = world.getBiome(x, z);
			if(bio != null) return bio.grassColor;
			else return grass;
		}
		else return Color.white;
	}
	
	@Override
	public Color getColor(ItemStack item, int side) {
		if(side == 4 && item.getDamage() == 0) return grass;
		else return Color.white;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#getTextureIndexMultitexture(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		if(side < 4){
			if(item.getDamage() == 0)return BlockTexture.grassSide;
			else return 178 + item.getDamage();
		}
		else return 175 + item.getDamage();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		if((side == 4 || (side < 4 && world.getNearbyBlockId(x, y - 1, z, Direction.getDirection(side)) == uid)) && world.getBlockData(x, y, z) == 0) return BlockTexture.grassTop;
		else return BlockTexture.dirt;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getTextureIndex(vc4.api.item.ItemStack, int)
	 */
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		if(side == 4 && item.getDamage() == 0) return BlockTexture.grassTop;
		else return BlockTexture.dirt;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#renderSideMultitexture(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSideMultitexture(World world, long x, long y, long z, int side) {
		if(multitextureUsed(world.getBlockData(x, y, z), side)) return super.renderSideMultitexture(world, x, y, z, side);
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.BlockMultitexture#multitextureUsed(byte, int)
	 */
	@Override
	public boolean multitextureUsed(byte data, int side) {
		if(side == 4) return data != 0;
		return side < 4;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#canGrowPlant(vc4.api.block.Plant)
	 */
	@Override
	public boolean canGrowPlant(Plant plant) {
		return plant.getUid() == Vanilla.plantTreeOak.getUid() || plant.getUid() == Vanilla.plantWeed.getUid();
	}
	
	
	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z) {
		byte data = world.getBlockData(x, y, z);
		if(rand.nextInt(data + 1) == 0){
			long nx = x - 1 + rand.nextInt(3);
			long nz = z - 1 + rand.nextInt(3);
			long ny = y - 2 + rand.nextInt(5);
			if(!world.getBlockType(nx, ny + 1, nz).isSolid(world, nx, ny, nz, 5)){
				if(world.getBlockId(nx, ny, nz) == Vanilla.dirt.uid){
					world.setBlockIdData(nx, ny, nz, Vanilla.grass.uid, (byte) 3);
				} else if(world.getBlockId(nx, ny, nz) == Vanilla.grass.uid){
					byte nd = world.getBlockData(nx, ny, nz);
					if(nd > 0){
						--nd;
						world.setBlockData(nx, ny, nz, nd);
					}
				}
			}
		}
		if(data > 0){
			--data;
			world.setBlockData(x, y, z, data);
		}
		
		return 0;
	}

}
