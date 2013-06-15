package vc4.vanilla.generation.village.wood;

import java.util.ArrayList;

import vc4.api.util.Adjustment;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.Building;
import vc4.vanilla.generation.village.Village;
import vc4.vanilla.generation.village.furnature.*;

public class WoodHouse implements Building {

	private static ArrayList<Furnature> furniture = new ArrayList<>();
	private static Village lastVille;
	
	public static void loadFurnature(Village ville){
		furniture.clear();
		furniture.add(new FurnatureWood(new Adjustment(6, 3, 0), Vanilla.table.uid));
		furniture.add(new FurnatureChair(new Adjustment(5, 3, 0), Vanilla.chair.uid, 2));
		furniture.add(new FurnatureBasic(new Adjustment(1, -2, 0), Vanilla.workbench.uid, 0));
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
						else if(y == start.y + 1){
							boolean nxWall = x == sx + 1 || x == ex - 1;
							boolean nzWall = z == sz + 1 || z == ez - 1;
							if(nxWall || nzWall) ville.setPlankBlock(x, y, z);
							else ville.setGlassBlock(x, y, z);
						} else ville.setPlankBlock(x, y, z);
					} else ville.setEmptyBlock(x, y, z);
				}
			}
		}
		for(Furnature f : furniture){
			f.place(ville, door.left, door.dir);
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
		npc.moveYaw = ville.getRand().nextInt(360) + ville.getRand().nextDouble();
		npc.setFirstName(ville.randomFirstName());
		npc.setLastName(ville.randomLastName());
		npc.setMan(ville.getRand().nextBoolean());
		npc.setSkinId((byte) ville.getRand().nextInt());
		npc.addToWorld();
	}

	@Override
	public void generateExtra(World world, Door door, Village ville, long y) {
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
	}
	

}
