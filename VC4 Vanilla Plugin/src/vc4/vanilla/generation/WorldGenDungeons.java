package vc4.vanilla.generation;


import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.biome.BiomeType;
import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenDungeons implements WorldPopulator{

	@Override
	public void populate(World world, long x, long y, long z) {
		Random rand = world.createRandom(x, y, z, 15625684286L);
		Biome b = world.getBiome(x << 5, z << 5);
		boolean badBiome = b.getType().equals(BiomeType.ocean) && y > -3;
		if(y > -1 || badBiome) return;
		if(rand.nextInt(150) != 0) return;
		x <<= 5;
		y <<= 5;
		z <<= 5;
		x += rand.nextInt(32);
		y += rand.nextInt(32);
		z += rand.nextInt(32);
		byte data = (byte) (rand.nextBoolean() ? 15 : 4);
		for(int cx = 0; cx < 7; ++cx){
			for(int cy = 0; cy < 6; ++cy){
				for(int cz = 0; cz < 7; ++cz){
					if(cx == 0 || cy == 0 || cz == 0 || cx == 6 || cy == 5 || cz == 6){
						if(world.getBlockId(x + cx, y + cy, z + cz) == 0) continue;
						world.setBlockIdData(x + cx, y + cy, z + cz, rand.nextInt(2) == 0 ? Vanilla.mossBrick.uid : Vanilla.brick.uid, data);
					} else {
						world.setBlockId(x + cx, y + cy, z + cz, 0);
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
//		//bones
//		int skull = rand.nextInt(rand.nextInt(6) + 1);
//		for(int dofor = 0; dofor < skull; ++dofor){
//			int slot = rand.nextInt(44);
//			chest.chest.setItem(slot, new ItemStack(Item.loot.shiftedIndex(), 1, 1 + rand.nextInt(rand.nextInt(54) + 3)));
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
