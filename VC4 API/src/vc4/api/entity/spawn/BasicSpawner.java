package vc4.api.entity.spawn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import vc4.api.entity.Entity;
import vc4.api.logging.Logger;
import vc4.api.world.World;

public class BasicSpawner implements Spawner {

	Constructor<? extends Entity> constructor;

	public BasicSpawner(Class<? extends Entity> clz) {
		try {
			constructor = clz.getConstructor(World.class);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(BasicSpawner.class).severe("Constructor not found. Ensure entity class has Entity(World) constructor", e);
		}
	}

	@Override
	public void spawnMob(World world, double x, double y, double z, Random rand) {
		try {
			Entity e = constructor.newInstance(world);
			e.setPosition(x, y, z);
			e.addToWorld();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.getLogger(BasicSpawner.class).warning("Failed to spawn entity", e);
		}
	}

}
