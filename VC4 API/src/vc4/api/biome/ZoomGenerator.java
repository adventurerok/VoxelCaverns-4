package vc4.api.biome;

import java.util.Random;

import vc4.api.util.XORShiftRandom;

public abstract class ZoomGenerator implements FastRandom {

	public Random rand;
	public long worldSeed;
	public long seed;
	public ZoomGenerator parent;

	public ZoomGenerator(long worldSeed) {
		this.worldSeed = worldSeed;
	}

	public void createRandom(long x, long z) {
		long seed = x * 341873128712L + z * 132897987541L + worldSeed + 35628132776231L;
		rand = new XORShiftRandom(seed);
	}

	@Override
	public void initSeed(long seed) {
		this.seed = seed * 341873128712L + worldSeed + 872169178519L;
	}

	public void initSeed(long x, long z) {
		seed = x * 341873128712L + z * 132897987541L + worldSeed + 872169178519L;
		if (seed == 0) seed = 2010817090135L;
	}

	@Override
	public long nextLong(long max) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		long res = (seed >> 24) % max;
		if (res < 0) res += max;
		return res;
	}

	@Override
	public int nextInt(int max) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		int res = (int) ((seed >> 24) % max);
		if (res < 0) res += max;
		return res;
	}

	public ZoomGenerator(long worldSeed, ZoomGenerator parent) {
		super();
		this.worldSeed = worldSeed;
		this.parent = parent;
	}

	public void setWorldSeed(long worldSeed) {
		this.worldSeed = worldSeed;
	}

	public abstract int[] generate(long x, long z, int size);
}
