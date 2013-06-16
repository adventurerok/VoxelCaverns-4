package vc4.vanilla.generation.village.stone;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.Building;
import vc4.vanilla.generation.village.Village;

public class StoneMine implements Building {

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
		RoomBB bb = new RoomBB(sx - 1, start.y, sz - 1, ex + 1, start.y + 3, ez + 1);
		if (!ville.addRoom(bb)) return;
		for (long x = sx; x <= ex; ++x) {
			boolean xWall = x == sx || x == ex;
			for (long z = sz; z <= ez; ++z) {
				boolean zWall = z == sz || z == ez;
				for (long y = start.y - 1; y < start.y + 4; ++y) {
					if (y == start.y - 1 || y == start.y + 3) {
						if (xWall || zWall) ville.setBrickBlock(x, y, z);
						else ville.setCobbleBlock(x, y, z);
					} else if (xWall || zWall) {
						if (xWall && zWall) ville.setBrickBlock(x, y, z);
						else ville.setCobbleBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		for (long x = sx + 1; x <= ex - 1; ++x) {
			boolean xWall = x == sx + 1 || x == ex - 1;
			for (long z = sz + 1; z <= ez - 1; ++z) {
				boolean zWall = z == sz + 1 || z == ez - 1;
				for (long y = start.y - 1; y > start.y - 20; --y) {
					if (xWall || zWall) ville.setCobbleBlock(x, y, z);
					else ville.setEmptyBlock(x, y, z);
				}
			}
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
		RoomBB bb = new RoomBB(sx - 1, start.y, sz - 1, ex + 1, start.y + 3, ez + 1);
		if (!ville.addRoom(bb)) return;
		if ((y << 5) >= door.left.y - 10) return;
		long depth = -512 - ville.getRand().nextInt(768);
		if(((y << 5) | 31) <= depth) return;
		long sy = Math.min(door.left.y - 10, (y << 5) | 31);
		long ey = Math.max(y << 5, depth);
		Vector3l a = null;
		Vector3l b = null;
		for (long ay = sy; ay >= ey; --ay) {
			boolean spec = (ay & 127) == 0;
			if(spec){
				a = door.left.move(5, door.dir);
				b = door.right.move(5, door.dir);
			}
			for (long x = sx + 1; x <= ex - 1; ++x) {
				boolean xWall = x == sx + 1 || x == ex - 1;
				for (long z = sz + 1; z <= ez - 1; ++z) {
					boolean zWall = z == sz + 1 || z == ez - 1;
					if (xWall || zWall) ville.setCobbleBlock(x, ay, z);
					else if(spec){
						Vector3l c = new Vector3l(x, ay, z);
						if(c.equals(a) || c.equals(b)) ville.setEmptyBlock(x, ay, z);
						else ville.setGlassBlock(x, ay, z);
					}
					else ville.setEmptyBlock(x, ay, z);

				}
			}
		}

	}

}
