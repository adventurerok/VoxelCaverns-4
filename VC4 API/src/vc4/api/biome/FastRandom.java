package vc4.api.biome;

public interface FastRandom {

	void initSeed(long seed);

	int nextInt(int max);

	long nextLong(long max);
}
