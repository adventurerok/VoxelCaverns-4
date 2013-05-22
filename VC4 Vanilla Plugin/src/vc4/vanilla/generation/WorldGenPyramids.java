package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.generator.WorldPopulator;
import vc4.api.logging.Logger;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenPyramids implements  WorldPopulator{

	@Override
	public void populate(World world, long x, long y, long z){
		Random rand = world.createRandom(x, y, z, 523705872809723L);
		if(rand.nextInt(20) != 0) return;
		x <<= 5;
		y <<= 5;
		z <<= 5;
		if(world.getBlockId(x, y + 1, z) != Vanilla.sand.uid) return;
		if(world.getBlockId(x + 32, y + 1, z) != Vanilla.sand.uid) return;
		if(world.getBlockId(x, y + 1, z + 32) != Vanilla.sand.uid) return;
		if(world.getBlockId(x + 32, y + 1, z + 32) != Vanilla.sand.uid) return;
		if(world.getBiome(x, z) != Vanilla.biomeDesert) return;
		Logger.getLogger("VC4").info("Generating Pyramid at: " + x + ", " + y + ", " + z);
		for(int h = -1; h < 16; ++h){
			for(int w = 0; w < 32; ++w){
				if(w < h || 31 - w < h) continue;
				for(int l = 0; l < 32; ++l){
					if(l < h || 31 - l < h) continue;
					if(h < 4 && h != -1 && w < 18 && w > 13 && l < 18 && l > 13){
						world.setBlockId(x + w, y + h + 1, z + l, 0);
					} else if(h < 5 && h != -1 && w < 19 && w > 12 && l < 19 && l > 12){
						world.setBlockIdData(x + w, y + h + 1, z + l, Vanilla.brick.uid, (byte)2);
					} else if(h == 13 && (w == 15 || w == 16) && (l == 15 || l == 16)){
						world.setBlockIdData(x + w, y + h + 1, z + l, Vanilla.brick.uid, (byte)6);
					} else {
						world.setBlockIdData(x + w, y + h + 1, z + l, Vanilla.brick.uid, (byte)1);
					}
				}
			}
		}
//		int cx = 14 + rand.nextInt(4);
//		int cz = 14 + rand.nextInt(4);
//		world.setBlockIdData(x + cx, y + 1, z + cz, Block.chest.uid, rand.nextByte((byte) 4));
//		TileEntityChest chest = new TileEntityChest(world, new Vector3l(x + cx, y + 1, z + cz), (byte)1, (byte)0);
//
//		//sand
//		int sand = 2 + rand.nextInt(12);
//		for(int dofor = 0; dofor < sand; ++dofor){
//			int slot = rand.nextInt(88);
//			chest.chest.setItem(slot, new ItemStack(Block.sand.uid, 0, 1 + rand.nextInt(rand.nextInt(98) + 1)));
//		}
//
//		//sandstone
//		int sandstone = 1 + rand.nextInt(7);
//		for(int dofor = 0; dofor < sandstone; ++dofor){
//			int slot = rand.nextInt(88);
//			chest.chest.setItem(slot, new ItemStack(Block.brick.uid, 1, 1 + rand.nextInt(rand.nextInt(88) + 2)));
//		}
//
//		//eyes
//		int eyes = rand.nextInt(rand.nextInt(3) + 1);
//		for(int dofor = 0; dofor < eyes; ++dofor){
//			int slot = rand.nextInt(88);
//			chest.chest.setItem(slot, new ItemStack(Item.loot.shiftedIndex(), 0, 1 + rand.nextInt(rand.nextInt(4) + 1)));
//		}
//
//		//tools
//		int tools = rand.nextInt(3) + 1;
//		for(int dofor = 0; dofor < tools; ++dofor){
//			int gt = rand.nextInt(5);
//			int tool = Item.goldPick.shiftedIndex();
//			switch(gt){
//			case 1:
//				tool = Item.goldHatchet.shiftedIndex();
//				break;
//			case 2:
//				tool = Item.goldSpear.shiftedIndex();
//				break;
//			case 3:
//				tool = Item.goldSword.shiftedIndex();
//				break;
//			case 4:
//				tool = Item.goldShovel.shiftedIndex();
//				break;
//			}
//			int slot = rand.nextInt(88);
//			chest.chest.setItem(slot, new ItemStack(tool, rand.nextInt(rand.nextInt(768) + 1), 1));
//		}
	}

}
