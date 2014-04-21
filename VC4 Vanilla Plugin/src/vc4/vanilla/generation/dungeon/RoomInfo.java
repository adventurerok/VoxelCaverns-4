package vc4.vanilla.generation.dungeon;

import java.util.*;

public class RoomInfo {

	RoomBB interior;
	RoomBB exterior;
	Door[] doors;
	
	
	
	public boolean intercepts(RoomInfo other){
		if(other.exterior.intercepts(interior)) return true;
//		if(!other.exterior.intercepts(exterior)) return false;
//		LinkedList<Door> myDoors = new LinkedList<>();
//		for(int d = 0; d < doors.length; ++d){
//			if(doors[d].intercepts(other.exterior)) myDoors.add(doors[d]);
//		}
//		LinkedList<Door> theirDoors = new LinkedList<>();
//		for(int d = 0; d < other.doors.length; ++d){
//			if(other.doors[d].intercepts(exterior)) theirDoors.add(other.doors[d]);
//		}
//		if(myDoors.size() != theirDoors.size()) return true;
//		boolean fail = true;
//		while(myDoors.size() > 0){
//			fail = true;
//			for(int i = 0; i < theirDoors.size(); ++i){
//				if(myDoors.getFirst().fits(theirDoors.get(i))){
//					fail = false;
//					theirDoors.remove(i);
//					break;
//				}
//				
//			}
//			if(fail) return true;
//			myDoors.removeFirst();
//		}
		return false;
	}
	
	public RoomInfo(RoomBB interior, Collection<Door> doors){
		this(interior, interior.expand(1), doors);
	}
	
	public RoomInfo(RoomBB interior, RoomBB exterior, Collection<Door> doors){
		Door[] da = new Door[doors.size()];
		if(doors instanceof ArrayList){
			da = ((ArrayList<Door>)doors).toArray(da);
		} else {
			int i = 0;
			for(Door d : doors){
				da[i++] = d;
			}
		}
		this.interior = interior;
		this.exterior = exterior;
		this.doors = da;
	}


	public RoomInfo(RoomBB interior, Door...doors){
		this(interior, interior.expand(1), doors);
	}
	
	
	public RoomInfo(RoomBB interior, RoomBB exterior, Door...doors) {
		super();
		this.interior = interior;
		this.exterior = exterior;
		this.doors = doors;
	}
	
}
