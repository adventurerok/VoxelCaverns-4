package vc4.vanilla.generation.village.stone;

import java.util.ArrayList;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.furnature.*;
import vc4.vanilla.generation.village.Building;
import vc4.vanilla.generation.village.Village;

public class StoneMine implements Building {

	private static ArrayList<Furnature> furniture = new ArrayList<>();
	private static Village lastVille;

	public static void loadFurnature(Village ville) {
		furniture.clear();
		furniture.add(new FurnatureTorch(new Adjustment(2, -2, 2), Vanilla.torch.uid, 2));
		furniture.add(new FurnatureTorch(new Adjustment(5, -2, 2), Vanilla.torch.uid, 2));
		furniture.add(new FurnatureTorch(new Adjustment(2, 3, 2), Vanilla.torch.uid, 4));
		furniture.add(new FurnatureTorch(new Adjustment(5, 3, 2), Vanilla.torch.uid, 4));
		lastVille = ville;
	}

	@Override
	public void generate(World world, Door door, Village ville) {
		if (ville != lastVille) loadFurnature(ville);
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
		Vector3l[] lads = new Vector3l[8];
		Direction clock = door.dir.clockwise();
		lads[0] = door.left.move(5, door.dir);
		lads[1] = door.left.move(4, door.dir).move(2, clock);
		lads[2] = door.left.move(2, door.dir);
		lads[3] = door.left.move(4, door.dir).move(-1, clock);
		lads[4] = door.right.move(5, door.dir);
		lads[5] = door.left.move(3, door.dir).move(2, clock);
		lads[6] = door.right.move(2, door.dir);
		lads[7] = door.left.move(3, door.dir).move(-1, clock);
		int ind;
		Vector3l c;
		for (long x = sx + 1; x <= ex - 1; ++x) {
			boolean xWall = x == sx + 1 || x == ex - 1;
			for (long z = sz + 1; z <= ez - 1; ++z) {
				boolean zWall = z == sz + 1 || z == ez - 1;
				for (long y = start.y - 1; y > start.y - 20; --y) {
					if (xWall || zWall) {
						ville.setCobbleBlock(x, y, z);
						continue;
					}
					c = new Vector3l(x, y, z);
					ind = -1;
					for (int num = 0; num < 8; ++num) {
						if (c.horizontalEquals(lads[num])) {
							ind = num;
							break;
						}
					}
					if (ind != -1) {
						world.setBlockIdData(x, y, z, Vanilla.ladder.uid, ((ind & 3) + door.dir.id()) & 3);
					} else if (y == start.y - 1 || (y & 127) == 0) ville.setGlassBlock(x, y, z);
					else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		for (Furnature f : furniture) {
			f.place(ville.getWorld(), door.left, door.dir);
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
		if (((y << 5) | 31) <= depth) return;
		long sy = Math.min(door.left.y - 10, (y << 5) | 31);
		long ey = Math.max(y << 5, depth);
		Vector3l[] lads = new Vector3l[8];
		Direction clock = door.dir.clockwise();
		lads[0] = door.left.move(5, door.dir);
		lads[1] = door.left.move(4, door.dir).move(2, clock);
		lads[2] = door.left.move(2, door.dir);
		lads[3] = door.left.move(4, door.dir).move(-1, clock);
		lads[4] = door.right.move(5, door.dir);
		lads[5] = door.left.move(3, door.dir).move(2, clock);
		lads[6] = door.right.move(2, door.dir);
		lads[7] = door.left.move(3, door.dir).move(-1, clock);
		int ind;
		Vector3l c;
		for (long ay = sy; ay >= ey; --ay) {
			boolean spec = (ay & 127) == 0;
			for (long x = sx + 1; x <= ex - 1; ++x) {
				boolean xWall = x == sx + 1 || x == ex - 1;
				for (long z = sz + 1; z <= ez - 1; ++z) {
					boolean zWall = z == sz + 1 || z == ez - 1;
					if (xWall || zWall) {
						ville.setCobbleBlock(x, ay, z);
						continue;
					}
					c = new Vector3l(x, ay, z);
					ind = -1;
					for (int num = 0; num < 8; ++num) {
						if (c.horizontalEquals(lads[num])) {
							ind = num;
							break;
						}
					}
					if (ind != -1) {
						world.setBlockIdData(x, ay, z, Vanilla.ladder.uid, ((ind & 3) + door.dir.id()) & 3);
					} else if (spec) {
						ville.setGlassBlock(x, ay, z);
					} else ville.setEmptyBlock(x, ay, z);

				}
			}
		}

	}

}
