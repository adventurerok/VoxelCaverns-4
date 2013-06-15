package vc4.vanilla.generation.village;

import java.util.*;

import vc4.api.math.MathUtils;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.area.AreaVillage;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.village.style.VillageStyle;
import vc4.vanilla.npc.VillageNames;

public class VillageGenerator {

	
	public void generateExtra(World world, long x, long y, long z){
		Random check = world.createRandom(x >> 4, z >> 4, 119821868L);
		int px = check.nextInt(16);
		if(px != (x & 0xf)) return;
		int pz = check.nextInt(16);
		if(pz != (z & 0xf)) return;
		if(y > 4) return;
		int h = world.getMapData(x, z).getHeight(16, 16);
		Random rand = world.createRandom(x, h >> 5, z, 1263763L);
		long ay = (h & ~31) + 16; 
		x <<= 5;
		z <<= 5;
		x += 16;
		z += 16;
		Village ville = new Village(world, x - 40, ay - 30, z - 40, x + 40, ay + 30, z + 40, rand);
		if(ville.getStyle() == null) return;
		ArrayList<BuildingInfo> roomData = getBuildings(world, ville, rand, x, z);
		for(BuildingInfo dat : roomData){
			Building build = nextRoom(ville.getStyle(), dat.getType());
			Direction dir =  Direction.getDirection(dat.getDir());
			Door door = Door.genDoor(new Vector3l(dat.getPos().x, dat.getPos().y, dat.getPos().z), dir.clockwise());
			door.setNewRoomDir(dir);
			build.generateExtra(world, door, ville, y);
		}
	}
	
	public void generate(World world, long x, long y, long z){
		Random check = world.createRandom(x >> 4, z >> 4, 119821868L);
		int px = check.nextInt(16);
		if(px != (x & 0xf)) return;
		int pz = check.nextInt(16);
		if(pz != (z & 0xf)) return;
		if(y < 0 || y > 4) return;
		int h = world.getMapData(x, z).getHeight(16, 16);
		if(h >> 5 != y) return;
		Random rand = world.createRandom(x, y, z, 1263763L);
		x <<= 5;
		y <<= 5;
		z <<= 5;
		x += 16;
		y += 16;
		z += 16;
		Village ville = new Village(world, x - 40, y - 30, z - 40, x + 40, y + 30, z + 40, rand);
		if(ville.getStyle() == null) return;
		AreaVillage area = new AreaVillage(world);
		area.setBounds(AABB.getBoundingBox(x - 40, x + 40, y - 30, y + 30, z - 40, z + 40));
		area.setVillageName((VillageNames.getRandomFirst(rand) + " " + VillageNames.getRandomLast(rand)).trim());
		area.addToWorld();
		ArrayList<BuildingInfo> roomData = getBuildings(world, ville, rand, x, z);
		ArrayList<Path> paths = getPaths(roomData);
		for(Path p : paths){
			p.generate(world);
		}
		for(BuildingInfo dat : roomData){
			Building build = nextRoom(ville.getStyle(), dat.getType());
			Direction dir =  Direction.getDirection(dat.getDir());
			Door door = Door.genDoor(new Vector3l(dat.getPos().x, dat.getPos().y, dat.getPos().z), dir.clockwise());
			door.setNewRoomDir(dir);
			build.generate(world, door, ville);
		}
	}
	
	public static Building nextRoom(VillageStyle style, int num){
		for(WeightedBuilding d : style.getBuildings()){
			num -= d.getWeight();
			if(num <= 0) return d.getBuilding();
		}
		return null;
	}
	
	public static Path genPath(Village ville, Vector2l start){
		int dist = 15 + ville.getRand().nextInt(16);
		float ang = (float) (ville.getRand().nextDouble() * 2 * Math.PI);
		int xd = MathUtils.floor(MathUtils.sin(ang) * dist);
		int zd = MathUtils.floor(MathUtils.cos(ang) * dist);
		return new Path(start, new Vector2l(start.x + xd, start.y + zd));
	}
	
	public ArrayList<BuildingInfo> getBuildings(World world, Village ville, Random rand, long x, long z){
		Vector2l centre = new Vector2l(x, z);
		ArrayList<BuildingInfo> roomData = new ArrayList<>();
		int rooms = 8 + rand.nextInt(15);
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
			long fy = MathUtils.floor(((ay + ly + hy) / 3d) + 1);
			if(fy < 0) continue;
			int type = rand.nextInt(ville.getStyle().getTotalWeight());
			int dir = rand.nextInt(4);
			Vector2l buildPos = new Vector2l(bx, bz);
			Vector2l frontPos = new Vector2l(bx + Direction.getDirection(dir).getX(), bz + Direction.getDirection(dir).getZ());
			if(frontPos.distanceSquared(centre) - buildPos.distanceSquared(centre) > 48) dir = Direction.getOpposite(dir).getId();
			BuildingInfo info = new BuildingInfo(new Vector3l(bx, fy, bz), type, dir);
			roomData.add(info);
			
		}
		return roomData;
	}
	
	public ArrayList<Path> getPaths(ArrayList<BuildingInfo> roomData){
		ArrayList<Path> paths = new ArrayList<>();
		double cd;
		double nd;
		Vector3l c;
		Vector3l n;
		for(int d = 0; d < roomData.size(); ++d){
			cd = 999999999999d;
			nd = 999999999999d;
			c = null;
			n = null;
			Vector3l dInf = roomData.get(d).backPos();
			for(int i = d + 1; i < roomData.size(); ++i){
				Vector3l iInf = roomData.get(i).backPos();
				double dist = dInf.distanceSquared(iInf);
				if(dist < cd){
					nd = cd;
					n = c;
					cd = dist;
					c = iInf;
				} else if(dist < nd){
					nd = dist;
					n = iInf;
				}
			}
			if(c != null){
				paths.add(new Path(new Vector2l(dInf.x, dInf.z), new Vector2l(c.x, c.z)));
				if(n != null) paths.add(new Path(new Vector2l(dInf.x, dInf.z), new Vector2l(n.x, n.z)));
			}
		}
		return paths;
	}
	
	public static Collection<Path> genPaths(Village ville, Vector2l centre){
		ArrayList<Path> res = new ArrayList<>();
		res.add(genPath(ville, centre));
		res.add(genPath(ville, centre));
		int other = 1 + ville.getRand().nextInt(4);
		for(int d = 0; d < other; ++d){
			Vector2l base = res.get(res.size() - 1 - ville.getRand().nextInt(2)).getEnd();
			Path late = genPath(ville, base);
			if(late.getEnd().x > centre.x + 40 || late.getEnd().x < centre.x - 40 || late.getEnd().y > centre.y + 40 || late.getEnd().y < centre.y - 40){
				if(ville.getRand().nextInt(3) == 0) other++;
				continue;
			}
			res.add(late);
		}
		return res;
	}
}
