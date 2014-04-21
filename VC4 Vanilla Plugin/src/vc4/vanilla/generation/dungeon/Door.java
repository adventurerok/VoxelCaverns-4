/**
 * 
 */
package vc4.vanilla.generation.dungeon;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;

/**
 * @author paul
 * 
 */
public class Door {

	public Vector3l left, right;
	public int width = 2;
	public Direction dir;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Door clone() {
		return new Door(left, right, dir);
	}

	public Door(Vector3l left, Vector3l right, Direction dir) {
		super();
		this.left = left;
		this.right = right;
		this.dir = dir;
	}

	public Door(Vector3l left, Vector3l right) {
		super();
		this.left = left;
		this.right = right;
	}

	public static Door genDoor(Vector3l loc, Direction d) {
		Door di = new Door(loc, loc.add(new Vector3l(d.getX(), d.getY(), d.getZ())));
		Vector3l lToR = di.left.subtract(di.right);
		di.dir = Direction.getDirection(lToR).clockwise();
		return di;
	}

	public Door setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getWidth() {
		return width;
	}
	
	public boolean fits(Door other){
		if(other.dir == this.dir){
			if(!other.left.equals(this.left)) return false;
			return other.right.equals(this.right);
		} else if(other.dir == this.dir.opposite()){
			if(!other.left.equals(this.right)) return false;
			return other.right.equals(this.left);
		} else return false;
	}
	
	public RoomBB getBB(){
		return new RoomBB(Math.min(left.x, right.x), left.y, Math.min(left.z, right.z), Math.max(left.x, right.x), left.y + 1, Math.max(left.z, right.z));
	}
	
	public boolean intercepts(RoomBB room){
		RoomBB me = getBB();
		return room.intercepts(me);
		
	}

	public Door setNewRoomDir(Direction dir) {
		if (left.equals(right.move(width, dir.clockwise()))) {
			return new Door(right, left, dir);
		} else {
			return new Door(left,  right, dir);
		}
	}
	
	public Door flip(){
		return new Door(right, left, dir.opposite());
	}

	public Door move(int amount, Direction dir) {
		return new Door(left.move(amount, dir), right.move(amount, dir), this.dir);
	}

	public Door(Vector3l pos, Direction dir) {
		super();
		this.left = pos;
		this.right = pos;
		this.dir = dir;
		width = 1;
	}
}
