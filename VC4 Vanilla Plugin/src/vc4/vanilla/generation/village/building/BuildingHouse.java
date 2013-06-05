package vc4.vanilla.generation.village.building;

import java.util.Random;

import vc4.api.client.Client;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.Village;
import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.style.VillageStyle;

public class BuildingHouse implements Building {

	@Override
	public void generate(World world, Door door, Village ville) {
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
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 3, ez - 1);
		if(!ville.addRoom(bb)) return;
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					boolean xWall = x == sx || x == ex;
					boolean zWall = z == sz || z == ez;
					if(y == start.y - 1 || y == start.y + 4){
						if(xWall || zWall) ville.setLogBlock(x, y, z);
						else ville.setPlankBlock(x, y, z);
					} else if(xWall || zWall){
						if(xWall && zWall) ville.setLogBlock(x, y, z);
						else ville.setPlankBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		ville.setEmptyBlock(door.left.x, door.left.y, door.left.z);
		ville.setEmptyBlock(door.left.x, door.left.y + 1, door.left.z);
		ville.setEmptyBlock(door.right.x, door.right.y, door.right.z);
		ville.setEmptyBlock(door.right.x, door.right.y + 1, door.right.z);
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
		if(y < 0 || y > 4) return;
		if(rand.nextInt(Client.debugMode() ? 50 : 500) != 0) return;
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
			Door door = Door.genDoor(new Vector3l(bx, ay, bz), Direction.getDirection(rand.nextInt(4)));
			room.generate(world, door, ville);
		}
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
