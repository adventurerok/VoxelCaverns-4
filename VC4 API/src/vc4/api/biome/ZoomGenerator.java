package vc4.api.biome;

import java.util.Random;

import vc4.api.world.World;

public abstract class ZoomGenerator {

	public World world;
	public Random rand;
	public ZoomGenerator parent;
	
	public ZoomGenerator(World world) {
		this.world = world;
	}
	
	public void createRandom(long x, long z){
		rand = world.createRandom(x, z, 35628132776231L);
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
