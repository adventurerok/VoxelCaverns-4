/**
 * 
 */
package vc4.vanilla.generation.dungeon;

import java.util.Collection;

import vc4.api.world.World;

/**
 * @author paul
 *
 */
public abstract class DungeonRoom {

	public abstract Collection<Door> generate(World world, Door door, Dungeon dungeon);
	public abstract int getWeight();
}
