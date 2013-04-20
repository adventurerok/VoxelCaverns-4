/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 *
 */
public interface Vector<V> {
	
	public V add(V vec);
	public V multiply(V vec);
	public V subtract(V vec);
	public V divide(V vec);
	public V abs();
	public V negate();
	public V clone();
	
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object o);
	
	public double distanceSquared(V vec);
	public double distance(V vec);

}
