/**
 * 
 */
package vc4.api.world;

import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

/**
 * @author paul
 *
 */
public class ChunkPos {

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		result = prime * result + (int) (z ^ (z >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ChunkPos other = (ChunkPos) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		if (z != other.z) return false;
		return true;
	}

	public long x, y, z;

	private ChunkPos(long x, long y, long z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static ChunkPos create(long x, long y, long z){
		return new ChunkPos(x, y, z);
	}
	
	public static ChunkPos createFromWorldPos(long x, long y, long z){
		return new ChunkPos(x >> 5, y >> 5, z >> 5);
	}
	
	public static ChunkPos createFromWorldPos(Vector3l pos){
		return new ChunkPos(pos.x >> 5, pos.y >> 5, pos.z >> 5);
	}
	
	public long worldX(int cx){
		return (x << 5) + cx;
	}
	
	public long worldY(int cy){
		return (y << 5) + cy;
	}
	
	public long worldZ(int cz){
		return (z << 5) + cz;
	}
	
	public Vector3l worldPos(int cx, int cy, int cz){
		return new Vector3l((x << 5) + cx, (y << 5) + cy, (z << 5) + cz);
	}

	/**
	 * @param loc
	 */
	public static ChunkPos createFromWorldVector(Vector3d loc) {
		return createFromWorldPos((long)loc.x, (long)loc.y, (long)loc.z);
	}

	/**
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	public ChunkPos add(int x, int y, int z) {
		return new ChunkPos(this.x + x, this.y + y, this.z + z);
	}
	
	public ChunkPos add(ChunkPos other){
		return new ChunkPos(other.x + x, other.y + y, other.z + z);
	}
	
	public ChunkPos subtract(ChunkPos other){
		return new ChunkPos(x - other.x, y - other.y, z - other.z);
	}
	
	public double distanceSquared(Vector3d loc){
		Vector3d me = new Vector3d(worldX(16), worldY(16), worldZ(16));
		return me.distanceSquared(loc);
	}

	public ChunkPos getCoordOfNearbyChunk(int cx, int cy, int cz){
		return createFromWorldPos((x << 5) + cx, (y << 5) + cy, (z << 5) + cz);
	}
	
	
	
	

}
