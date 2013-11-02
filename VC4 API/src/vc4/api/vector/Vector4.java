/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 *
 */
public interface Vector4<V> extends Vector<V> {

	
	public Vector4f toVector4f();
	public Vector4l toVector4l();
	public Vector4i toVector4i();
	
}
