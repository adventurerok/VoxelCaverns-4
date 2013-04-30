package vc4.api.entity;

import java.util.HashMap;
import java.util.Random;

import vc4.api.math.MathUtils;
import vc4.api.util.AABB;
import vc4.api.vector.Vector3d;
import vc4.api.world.Chunk;
import vc4.api.world.World;

public abstract class Entity {

	public static HashMap<Short, Class<? extends Entity>> types = new HashMap<Short, Class<? extends Entity>>();

	public boolean inQuicksand = false;
	public boolean collisionHorizontal = false;
	public boolean collisionVertical = false;
	public boolean collision = false;
	public boolean onGround = false;
	public boolean hadUpdate = false;
	
	public Vector3d position;
	public Vector3d oldPos;
	public boolean isDead = false;
	public World world;

	public boolean checkStep = true;
	
	public Random rand = new Random();

	public double motionX = 0D;
	public double motionY = 0D;
	public double motionZ = 0D;

	public double stepHeight = 0.55D;

	public AABB bounds;

	public Vector3d size = new Vector3d(0.3, 1.6, 0.3);
	
	public int health = 100;

	private boolean noClip;
	
	protected long ticksAlive = 0;
	
	public Entity(World world){
		this.world = world;
	}
	
	public Entity setPosition(double x, double y, double z){
		position = new Vector3d(x, y, z);
		size = getDefaultSize();
		calculateBounds();
		return this;
	}
	
	public void damage(int amount, DamageSource source){
		health -= amount;
		if(health < 1) isDead = true;
	}
	
	public void damage(int amount, DamageSource source, int ticks){
		if(world.getTime() % ticks == 0) damage(amount, source);
	}
	
	protected void calculateBounds(){
		if (bounds == null)
			bounds = AABB.getBoundingBox(0, 0, 0, 0, 0, 0);
		if (Double.isNaN(position.x))
			return;
		bounds.setBounds(position.x - size.x, position.y - size.y, position.z - size.z, position.x + size.x, position.y + size.y, position.z + size.z);
	}
	
	public Vector3d getDefaultSize(){
		return new Vector3d(0.3, 1.6, 0.3);
	}
	
	public Entity addToWorld(){
		long x = MathUtils.floor(position.x) >> 5;
		long y = MathUtils.floor(position.y) >> 5;
		long z = MathUtils.floor(position.z) >> 5;
		Chunk c = world.getChunk(x, y, z);
		if(c == null) return null;
		c.addEntity(this);
		return this;
	}
	
	public boolean noClip() {
		return noClip;
	}
	
	public void move(double motionX, double motionY, double motionZ) {
		if(Math.abs(motionX) < 0.0001 && Math.abs(motionY) < 0.0001 && Math.abs(motionZ) < 0.0001) return;
		if (isDead)
			motionX = motionZ = 0D;
		if (noClip()) {
			oldPos = position.clone();
			bounds.add(motionX, motionY, motionZ);
			position.x = (bounds.minX + bounds.maxX) / 2D;
			position.y = (bounds.minY + bounds.maxY) / 2D;
			position.z = (bounds.minZ + bounds.maxZ) / 2D;
			return;
		}

		double startX = motionX;
		double startY = motionY;
		double startZ = motionZ;
		// calculateBounds();
		AABB ourBounds = this.bounds.clone();
		boolean sneaking = false;
		if (this instanceof EntityLiving) {
			EntityLiving e = (EntityLiving) this;
			sneaking = e.isSneaking();
		}
		if (sneaking && onGround) {
			double amount = 0.050000000000000003D;
			for (; motionX != 0.0D && world.getAABBsInBounds(bounds.getOffsetBoundingBox(motionX, -1D, 0.0D), this).length == 0; startX = motionX) {
				if (motionX < amount && motionX >= -amount) {
					motionX = 0.0D;
					continue;
				}
				if (motionX > 0.0D)
					motionX -= amount;
				else
					motionX += amount;
			}
			for (; motionZ != 0.0D && world.getAABBsInBounds(bounds.getOffsetBoundingBox(0.0D, -1D, motionZ), this).length == 0; startZ = motionZ) {
				if (motionZ < amount && motionZ >= -amount) {
					motionZ = 0.0D;
					continue;
				}

				if (motionZ > 0.0D)
					motionZ -= amount;
				else
					motionZ += amount;
			}
			while (motionX != 0.0D && motionZ != 0.0D && world.getAABBsInBounds(bounds.getOffsetBoundingBox(motionX, -1D, motionZ), this).length == 0) {
				if (motionX < amount && motionX >= -amount)
					motionX = 0.0D;
				else if (motionX > 0.0D)
					motionX -= amount;
				else
					motionX += amount;

				if (motionZ < amount && motionZ >= -amount)
					motionZ = 0.0D;
				else if (motionZ > 0.0D)
					motionZ -= amount;
				else
					motionZ += amount;

				startX = motionX;
				startZ = motionZ;
			}
		}

		AABB in[] = world.getAABBsInBounds(bounds.include(motionX, motionY, motionZ), this);

		for (int s = 0; s < in.length; ++s)
			motionY = in[s].calculateYOffset(bounds, motionY);
		bounds.add(0.0D, motionY, 0.0D);
		boolean grounded = (startY != motionY && startY < 0.0D);

		for (int s = 0; s < in.length; ++s)
			motionX = in[s].calculateXOffset(bounds, motionX);
		bounds.add(motionX, 0.0D, 0.0D);

		for (int s = 0; s < in.length; ++s)
			motionZ = in[s].calculateZOffset(bounds, motionZ);
		bounds.add(0.0D, 0.0D, motionZ);

		boolean notMovedFull = startX != motionX || startZ != motionZ;
		if (stepHeight > 0.001F && grounded && notMovedFull && checkStep && !inQuicksand) {
			double stepX = motionX;
			double stepY = motionY;
			double stepZ = motionZ;
			motionX = startX;
			motionY = stepHeight;
			motionZ = startZ;
			AABB ourBounds2 = this.bounds.clone();
			bounds.setBB(ourBounds);
			AABB in2[] = world.getAABBsInBounds(bounds.include(motionX, motionY, motionZ), this);
			for (int s = 0; s < in2.length; ++s) {
				motionY = in2[s].calculateYOffset(bounds, motionY);
			}
			bounds.add(0.0D, motionY, 0.0D);
			for (int s = 0; s < in2.length; ++s) {
				motionX = in2[s].calculateXOffset(bounds, motionX);
			}
			bounds.add(motionX, 0.0D, 0.0D);
			for (int s = 0; s < in2.length; ++s) {
				motionZ = in2[s].calculateZOffset(bounds, motionZ);
			}
			bounds.add(0.0D, 0.0D, motionZ);

			if (MathUtils.equals(startX, motionX) && MathUtils.equals(startZ, motionZ)) {
				motionY = -stepHeight;
				for (int s = 0; s < in2.length; ++s) {
					motionY = in2[s].calculateYOffset(bounds, motionY);
				}
				bounds.add(0.0D, motionY, 0.0D);
			}

			if (stepX * stepX + stepZ * stepZ >= motionX * motionX + motionZ * motionZ) {
				motionX = stepX;
				motionY = stepY;
				motionZ = stepZ;
				bounds.setBB(ourBounds2);
			}
		}

		if (!MathUtils.equals(startX, motionX))
			this.motionX = 0;
		if (!MathUtils.equals(startY, motionY))
			this.motionY = 0;
		if (!MathUtils.equals(startZ, motionZ))
			this.motionZ = 0;
		oldPos = position.clone();
		// bounds.add(motionX, motionY, motionZ);
		position.x = (bounds.minX + bounds.maxX) / 2D;
		position.y = (bounds.minY + bounds.maxY) / 2D;
		position.z = (bounds.minZ + bounds.maxZ) / 2D;
		collisionHorizontal = !MathUtils.equals(startX, motionX) || !MathUtils.equals(startZ, motionZ);
		collisionVertical = !MathUtils.equals(startY, motionY);
		onGround = collisionVertical && startY < 0;
		collision = collisionHorizontal || collisionVertical;
		// calculateBounds();

		long mix = MathUtils.floor(bounds.minX + 0.001D);
		long miy = MathUtils.floor(bounds.minY + 0.001D);
		long miz = MathUtils.floor(bounds.minZ + 0.001D);
		long max = MathUtils.floor(bounds.maxX - 0.001D);
		long may = MathUtils.floor(bounds.maxY - 0.001D);
		long maz = MathUtils.floor(bounds.maxZ - 0.001D);

		for (long qx = mix; qx <= max; qx++) {
			for (long qy = miy; qy <= may; qy++) {
				for (long qz = miz; qz <= maz; qz++) {
//					int id = world.getBlockId(qx, qy, qz);
//					if (id > 0)
//						Block.byId(id).onEntityCollidedWith(world, qx, qy, qz, this);
				}
			}
		}

		return;
	}
	
	public int getMaxHealth(){
		return 100;
	}

	public boolean canCollide(Entity test) {
		return true;
	}

	public void update() {
		++ticksAlive;
		move(motionX, motionY, motionZ);
		
	}

	public boolean needsRemoving() {
		return isDead;
	}

	public void draw() {
		
		
	}

	public Entity setVelocity(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
		return this;
	}
	
}
