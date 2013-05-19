package vc4.api.generator;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.world.MapData;
import vc4.api.world.World;

public interface PlantGenerator {

	
	/**
	 * 
	 * @param world
	 * @param x The chunk x to grow the plant in
	 * @param y The chunk y to grow the plant in
	 * @param z The chunk z to grow the plant in
	 * @param rand
	 * @param plant
	 */
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant);
	public void onWorldLoad(World world);
}
