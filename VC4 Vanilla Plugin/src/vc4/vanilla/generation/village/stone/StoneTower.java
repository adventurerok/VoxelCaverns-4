package vc4.vanilla.generation.village.stone;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.Building;
import vc4.vanilla.generation.village.Village;

public class StoneTower implements Building {

	@Override
	public void generate(World world, Door door, Village ville) {
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if (!ville.inBounds(start)) return;
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(7, door.dir);
		if (!ville.inBounds(end)) return;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		RoomBB bb = new RoomBB(sx - 1, start.y, sz - 1, ex + 1, start.y + 22, ez + 1);
		if (!ville.addRoom(bb)) return;
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y - 1; y < start.y + 4; ++y) {
					boolean xWall = x == sx || x == ex;
					boolean zWall = z == sz || z == ez;
					if (y == start.y - 1 || y == start.y + 3) {
						if (xWall || zWall) ville.setBrickBlock(x, y, z);
						else if (y == start.y + 3 && (x == sx + 3 || x == sx + 4) && (z == sz + 3 || z == sz + 4)) ville.setEmptyBlock(x, y, z);
						else ville.setCobbleBlock(x, y, z);
					} else if (xWall || zWall) {
						if (xWall && zWall) ville.setBrickBlock(x, y, z);
						else if (y == start.y + 1) {
							boolean nxWall = x == sx + 1 || x == ex - 1;
							boolean nzWall = z == sz + 1 || z == ez - 1;
							if (nxWall || nzWall) ville.setCobbleBlock(x, y, z);
							else ville.setGlassBlock(x, y, z);
						} else ville.setCobbleBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		for (long x = sx + 1; x < ex; ++x) {
			boolean xWall = x == sx + 1 || x == ex - 1;
			boolean nxWall = x == sx + 2 || x == ex - 2;
			for (long z = sz + 1; z < ez; ++z) {
				boolean zWall = z == sz + 1 || z == ez - 1;
				boolean nzWall = z == sz + 2 || z == ez - 2;
				if (!xWall && !zWall) continue;
				for (long y = start.y + 4; y < start.y + 17; ++y) {
					if (xWall && zWall) ville.setBrickBlock(x, y, z);
					else if ((y - start.y) % 4 == 0) ville.setBrickBlock(x, y, z);
					else if ((y - start.y + 2) % 4 == 0 && !nxWall && !nzWall) ville.setGlassBlock(x, y, z);
					else ville.setCobbleBlock(x, y, z);
				}
			}
		}
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y + 17; y < start.y + 19; ++y) {
					boolean xWall = x == sx || x == ex;
					boolean zWall = z == sz || z == ez;
					if (y == start.y + 17) {
						if (xWall || zWall) ville.setBrickBlock(x, y, z);
						else if ((x == sx + 3 || x == sx + 4) && (z == sz + 3 || z == sz + 4)) ville.setEmptyBlock(x, y, z);
						else ville.setCobbleBlock(x, y, z);
					} else if (xWall || zWall) {
						if (xWall && zWall) ville.setBrickBlock(x, y, z);
						else ville.setCobbleBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		Vector3l supLeft = door.left.move(3, door.dir);
		Vector3l supRight = door.right.move(3, door.dir);
		Vector3l ladLeft = door.left.move(4, door.dir);
		Vector3l ladRight = door.right.move(4, door.dir);
		int ladDir = door.dir.opposite().id();
		for (long y = start.y; y < start.y + 18; ++y) {
			ville.setCobbleBlock(supLeft.x, y, supLeft.z);
			ville.setCobbleBlock(supRight.x, y, supRight.z);
			world.setBlockIdData(ladLeft.x, y, ladLeft.z, Vanilla.ladder.uid, ladDir);
			world.setBlockIdData(ladRight.x, y, ladRight.z, Vanilla.ladder.uid, ladDir);
		}
		ville.setEmptyBlock(door.left.x, door.left.y, door.left.z);
		ville.setEmptyBlock(door.left.x, door.left.y + 1, door.left.z);
		ville.setEmptyBlock(door.right.x, door.right.y, door.right.z);
		ville.setEmptyBlock(door.right.x, door.right.y + 1, door.right.z);
		Vector3l backLeft = door.left.move(1, door.dir.opposite());
		Vector3l backRight = door.right.move(1, door.dir.opposite());
		ville.setEmptyBlock(backLeft.x, backLeft.y, backLeft.z);
		ville.setEmptyBlock(backLeft.x, backLeft.y + 1, backLeft.z);
		ville.setEmptyBlock(backRight.x, backRight.y, backRight.z);
		ville.setEmptyBlock(backRight.x, backRight.y + 1, backRight.z);
	}

	@Override
	public void generateExtra(World world, Door door, Village ville, long y) {

	}

}
