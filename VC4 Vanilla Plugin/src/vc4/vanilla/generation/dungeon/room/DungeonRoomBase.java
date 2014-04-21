/**
 * 
 */
package vc4.vanilla.generation.dungeon.room;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.client.Client;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.entity.EntityNpc.NpcState;
import vc4.vanilla.generation.dungeon.*;
import vc4.vanilla.generation.dungeon.style.DungeonStyle;

/**
 * @author paul
 * 
 */
public class DungeonRoomBase extends DungeonRoom {

	static ArrayList<DungeonRoom> roomsToGen = new ArrayList<>();

	static {
		roomsToGen.add(new DungeonRoomBase());
		roomsToGen.add(new DungeonRoomCorridor());
		roomsToGen.add(new DungeonRoomLeftTurn());
		roomsToGen.add(new DungeonRoomRightTurn());
		roomsToGen.add(new DungeonRoomStairs());
		roomsToGen.add(new DungeonRoomPit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.vanilla.generation.dungeon.DungeonRoom#generate(vc4.api.world.World, vc4.vanilla.generation.dungeon.Door)
	 */
	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		result.add(door.clone().flip());
		result.add(door.move(7, door.dir));
		result.add(Door.genDoor(door.left.move(3, door.dir.counterClockwise()).move(3, door.dir), door.dir));
		result.add(Door.genDoor(door.right.move(3, door.dir.clockwise()).move(3, door.dir), door.dir));
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if (!dungeon.inBounds(start)) return null;
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(7, door.dir);
		if (!dungeon.inBounds(end)) return null;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		//if(ex - sx == 5 || ez - sz == 5) Logger.getLogger(DungeonRoomBase.class).debug("DL: " + door.left + ", DR: " + door.right + ", DF: " + door.dir);
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 3, ez - 1);
		RoomInfo info = new RoomInfo(bb, result);
		if (!dungeon.addRoom(info)) return null;
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y - 1; y < start.y + 5; ++y) {
					if (y == start.y - 1 || y == start.y + 4) {
						dungeon.setDungeonBlock(x, y, z);
					} else if (x == sx || x == ex || z == sz || z == ez) {
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		EntityNpc prisoner = new EntityNpc(world);
		prisoner.setPosition(sx + 4, start.y + 0.93, sz + 4);
		dungeon.setTorchBlock(sx + 1 + dungeon.getRand().nextInt(6), start.y + 1 + dungeon.getRand().nextInt(2), sz + 1 + dungeon.getRand().nextInt(6));
		prisoner.moveYaw = dungeon.getRand().nextInt(360) + dungeon.getRand().nextDouble();
		prisoner.setFirstName(dungeon.randomFirstName());
		prisoner.setLastName(dungeon.randomLastName());
		prisoner.setMan(dungeon.getRand().nextBoolean());
		prisoner.setState(NpcState.TRAPPED);
		prisoner.setSkinId((byte) dungeon.getRand().nextInt());
		prisoner.addToWorld();
		return result;
	}
	
	public boolean generate(World world, long x, long y, long z) {
		return generate(world, x, y, z, false);
	}

	public boolean generate(World world, long x, long y, long z, boolean force) {
		Random rand = world.createRandom(x, y, z, 1263763L);
		if(!force){
			if (y > -3 || y < -185) return false;
			if (rand.nextInt(Client.debugMode() ? 150 : 500) != 0) return false;
		}
		x <<= 5;
		y <<= 5;
		z <<= 5;
		x += 16;
		y += 16;
		z += 16;
		Direction dir = Direction.getDirection(rand.nextInt(4));
		Door d = Door.genDoor(new Vector3l(x, y, z), dir);
		dir = dir.counterClockwise();
		d.dir = dir;
		Dungeon dungeon = new Dungeon(world, x - 40, y - 30, z - 40, x + 40, y + 30, z + 40, rand);
		if (dungeon.getStyle() == null) return false;
		ConcurrentLinkedQueue<Door> doorsToGen = new ConcurrentLinkedQueue<Door>();
		doorsToGen.addAll(nextRoom(dungeon.getStyle(), rand).generate(world, d, dungeon));
		int rooms = 1;
		Collection<Door> gened = null;
		while ((d = doorsToGen.poll()) != null) {
			if (!dungeon.inBounds(d.left) || (rand.nextDouble() < dungeon.getStyle().getRoomFailChance() && rooms > 8)) continue;
			DungeonRoom room = nextRoom(dungeon.getStyle(), rand);
			gened = room.generate(world, d, dungeon);
			if(gened != null){
				doorsToGen.addAll(gened);
				++rooms;
			}
			if (dungeon.getStyle().getMaxRooms() != -1 && rooms > dungeon.getStyle().getMaxRooms()) break;
		}
		dungeon.clearUsedDoors();
		return true;
	}

	public static DungeonRoom nextRoom(DungeonStyle style, Random rand) {
		int max = 0;
		for (WeightedRoom d : style.getRooms()) {
			max += d.getWeight();
		}
		int num = rand.nextInt(max + 1);
		for (WeightedRoom d : style.getRooms()) {
			num -= d.getWeight();
			if (num <= 0) return d.getRoom();
		}
		return null;
	}

}
