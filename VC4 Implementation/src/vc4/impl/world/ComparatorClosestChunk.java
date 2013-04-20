/**
 * 
 */
package vc4.impl.world;

import java.util.Comparator;

import vc4.api.vector.Vector3d;

/**
 * @author paul
 *
 */
public class ComparatorClosestChunk implements Comparator<ImplChunk> {

	Vector3d loc;
	
	public ComparatorClosestChunk(Vector3d loc) {
		super();
		this.loc = loc;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ImplChunk o1, ImplChunk o2) {
		return (int) (o1.distanceSquared(loc) - o2.distanceSquared(loc));
	}

}
