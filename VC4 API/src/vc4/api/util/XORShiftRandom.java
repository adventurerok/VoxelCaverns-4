package vc4.api.util;

import java.util.Random;

import vc4.api.biome.FastRandom;

public class XORShiftRandom extends Random implements FastRandom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2246507463386390765L;
	long seed;

	@Override
	public void initSeed(long seed) {
		this.seed = seed;
	}

	@Override
	public synchronized void setSeed(long seed) {
		this.seed = seed;
	}
	
	

	@Override
	public long nextLong(long max) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		long res = seed % max;
		if(res < 0) res += max;
		return res;
	}
	
	@Override
	public long nextLong() {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		return seed;
	}
	
	@Override
	public int nextInt(int n) {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		int res = (int) (seed % n);
		if(res < 0) res += n;
		return res;
	}
	
	@Override
	public int nextInt() {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		return (int) (seed >> 24);
	}

	@Override
	protected int next(int bits) {
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		x &= ((1L << bits) - 1);
		return (int) x;
	}

	public XORShiftRandom() {
		seed = System.nanoTime();
	}

	public XORShiftRandom(long seed) {
		this.seed = seed;
	}

}
