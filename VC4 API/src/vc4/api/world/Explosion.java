package vc4.api.world;


import java.util.*;

import vc4.api.block.Block;
import vc4.api.entity.DamageSource;
import vc4.api.entity.Entity;
import vc4.api.math.MathUtils;
import vc4.api.sound.Audio;
import vc4.api.util.AABB;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

public class Explosion {
	public static Random rand = new Random();

	/** whether or not the explosion sets fire to blocks around it */
	public boolean onFire;
	private World world;
	public double x;
	public double y;
	public double z;
	public float dropChance = 0.3F;
	public Entity exploder;
	public float explosionSize;
	public Set<Vector3l> explosionPositions;

	public Explosion(World world, Entity exploder, double xPos, double yPos, double zPos, float size) {
		onFire = false;
		explosionPositions = new HashSet<Vector3l>();
		this.world = world;
		this.exploder = exploder;
		explosionSize = size;
		x = xPos;
		y = yPos;
		z = zPos;
	}

	/**
	 * Does the first part of explosion (destroy blocks)
	 */
	public void calculateExplosionDamage() {
		float size = explosionSize;
		int max = 16;

		int xn, yn, zn;
		double xc, yc, zc, dist, rx, ry, rz, powerCurrent, powerChange;
		long bx, by, bz;
		
		for (xn = 0; xn < max; xn++) {
			for (yn = 0; yn < max; yn++) {
				yLoop:

				for (zn = 0; zn < max; zn++) {
					if (xn != 0 && xn != max - 1 && yn != 0 && yn != max - 1 && zn != 0 && zn != max - 1) {
						continue;
					}

					xc = (xn / (max - 1)) * 2F - 1F;
					yc = (yn / (max - 1)) * 2F - 1F;
					zc = (zn / (max - 1)) * 2F - 1F;
					dist = Math.sqrt(xc * xc + yc * yc + zc * zc);
					xc /= dist;
					yc /= dist;
					zc /= dist;
					powerCurrent = explosionSize * (0.7F + rand.nextFloat() * 0.6F);
					rx = x;
					ry = y;
					rz = z;
					powerChange = 0.3F;

					while(true){
						if (powerCurrent <= 0.0F) continue yLoop;

						bx = MathUtils.floor(rx);
						by = MathUtils.floor(ry);
						bz = MathUtils.floor(rz);
						int bid = world.getBlockId(bx, by, bz);

						if (bid > 0) {
							powerCurrent -= (Block.byId(bid).getBlastResistance(world, bx, by, bz, exploder) + 0.3F) * powerChange;
						}

						if (powerCurrent > 0F) {
							explosionPositions.add(new Vector3l(bx, by, bz));
						}

						rx += xc * powerChange;
						ry += yc * powerChange;
						rz += zc * powerChange;
						powerCurrent -= powerChange * 0.75F;
					} 
				}
			}
		}

		explosionSize *= 2.0F;
		int minX = (int) MathUtils.floor(x - explosionSize - 1D);
		int maxX = (int) MathUtils.floor(x + explosionSize + 1D);
		int minY = (int) MathUtils.floor(y - explosionSize - 1D);
		int maxY = (int) MathUtils.floor(y + explosionSize + 1D);
		int minZ = (int) MathUtils.floor(z - explosionSize - 1D);
		int maxZ = (int) MathUtils.floor(z + explosionSize + 1D);
		List<Entity> list = world.getEntitiesInBoundsExcluding(AABB.getBoundingBox(minX, maxX, minY, maxY, minZ, maxZ), exploder);
		Vector3d pos = new Vector3d(x, y, z);

		for (int k2 = 0; k2 < list.size(); k2++) {
			Entity entity = list.get(k2);
			double d4 = entity.position.distance(pos) / explosionSize;

			if (d4 <= 1.0D) {
				double xDist = entity.position.x - x;
				double yDist = entity.position.y - y;
				double zDist = entity.position.z - z;
				double d11 = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
				xDist /= d11;
				yDist /= d11;
				zDist /= d11;
				double d12 = world.getBlockDensity(pos, entity.bounds);
				double d13 = (1.0D - d4) * d12;
				entity.damage((int) (((d13 * d13 + d13) / 2D) * 32D * explosionSize + 1.0D), DamageSource.explosionDamage);
				double d14 = d13;
				entity.motionX += xDist * d14;
				entity.motionY += yDist * d14;
				entity.motionZ += zDist * d14;
			}
		}

		explosionSize = size;
		ArrayList<Vector3l> arraylist = new ArrayList<Vector3l>();
		arraylist.addAll(explosionPositions);
	}

	public void explode() {
		String s = "small";
		if(explosionSize > 25) s = "big";
		else if(explosionSize > 5) s = "medium";
		//SoundPlayer.playSound("explosion." + s, x, y, z, 4.0F, 1.0F);
		Audio.playSound("explosion/" + s, x, y, z, 4f, 1f);
		ArrayList<Vector3l> toExplode = new ArrayList<Vector3l>();
		toExplode.addAll(explosionPositions);

		Vector3l bPos;
		long bx, by, bz;
		
		for (int d = toExplode.size() - 1; d >= 0; d--) {
			bPos = toExplode.get(d);
			bx = bPos.x;
			by = bPos.y;
			bz = bPos.z;
			int bid = world.getBlockId(bx, by, bz);

			//smoke

			if (bid > 0) {
				if(rand.nextFloat() <= dropChance) Block.byId(bid).dropItems(world, bx, by, bz, null);
				Block.byId(bid).onBlockExploded(world, bx, by, bz, exploder);
				world.setBlockId(bx, by, bz, 0);
				
			}
		}

	}
}
