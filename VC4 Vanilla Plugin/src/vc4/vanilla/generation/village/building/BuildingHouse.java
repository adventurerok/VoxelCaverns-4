package vc4.vanilla.generation.village.building;

import java.util.*;
import java.util.Map.Entry;

import vc4.api.client.Client;
import vc4.api.math.MathUtils;
import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.Village;
import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.style.VillageStyle;

public class BuildingHouse implements Building {

	private static HashMap<Adjustment, Integer> furniture = new HashMap<>();
	private static Village lastVille;
	
	public static void loadFurnature(Village ville){
		furniture.clear();
		furniture.put(new Adjustment(6, 3, 0), (int)Vanilla.table.uid);
		lastVille = ville;
	}
	
	@Override
	public void generate(World world, Door door, Village ville) {
		if(ville != lastVille) loadFurnature(ville);
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if(!ville.inBounds(start)) return;
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(7, door.dir);
		if(!ville.inBounds(end)) return;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		RoomBB bb = new RoomBB(sx - 1, start.y, sz - 1, ex + 1, start.y + 3, ez + 1);
		if(!ville.addRoom(bb)) return;
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 4; ++y){
					boolean xWall = x == sx || x == ex;
					boolean zWall = z == sz || z == ez;
					if(y == start.y - 1 || y == start.y + 3){
						if(xWall || zWall) ville.setLogBlock(x, y, z);
						else ville.setPlankBlock(x, y, z);
					} else if(xWall || zWall){
						if(xWall && zWall) ville.setLogBlock(x, y, z);
						else ville.setPlankBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		for(Entry<Adjustment, Integer> s : furniture.entrySet()){
			Vector3l pal = s.getKey().adjust(door.left, door.dir);
			world.setBlockId(pal.x, pal.y, pal.z, s.getValue());
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
		EntityNpc npc = new EntityNpc(world);
		npc.setPosition(sx + 4, start.y + 0.93, sz + 4);
		npc.yaw = ville.getRand().nextInt(360) + ville.getRand().nextDouble();
		npc.setFirstName(ville.randomFirstName());
		npc.setLastName(ville.randomLastName());
		npc.setMan(ville.getRand().nextBoolean());
		npc.addToWorld();
	}
	
	public void generate(World world, long x, long y, long z){
		Random rand = world.createRandom(x, y, z, 1263763L);
		if(y < -1 || y > 4) return;
		if(rand.nextInt(Client.debugMode() ? 150 : 500) != 0) return;
		x <<= 5;
		y <<= 5;
		z <<= 5;
		x += 16;
		y += 16;
		z += 16;
		Village ville = new Village(world, x - 40, y - 30, z - 40, x + 40, y + 30, z + 40, rand);
		if(ville.getStyle() == null) return;
		int rooms = 5 + rand.nextInt(10);
		for(int a = 0; a < rooms; ++a){
			long bx = x - 35 + rand.nextInt(70);
			long bz = z - 35 + rand.nextInt(70);
			long ay = world.getMapData(bx >> 5, bz >> 5).getHeight((int)(bx & 31), (int)(bz & 31));
			long ly = ay;
			long hy = ay;
			for(long tx = bx - 4; tx <= bx + 4; tx += 8){
				for(long tz = bz - 4; tz <= bz + 4; tz += 8){
					long ty = world.getMapData(tx >> 5, tz >> 5).getHeight((int)(tx & 31), (int)(tz & 31));
					if(ty < ly) ly = ty;
					if(ty > hy) hy = ty;
				}
			}
			if(hy - ly > 4) continue;
			Building room = nextRoom(ville.getStyle(), rand);
			Direction dir =  Direction.getDirection(rand.nextInt(4));
			Door door = Door.genDoor(new Vector3l(bx, MathUtils.floor(((ay + ly + hy) / 3d) + 1), bz), dir);
			door.setNewRoomDir(dir.counterClockwise());
			room.generate(world, door, ville);
		}
		lastVille = null;
	}
	
	public static Building nextRoom(VillageStyle style, Random rand){
		int max = 0;
		for(WeightedBuilding d : style.getBuildings()){
			max += d.getWeight();
		}
		int num = rand.nextInt(max);
		for(WeightedBuilding d : style.getBuildings()){
			num -= d.getWeight();
			if(num <= 0) return d.getBuilding();
		}
		return null;
	}

}
