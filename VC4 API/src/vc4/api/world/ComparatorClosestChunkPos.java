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

	Vector3d[] loc;
	
	public ComparatorClosestChunkPos(Vector3d...loc) {
		super();
		this.loc = loc;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ChunkPos o1, ChunkPos o2) {
		double d1 = 10000000000000000000d;
		double d2 = 10000000000000000000d;
		double t = 0;
		for(int li = 0; li < loc.length; ++li) {
			t = o1.distanceSquared(loc[li]);
			if(d1 > t) d1 = t;
			t = o2.distanceSquared(loc[li]);
			if(d2 > t) d2 = t;
		}
		return (int) Math.signum(d1 - d2);
	}

}
