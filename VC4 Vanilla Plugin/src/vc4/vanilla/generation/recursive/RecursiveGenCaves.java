package vc4.vanilla.generation.recursive;


import java.util.Random;

import vc4.api.block.Block;
import vc4.api.block.BlockFluid;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.RecursiveGenerator;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.underbiome.UnderBiome;

public class RecursiveGenCaves extends RecursiveGenerator{

	/**
	 * 
	 */
	public RecursiveGenCaves() {
		range = range - 1;
	}
	
	@Override
	protected void generateRecursive(World world, MapData map, Random rand, long x, long y, long z, long cx, long cy,
			long cz, GeneratorOutput data) {
		UnderBiome biom = UnderBiome.byId(Vanilla.underBiomesGen.generate((cx << 5) + 16, (cz << 5) + 16, 1)[0]);
		if(biom.cavesAmount < 1) return;
		int change = rand.nextInt(rand.nextInt(biom.cavesChance - (int)Math.max(-20, Math.min(cy, 50))) + 1);
		if(change != 0) return;
		int var = biom.cavesAmount + (int)Math.max(0, Math.min(-cy * 0.2, 3));
		int amount = rand.nextInt(rand.nextInt(var) + 1);
		for(int dofor = 0; dofor < amount; ++dofor){
			double sx = rand.nextInt(32) + rand.nextDouble() + (cx * 32);
			double sy = rand.nextInt(32) + rand.nextDouble() + (cy * 32);
			double sz = rand.nextInt(32) + rand.nextDouble() + (cz * 32);
			float yaw = rand.nextFloat() * (float)Math.PI * 2.0F;
	        float pitch = ((rand.nextFloat() - 0.5F) * 2.0F) / 8F;
	        float stride = rand.nextFloat() * 2.0F + rand.nextFloat();
	        generateCaveNode(rand, x, y, z, cx, cy, cz, data, sx, sy, sz, yaw, pitch, stride, 0, 3 + rand.nextInt(rand.nextInt(4) + 1));
		}

	}

	public void generateCaveNode(Random rand, long x, long y, long z, long cx, long cy,
			long cz, GeneratorOutput data, double sx, double sy, double sz, float yaw, float pitch, float stride, int length, int radius){

		long actX = x * 32;
		long actY = y * 32;
		long actZ = z * 32;
		sx -= actX;
		sy -= actY;
		sz -= actZ;
		

		float yawAdd = 0.0F;
		float pitchAdd = 0.0F;

		boolean curveLess = rand.nextInt(6) == 0;

		if(length <= 0){
			int longRange = range * 32 - 32;
			length = longRange - rand.nextInt(longRange / 3);
		}

		int start = 0;
		for(; start < length; ++start){
			//double addFactor = 1.5D + (MathUtils.sin((start * (float)Math.PI) / length) * stride * 1.0F);

			float yMod = MathUtils.cos(pitch);
			float yAdd = MathUtils.sin(pitch);
			sy += yAdd;
			sx += MathUtils.cos(yaw) * yMod * 2;
			sz += MathUtils.sin(yaw) * yMod * 2;

			if(curveLess) pitch *= 0.92F;
			else pitch *= 0.7F;

			pitch += pitchAdd * 0.1F;
			yaw += yawAdd * 0.1F;
			pitchAdd *= 0.9F;
			yawAdd *= 0.75F;

			pitchAdd += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 2.0F;
			yawAdd += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 4F;

//			double startX = sx - actX;
//			double startY = sy - actY;
//			double startZ = sz - actZ;
//			//double d7 = stride + 2.0F + 16F;
//
//			startX = MathUtils.floorDouble(sx - addFactor) - x * 32 - 1;
//			long endX = (MathUtils.floorDouble(sx + addFactor) - x * 32) + 1;
//			startY = MathUtils.floorDouble(sy - addFactor) - y * 32 - 1;
//			long endY = (MathUtils.floorDouble(sy + addFactor) - y * 32) + 1;
//			startZ = MathUtils.floorDouble(sz - addFactor) - z * 16 - 1;
//			long endZ = (MathUtils.floorDouble(sz + addFactor) - z * 32) + 1;
//
//			if (startX < 0) startX = 0;
//			if (endX > 32) endX = 32;
//			if (startY < 0) startZ = 0;
//			if (endY > 32) endY = 32;
//			if (startZ < 0) startY = 0;
//			if (endZ > 32) endZ = 32;

//			boolean hitWater = false;
//
//			for (long nx = (int) startX; !hitWater && nx < endX; nx++)
//			{
//				for (long nz = (int) startZ; !hitWater && nz < endZ; nz++)
//				{
//					for (long ny = endY + 1; !hitWater && ny >= startY - 1; ny--)
//					{
//						int at = Chunk.arrayCalc(nx, ny, nz);
//
//						if (data.getBlock(at) == Block.waterFlowing.blockID || data.getBlock(at) == Block.waterStill.blockID) hitWater = true;
//
////						if (nz != startZ - 1 && nz != startX && nx != endX - 1 && nz != startZ && nz != endZ - 1) ny = (int) startZ;
//					}
//				}
//			}
//			if (hitWater) continue;
			
			int ir = (int) (radius + 1);
			
			Vector3d center = new Vector3d(sx, sy, sz);
			
			for(int nx = (int) MathUtils.floor(sx - ir); nx < sx + ir; ++nx){
				for(int ny = (int) MathUtils.floor(sy - ir); ny < sy + ir; ++ny){
					for(int nz = (int) MathUtils.floor(sz - ir); nz < sz + ir; ++nz){
						if(nx < 0 || nx > 31 || ny < 0 || ny > 31 || nz < 0 || nz > 31) continue;
						if(center.distance(new Vector3d(nx, ny, nz)) > radius) continue;
						if(Block.byId(data.getBlockId(nx, ny, nz)) instanceof BlockFluid) continue;
						data.setBlockId(nx, ny, nz, (short) 0);
					}
				}
			}
			
			
		}
	}

}
