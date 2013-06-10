package vc4.vanilla.generation.village;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;

public class BuildingInfo {

	Vector3l pos;
	int type;
	int dir;
	
	public BuildingInfo(Vector3l pos, int type, int dir) {
		super();
		this.pos = pos;
		this.type = type;
		this.dir = dir;
	}
	
	public Vector3l backPos(){
		return pos.move(1, Direction.getOpposite(dir));
	}

	public Vector3l getPos() {
		return pos;
	}

	public int getType() {
		return type;
	}

	public int getDir() {
		return dir;
	}
	
	
}
