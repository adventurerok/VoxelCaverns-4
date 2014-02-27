/**
 * 
 */
package vc4.api.util;

/**
 * @author paul
 * 
 */
public enum Direction {

	NORTH(0, 2, 1, 0, 0), EAST(1, 3, 0, 0, 1), SOUTH(2, 0, -1, 0, 0), WEST(3, 1, 0, 0, -1), UP(4, 5, 0, 1, 0), DOWN(5, 4, 0, -1, 0), NORTHEAST(6, 8, 1, 0, 1), // optional
	SOUTHEAST(7, 9, -1, 0, 1), // optional
	SOUTHWEST(8, 6, -1, 0, -1), // optional
	NORTHWEST(9, 7, 1, 0, -1), // optional
	CUSTOM0(10, -1, 0, 0, 0),
	CUSTOM1(11, -1, 0, 0, 0),
	CUSTOM2(12, -1, 0, 0, 0),
	CUSTOM3(13, -1, 0, 0, 0),
	CUSTOM4(14, -1, 0, 0, 0),
	CUSTOM5(15, -1, 0, 0, 0),
	CUSTOM6(16, -1, 0, 0, 0),
	CUSTOM7(17, -1, 0, 0, 0),
	CUSTOM8(18, -1, 0, 0, 0),
	CUSTOM9(19, -1, 0, 0, 0);

	private static Direction[] dirs;
	private int id, opposite, x, y, z;

	static {
		dirs = new Direction[20];
		dirs[0] = NORTH;
		dirs[1] = EAST;
		dirs[2] = SOUTH;
		dirs[3] = WEST;
		dirs[4] = UP;
		dirs[5] = DOWN;
		dirs[6] = NORTHEAST;
		dirs[7] = SOUTHEAST;
		dirs[8] = SOUTHWEST;
		dirs[9] = NORTHWEST;
		dirs[10] = CUSTOM0;
		dirs[11] = CUSTOM1;
		dirs[12] = CUSTOM2;
		dirs[13] = CUSTOM3;
		dirs[14] = CUSTOM4;
		dirs[15] = CUSTOM5;
		dirs[16] = CUSTOM6;
		dirs[17] = CUSTOM7;
		dirs[18] = CUSTOM8;
		dirs[19] = CUSTOM9;
	}

	public static Direction getDirection(int id) {
		return dirs[id];
	}

	private Direction(int id, int opposite, int x, int y, int z) {
		this.id = id;
		this.opposite = opposite;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Direction counterClockwise() {
		return getDirection((id - 1) & 3);
	}

	public Direction clockwise() {
		return getDirection((id + 1) & 3);
	}

	public boolean isNorthSouth() {
		return this.id == 0 || this.id == 2;
	}

	public boolean isEastWest() {
		return this.id == 1 || this.id == 3;
	}

	public boolean isUpDown() {
		return this.id == 4 || this.id == 5;
	}

	/**
	 * @return the id
	 */
	public int id() {
		return id;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	public Direction opposite() {
		if (opposite == -1) return null;
		return dirs[opposite];
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	public void setCustom(int x, int y, int z) {
		if (id < 10) return;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void clearCustom() {
		if (id < 10) return;
		x = y = z = 0;
	}

	public void addDirectionToCustom(Direction dir) {
		if (id < 10) return;
		x += dir.x;
		y += dir.y;
		z += dir.z;
	}

	/**
	 * @param side
	 */
	public static Direction getDiagonal(int side) {
		return getDirection(side + 6);
	}

	public static Direction getOpposite(int dir) {
		return getDirection(dir).opposite();
	}

}
