package vc4.api.itementity;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.jnbt.CompoundTag;

import vc4.api.entity.Entity;
import vc4.api.logging.Logger;
import vc4.api.world.World;

public abstract class ItemEntity {

	public static HashMap<String, Constructor<? extends ItemEntity>> types = new HashMap<>();

	public abstract String getName();

	public short getId(World world) {
		return world.getRegisteredItemEntity(getName());
	}

	public boolean canCombine(ItemEntity entity) {
		return false;
	}

	@Override
	public abstract ItemEntity clone();

	public CompoundTag getSaveCompound(World world) {
		CompoundTag tag = new CompoundTag("tag");
		tag.setShort("id", getId(world));
		return tag;
	}

	public static Constructor<? extends ItemEntity> getItemEntityType(String name) {
		return types.get(name);
	}

	public void loadSaveCompound(World world, CompoundTag tag) {

	}

	public static ItemEntity loadItemEntity(World world, CompoundTag tag) {
		short id = tag.getShort("id");
		String name = world.getItemEntityName(id);
		Constructor<? extends ItemEntity> clz = getItemEntityType(name);
		try {
			ItemEntity e = clz.newInstance();
			e.loadSaveCompound(world, tag);
			return e;
		} catch (Exception e) {
			Logger.getLogger(Entity.class).warning("Exception while loading item entity: " + name, e);
		}
		return null;
	}

	public static void registerEntity(String name, Class<? extends ItemEntity> c) {
		try {
			types.put(name, c.getConstructor());
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(ItemEntity.class).warning("Failed to register itementity: " + name, e);
		}
	}

	public static void initClass() {
	}

	static {
		registerEntity("enchantment", ItemEntityEnchantment.class);
	}
}
