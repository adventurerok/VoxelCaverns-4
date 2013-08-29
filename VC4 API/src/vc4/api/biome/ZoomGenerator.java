package vc4.api.biome;

import java.util.Random;

import vc4.api.world.World;

public abstract class ZoomGenerator implements FastRandom{

	public World world;
	public Random rand;
	public long seed;
	public ZoomGenerator parent;
	
	public ZoomGenerator(World world) {
		this.world = world;
	}
	
	public void createRandom(long x, long z){
		rand = world.createRandom(x, z, 35628132776231L);
	}
	
	@Override
	public void initSeed(long seed){
		this.seed = seed * 341873128712L + world.getSeed() + 872169178519L;
	}
	
	public void initSeed(long x, long z){
		seed = x * 341873128712L + z * 132897987541L + world.getSeed() + 872169178519L;
		if(seed == 0) seed = 2010817090135L;
	}
	
	@Override
	public long nextLong(long max) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		long res = (seed >> 24) % max;
		if(res < 0) res += max;
		return res;
	}
	
	@Override
	public int nextInt(int max) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		int res = (int) ((seed >> 24) % max);
		if(res < 0) res += max;
		return res;
	}
	
	public ZoomGenerator(World world, ZoomGenerator parent) {
		super();
		this.world = world;
		this.parent = parent;
	}



	public void setWorld(World world) {
		this.world = world;
	}
	
	
	public abstract int[] generate(long x, long z, int size);
}
