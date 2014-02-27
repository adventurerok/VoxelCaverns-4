package vc4.api.area;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.jnbt.CompoundTag;

import vc4.api.entity.Entity;
import vc4.api.logging.Logger;
import vc4.api.math.MathUtils;
import vc4.api.util.AABB;
import vc4.api.world.Chunk;
import vc4.api.world.World;

public abstract class Area {

	private static HashMap<String, Constructor<? extends Area>> types = new HashMap<>();

	public World world;
	public AABB bounds;

	public static void registerArea(String name, Class<? extends Area> clz) {
		try {
			types.put(name, clz.getConstructor(World.class));
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(Entity.class).warning("Entity class does not have correct constructor", e);
		}
	}

	public void setBounds(AABB bounds) {
		this.bounds = bounds;
	}

	public AABB getBounds() {
		return bounds;
	}

	public World getWorld() {
		return world;
	}

	public static Area loadArea(World world, CompoundTag tag) {
		short id = tag.getShort("id");
		String name = world.getAreaName(id);
		Constructor<? extends Area> clz = getAreaType(name);
		try {
			Area e = clz.newInstance(world);
			e.loadSaveCompound(tag);
			return e;
		} catch (Exception e) {
			Logger.getLogger(Entity.class).warning("Exception while loading area: " + name, e);
		}
		return null;
	}

	public CompoundTag getSaveCompound() {
		CompoundTag tag = new CompoundTag("tag");
		tag.setShort("id", getId());
		tag.addTag(CompoundTag.createAABBTag("aabb", bounds));
		return tag;
	}

	public void loadSaveCompound(CompoundTag tag) {
		bounds = tag.getCompoundTag("aabb").readAABB();
	}

	public boolean persistent() {
		return true;
	}

	public static Constructor<? extends Area> getAreaType(String name) {
		return types.get(name);
	}

	public Area(World world) {
		super();
		this.world = world;
	}

	public Area(World world, AABB bounds) {
		super();
		this.world = world;
		this.bounds = bounds;
	}

	public abstract String getName();

	public short getId() {
		return world.getRegisteredArea(getName());
	}

	public void updateTick() {

	}

	public Area addToWorld() {
		double dx = (bounds.minX + bounds.maxX) / 2d;
		double dy = (bounds.minY + bounds.maxY) / 2d;
		double dz = (bounds.minZ + bounds.maxZ) / 2d;
		long x = MathUtils.floor(dx) >> 5;
		long y = MathUtils.floor(dy) >> 5;
		long z = MathUtils.floor(dz) >> 5;
		Chunk c = world.getChunk(x, y, z);
		if (c == null) return null;
		c.addArea(this);
		return this;
	}

}
