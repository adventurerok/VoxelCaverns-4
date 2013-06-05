/**
 * 
 */
package vc4.vanilla.generation.populate;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.block.Block;
import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

/**
 * @author paul
 *
 */
public class WorldGenOres implements WorldPopulator {
	
	/*
	 * Near Surface:
	 * 50 <-> -1500
	 * 
	 * Deep Down:
	 * -1500 <-> -5000
	 * 
	 * In Hell:
	 * -5000 <-> -7000
	 */

	public static class OreGen{
		
		double highFreq, stdFreq, lowFreq;
		long highLevel, stdLevel, lowLevel;
		int size = 3;
		int bid = Vanilla.ore.uid;
		int hid = Vanilla.oreHell.uid;
		byte data = 0;
		
		/**
		 * 
		 */
		public OreGen() {
		}
		
		
		
		public OreGen(int data) {
			super();
			this.data = (byte) data;
		}



		/**
		 * @param data the data to set
		 */
		public void setData(int data) {
			this.data = (byte) data;
		}
		
		/**
		 * @param bid the bid to set
		 */
		public OreGen setBlockId(int bid) {
			this.bid = bid;
			return this;
		}
		
		public OreGen setHellId(int hid){
			this.hid = hid;
			return this;
		}
		
		public OreGen setBlockHellId(int bhid){
			this.hid = this.bid = bhid;
			return this;
		}
		
		public OreGen setNearSurface(){
			highLevel = 150;
			stdLevel = -1500;
			lowLevel = -2000;
			return this;
		}
		
		public OreGen setDeepDown(){
			highLevel = -1500;
			stdLevel = -4500;
			lowLevel = -5000;
			return this;
		}
		
		public OreGen setInHell(){
			highLevel = -5000;
			stdLevel = -6000;
			lowLevel = -7000;
			return this;
		}
		
		public OreGen setFrequency(double high, double std, double low){
			highFreq = high;
			stdFreq = std;
			lowFreq = low;
			return this;
		}
		
		public OreGen setSize(int size){
			this.size = size;
			return this;
		}
		
		public double getFrequency(long y){
			if(y == stdLevel) return stdFreq;
			if(y > stdLevel){
				double d = (y - stdLevel) / (highLevel - stdLevel);
				return d * highFreq + (1 - d) * stdFreq;
			}
			if(y < stdLevel){
				double d = (y - lowLevel) / (stdLevel - lowLevel);
				return d * stdFreq + (1 - d) * lowFreq;
			}
			return stdFreq;
		}
	}
	
	private static ArrayList<OreGen> oreGen = new ArrayList<>();
	
	public static void addOre(OreGen ore){
		oreGen.add(ore);
	}
	
	
	public static void onWorldLoad(World world){
		oreGen.clear();
		addOre(new OreGen(0).setSize(5).setNearSurface().setFrequency(0.2d, 0.8d, 0.4d));
		addOre(new OreGen(1).setNearSurface().setFrequency(0.2d, 0.75d, 0.4d));
		addOre(new OreGen(2).setNearSurface().setFrequency(0.2d, 0.75d, 0.4d));
		addOre(new OreGen(3).setNearSurface().setFrequency(0.15d, 0.68d, 0.3d));
		addOre(new OreGen(4).setSize(2).setNearSurface().setFrequency(0.05d, 0.25d, 0.3d));
		addOre(new OreGen(20).setSize(2).setNearSurface().setFrequency(0.05d, 0.2d, 0.3d));
		addOre(new OreGen(4).setDeepDown().setFrequency(0.3d, 0.8d, 0.7d));
		addOre(new OreGen(20).setDeepDown().setFrequency(0.3d, 0.5d, 0.6d));
		addOre(new OreGen(4).setInHell().setFrequency(0.7d, 0.4d, 0.2d));
		addOre(new OreGen(20).setSize(2).setInHell().setFrequency(0.6d, 0.2d, 0.1d));
		addOre(new OreGen(25).setSize(5).setDeepDown().setFrequency(0.2d, 0.8d, 0.4d));
		addOre(new OreGen(5).setDeepDown().setFrequency(0.1d, 0.6d, 0.4d));
		addOre(new OreGen(6).setDeepDown().setFrequency(0.08d, 0.45d, 0.3d));
		addOre(new OreGen(7).setDeepDown().setFrequency(0.01d, 0.45d, 0.3d));
		addOre(new OreGen(8).setDeepDown().setFrequency(0.09d, 0.4d, 0.2d));
		addOre(new OreGen(9).setInHell().setFrequency(0.4d, 0.7d, 0.3d));
		addOre(new OreGen(10).setSize(2).setDeepDown().setFrequency(0.01d, 0.3d, 0.2d));
		addOre(new OreGen(11).setSize(2).setDeepDown().setFrequency(0.01d, 0.1d, 0.1d));
		addOre(new OreGen(11).setSize(2).setInHell().setFrequency(0.1d, 0.15d, 0.2d));
		addOre(new OreGen(26).setSize(5).setInHell().setFrequency(0.4d, 0.8d, 0.45d));
		addOre(new OreGen(21).setSize(2).setInHell().setFrequency(0.2d, 0.45d, 0.3d));
		addOre(new OreGen(21).setSize(2).setDeepDown().setFrequency(0.01d, 0.1d, 0.2d));
		addOre(new OreGen(0).setBlockId(Vanilla.dirt.uid).setSize(5).setNearSurface().setFrequency(0.9d, 0.5d, 0.1d));
		addOre(new OreGen(0).setBlockId(Vanilla.dirt.uid).setSize(5).setNearSurface().setFrequency(0.9d, 0.5d, 0.1d));
		addOre(new OreGen(0).setBlockHellId(Vanilla.obsidian.uid).setSize(5).setInHell().setFrequency(0.2d, 0.6d, 0.4d));
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.generator.WorldPopulator#populate(vc4.api.world.World, long, long, long)
	 */
	@Override
	public void populate(World world, long x, long y, long z) {
		if(y > 5 || y < -220) return;
		Random rand = world.createRandom(x, y, z, 7667567423L);
		for(int d = 0; d < oreGen.size(); ++d){
			long px = (x << 5) + rand.nextInt(32);
			long py = (y << 5) + rand.nextInt(32);
			long pz = (z << 5) + rand.nextInt(32);
			OreGen o = oreGen.get(rand.nextInt(oreGen.size()));
			if(o.highLevel < py || o.lowLevel > py) continue;
			if(rand.nextDouble() > o.getFrequency(py)) continue;
			int sizeSquared = o.size * o.size;
			int sizeSeg = sizeSquared / 4;
			int sizeSml = sizeSeg / 2;
			for(long ax = px - o.size; ax <= px + o.size; ++ax){
				for(long ay = py - o.size; ay <= py + o.size; ++ay){
					for(long az = pz - o.size; az <= pz + o.size; ++az){
						long dis = (px - ax) * (px - ax) + (py - ay) * (py - ay) + (pz - az) * (pz - az);
						if(dis > sizeSquared - sizeSml + rand.nextInt(sizeSeg)) continue;
						if(world.getBlockId(ax, ay, az) == Block.stone.uid){
							world.setBlockIdDataNoNotify(ax, ay, az, o.bid, o.data);
						} else if(world.getBlockId(ax, ay, az) == Vanilla.hellrock.uid){
							world.setBlockIdDataNoNotify(ax, ay, az, o.hid, o.data);
						}
					}
				}
			}
		}
	}

}
