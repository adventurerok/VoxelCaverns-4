/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 *
 */
public interface Vector3<V> extends Vector<V> {

	public Vector3f toVector3f();
	public Vector3d toVector3d();
	public Vector3l toVector3l();
	public Vector3i toVector3i();
}
