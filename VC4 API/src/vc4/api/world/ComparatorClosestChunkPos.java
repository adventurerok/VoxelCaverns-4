/**
 * 
 */
package vc4.api.world;

import java.util.Comparator;

import vc4.api.vector.Vector3d;

/**
 * @author paul
 *
 */
public class ComparatorClosestChunkPos implements Comparator<ChunkPos> {

	Vector3d loc;
	
	public ComparatorClosestChunkPos(Vector3d loc) {
		super();
		this.loc = loc;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ChunkPos o1, ChunkPos o2) {
		double d1 = o1.distanceSquared(loc);
		double d2 = o2.distanceSquared(loc);
		return (int) Math.signum(d1 - d2);
	}

}
