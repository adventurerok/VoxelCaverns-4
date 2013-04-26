/**
 * 
 */
package vc4.vanilla.generation.dungeon.room;

import java.util.Collection;

import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.Dungeon;

/**
 * @author paul
 *
 */
public abstract class DungeonRoom {

	public abstract Collection<Door> generate(World world, Door door, Dungeon dungeon);
}
