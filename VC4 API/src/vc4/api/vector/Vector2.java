/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 * 
 */
public interface Vector2<V> extends Vector<V> {

	public Vector2f toVector2f();

	public Vector2i toVector2i();

	public Vector2l toVector2l();

	public Vector2d toVector2d();

}
