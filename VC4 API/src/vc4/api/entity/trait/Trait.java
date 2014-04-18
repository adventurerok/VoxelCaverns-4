package vc4.api.entity.trait;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import vc4.api.entity.Entity;
import vc4.api.logging.Logger;
import vc4.api.vbt.TagCompound;

public abstract class Trait {

	Entity entity;

	private static HashMap<String, Constructor<? extends Trait>> types = new HashMap<String, Constructor<? extends Trait>>();

	static {
		registerTrait("inventory", TraitInventory.class);
	}

	public static void registerTrait(String name, Class<? extends Trait> clz) {
		try {
			types.put(name, clz.getConstructor(Entity.class));
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(Entity.class).warning("Entity class does not have correct constructor", e);
		}
	}

	public static Constructor<? extends Trait> getTraitType(String name) {
		return types.get(name);
	}

	public static Trait loadTrait(Entity entity, TagCompound tag) {
		short id = tag.getShort("id");
		String name = entity.world.getTraitName(id);
		Constructor<? extends Trait> clz = getTraitType(name);
		try {
			Trait e = clz.newInstance(entity);
			e.loadSaveCompound(tag);
			return e;
		} catch (Exception e) {
			Logger.getLogger(Trait.class).warning("Exception while loading trait: " + name, e);
		}
		return null;
	}

	public Trait(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public abstract void update();

	public abstract String name();

	public abstract boolean persistent();

	public short getId() {
		return entity.world.getRegisteredTrait(name());
	}

	public TagCompound getSaveCompound() {
		TagCompound root = new TagCompound("root");
		root.setShort("id", getId());
		return root;
	}

	public void loadSaveCompound(TagCompound tag) {

	}

	public void onDeath() {

	}

	public void onDamage() {

	}
}
