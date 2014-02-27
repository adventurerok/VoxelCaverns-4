package vc4.vanilla.generation.village;

import org.jnbt.CompoundTag;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3d;
import vc4.api.world.ThingImporter;
import vc4.api.world.World;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.generation.dungeon.Door;

public class BuildingThing implements Building, ThingImporter {

	Village current;

	@Override
	public void importBlock(World world, long x, long y, long z, int id, int data) {
		if (id < 3) {
			if (id == 0) current.setEmptyBlock(x, y, z);
			else if (id == 1) current.setLogBlock(x, y, z);
			else if (id == 2) current.setPlankBlock(x, y, z);
		} else {
			if (id == 3) current.setBrickBlock(x, y, z);
			else if (id == 4) current.setCobbleBlock(x, y, z);
			else if (id == 5) current.setGlassBlock(x, y, z);
		}
	}

	@Override
	public void importSpecial(World world, Vector3d base, Direction dir, CompoundTag special) {
		int id = special.getInt("id");
		if (id == 0) {
			Vector3d pos = special.getCompoundTag("pos").readVector3d();
			pos = base.adjust(pos, dir);
			EntityNpc npc = new EntityNpc(current.getWorld());
			npc.setPosition(pos.x, pos.y, pos.z);
		}
	}

	@Override
	public void generate(World world, Door door, Village ville) {
		// TASK Auto-generated method stub

	}

	@Override
	public void generateExtra(World world, Door door, Village ville, long y) {
		// TASK Auto-generated method stub

	}

}
