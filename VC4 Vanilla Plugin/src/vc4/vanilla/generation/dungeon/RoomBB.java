/**
 * 
 */
package vc4.vanilla.generation.dungeon;

/**
 * @author paul
 *
 */
public class RoomBB {

	long minX, minY, minZ, maxX, maxY, maxZ;

	public RoomBB(long minX, long minY, long minZ, long maxX, long maxY, long maxZ) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public RoomBB expand(long amt){
		long mix = minX - amt;
		long miy = minY - amt;
		long miz = minZ - amt;
		long max = maxX + amt;
		long may = maxY + amt;
		long maz = maxZ + amt;
		return new RoomBB(mix, miy, miz, max, may, maz);
	}
	
	public boolean intercepts(RoomBB aabb){
		if(aabb == null) return false;
		if (aabb.maxX <= minX || aabb.minX >= maxX) {
			return false;
		}

		if (aabb.maxY <= minY || aabb.minY >= maxY) {
			return false;
		}

		return aabb.maxZ > minZ && aabb.minZ < maxZ;
	}
	
	
}
