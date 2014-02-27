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
		// if(d == Direction.SOUTH || d == Direction.WEST){
		// Vector3l ri = di.right;
		// di.right = di.left;
		// di.left = ri;
		// }
		return di;
	}

	public Door setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Door setNewRoomDir(Direction dir) {
		this.dir = dir;
		if (left.equals(right.move(width, dir.clockwise()))) {
			Vector3l ri = right;
			right = left;
			left = ri;
		}
		return this;
	}

	public Door move(int amount, Direction dir) {
		return new Door(left.move(amount, dir), right.move(amount, dir), dir);
	}

	public Door(Vector3l pos, Direction dir) {
		super();
		this.left = pos;
		this.right = pos;
		this.dir = dir;
		width = 1;
	}
}
