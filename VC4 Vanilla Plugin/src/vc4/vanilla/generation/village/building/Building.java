package vc4.vanilla.generation.village.building;

import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.village.Village;

public interface Building {

	public void generate(World world, Door door, Village ville);
	public void generateExtra(World world, Door door, Village ville, long y);
}
