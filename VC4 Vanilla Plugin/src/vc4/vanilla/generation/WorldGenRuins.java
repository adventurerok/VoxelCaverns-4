package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.biome.BiomeType;
import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenRuins implements WorldPopulator{

	@Override
	public void populate(World world, long x, long y, long z){
		Random rand = world.createRandom(x, y, z, 10879072L);
		if(rand.nextInt(50) != 0) return;
		x <<= 5;
		y <<= 5;
		z <<= 5;
		x += rand.nextInt(32);
		y += rand.nextInt(32);
		z += rand.nextInt(32);
		if(world.getBlockId(x, y + 1, z) != Vanilla.dirt.uid && world.getBlockId(x, y + 1, z) != Vanilla.grass.uid) return;
		if(world.getBlockId(x + 6, y + 1, z) != Vanilla.dirt.uid && world.getBlockId(x + 6, y + 1, z) != Vanilla.grass.uid) return;
		if(world.getBlockId(x, y + 1, z + 6) != Vanilla.dirt.uid && world.getBlockId(x, y + 1, z + 6) != Vanilla.grass.uid) return;
		if(world.getBlockId(x + 6, y + 1, z + 6) != Vanilla.dirt.uid && world.getBlockId(x + 6, y + 1, z + 6) != Vanilla.grass.uid) return;
		if(world.getBlockId(x + 3, y + 5, z + 3) == Vanilla.dirt.uid || world.getBlockId(x + 3, y + 5, z + 3) == Vanilla.grass.uid) return;
		Biome b = world.getBiome(x, z);
		if(!b.getType().equals(BiomeType.normal)) return;
		byte data = (byte) (rand.nextBoolean() ? 15 : 4);
		int cz, h, cy;
		for(int cx = 0; cx < 7; ++cx){
			for(cz = 0; cz < 7; ++cz){
				world.setBlockId(x + cx, y, z + cz, rand.nextInt(2) == 0 ? Vanilla.mossBrick.uid : Vanilla.brick.uid);
				world.setBlockData(x + cx, y, z + cz, data);
				if(cx == 0 || cx == 6 || cz == 0 || cz == 6){
					h = rand.nextInt(4) + 2;
					for(cy = 1; cy < h; ++cy){
						world.setBlockIdData(x + cx, y + cy, z + cz, rand.nextInt(2) == 0 ? Vanilla.mossBrick.uid : Vanilla.brick.uid, data);
					}
				}
			}
		}
//		int cx = 1 + rand.nextInt(5);
//		int cz = 1 + rand.nextInt(5);
//		world.setBlockIdData(x + cx, y + 1, z + cz, Block.chest.uid, rand.nextByte((byte) 4));
//		TileEntityChest chest = new TileEntityChest(world, new Vector3l(x + cx, y + 1, z + cz), (byte)0, (byte) rand.nextInt(7));
//
//		//cobblestone
//		int cstone = 2 + rand.nextInt(12);
//		for(int dofor = 0; dofor < cstone; ++dofor){
//			int slot = rand.nextInt(44);
//			chest.chest.setItem(slot, new ItemStack(Block.brick.uid, 15, 1 + rand.nextInt(rand.nextInt(98) + 1)));
//		}
//
//		//stone brick
//		int brick = 1 + rand.nextInt(7);
//		for(int dofor = 0; dofor < brick; ++dofor){
//			int slot = rand.nextInt(44);
//			chest.chest.setItem(slot, new ItemStack(Block.brick.uid, 4, 1 + rand.nextInt(rand.nextInt(88) + 2)));
//		}
//
//		//skulls
//		int skull = rand.nextInt(rand.nextInt(3) + 1);
//		for(int dofor = 0; dofor < skull; ++dofor){
//			int slot = rand.nextInt(44);
//			chest.chest.setItem(slot, new ItemStack(Item.loot.shiftedIndex(), 2, 1 + rand.nextInt(rand.nextInt(4) + 1)));
//		}
//
//		//tools
//		int tools = rand.nextInt(3) + 1;
//		for(int dofor = 0; dofor < tools; ++dofor){
//			int gt = rand.nextInt(5);
//			int tool = Item.bronzePick.shiftedIndex();
//			switch(gt){
//			case 1:
//				tool = Item.bronzeHatchet.shiftedIndex();
//				break;
//			case 2:
//				tool = Item.bronzeSpear.shiftedIndex();
//				break;
//			case 3:
//				tool = Item.bronzeSword.shiftedIndex();
//				break;
//			case 4:
//				tool = Item.bronzeShovel.shiftedIndex();
//				break;
//			}
//			int slot = rand.nextInt(44);
//			chest.chest.setItem(slot, new ItemStack(tool, rand.nextInt(rand.nextInt(196) + 1), 1));
//		}
		
	}
}
