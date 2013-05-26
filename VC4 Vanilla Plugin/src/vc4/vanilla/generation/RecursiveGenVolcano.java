package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.biome.*;
import vc4.api.block.Block;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.RecursiveGenerator;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class RecursiveGenVolcano extends RecursiveGenerator {
	
	World lastWorld;
	ZoomGenerator volcanicCheck;

	@Override
	protected void generateRecursive(World world, Random rand, long x, long y, long z, long cx, long cy, long cz, GeneratorOutput data) {
		if(cy != 2) return;
		if(rand.nextInt(55) != 0) return;
		if(world != lastWorld) createZoom(world);
		if(volcanicCheck.generate(cx >> 2, cz >> 2, 1)[0] != Vanilla.biomeVolcano.id) return;
		SimplexOctaveGenerator noise = new SimplexOctaveGenerator(rand.nextLong(), 1);
		noise.setScale(1/15f);
		double topRadius = 6 + rand.nextInt(12);
		//double topRadSq = topRadius * topRadius;
		double botRadius = 64 + rand.nextInt(48);
		int topHeight = 80 + rand.nextInt(80);
		if(y << 5 > topHeight) return;
		//int midHeight = topHeight - 5 - rand.nextInt(15);
		int botHeight = -32;
		long ox = (cx << 5) + rand.nextInt(32);
		long oz = (cz << 5) + rand.nextInt(32);
		int ay, az;
		double diffHeight = topHeight - botHeight;
		long qx, qz, qy;
		double cDist, rad;
		double thickness = 5.5 + rand.nextInt(6) + rand.nextDouble();
		int lavaHeight = rand.nextInt(topHeight - 12);
		for(int ax = 0; ax < 32; ++ax){
			qx = (x << 5) + ax;
			for(az = 0; az < 32; ++az){
				qz = (z << 5) + az;
				cDist = Math.sqrt(square(ox - qx) + square(oz - qz));
				for(ay = 0; ay < 32; ++ay){
					//if(!Block.byId(data.getBlockId(ax, ay, az)).canBeReplaced(Vanilla.obsidian.uid, (byte)0)) continue;
					qy = (y << 5) + ay;
					if(qy > topHeight || qy < botHeight) return;
					//if(qy >= topHeight + cDist - topRadius) return;
					rad = ((qy - botHeight) / diffHeight) * topRadius + ((diffHeight - (qy - botHeight)) / diffHeight) * botRadius;
					rad += noise.noise(qx, qy, qz, 0.1, 5, true);
					if(cDist > rad) continue;
					if(cDist < rad - thickness && qy > -55){
						if(qy < lavaHeight) data.setBlockId(ax, ay, az, Vanilla.lava.uid);
						else data.setBlockId(ax, ay, az, 0);
					}
					else{
						if(rand.nextInt(11) == 0) data.setBlockId(ax, ay, az, Block.stone.uid);
						else data.setBlockId(ax, ay, az, Vanilla.obsidian.uid);
					}
				}
			}
		}
	}

	private void createZoom(World world) {
		ZoomGenerator bgen = new BiomeGenIslands(world);
		bgen = new BiomeGenZoom(world, bgen, false);
		bgen = new BiomeGenIslands(world, bgen);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSuperBiome(world, bgen);
		bgen = new BiomeGenZoom(world, bgen, false);
		bgen = new BiomeGenBiome(world, bgen, Vanilla.biomes);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSubBiome(world, bgen, 0);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSubBiome(world, bgen, 1);
		bgen = new BiomeGenZoom(world, bgen, true);
		bgen = new BiomeGenSubBiome(world, bgen, 2);
		volcanicCheck = bgen;
		lastWorld = world;
	}

}
