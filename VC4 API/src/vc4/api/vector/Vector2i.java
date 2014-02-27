/**
 * 
 */
package vc4.api.vector;

/**
 * @author paul
 * 
 */
public class Vector2i implements Vector2<Vector2i> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2i other = (Vector2i) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}

	public int x, y;

	public Vector2i(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#add(java.lang.Object)
	 */
	@Override
	public Vector2i add(Vector2i vec) {
		return new Vector2i(x + vec.x, y + vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#multiply(java.lang.Object)
	 */
	@Override
	public Vector2i multiply(Vector2i vec) {
		return new Vector2i(x * vec.x, y * vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#subtract(java.lang.Object)
	 */
	@Override
	public Vector2i subtract(Vector2i vec) {
		return new Vector2i(x - vec.x, y - vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#divide(java.lang.Object)
	 */
	@Override
	public Vector2i divide(Vector2i vec) {
		return new Vector2i(x / vec.x, y / vec.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#abs()
	 */
	@Override
	public Vector2i abs() {
		return new Vector2i(Math.abs(x), Math.abs(y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#negate()
	 */
	@Override
	public Vector2i negate() {
		return new Vector2i(-x, -y);
	}

	@Override
	public Vector2i clone() {
		return new Vector2i(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distanceSquared(java.lang.Object)
	 */
	@Override
	public double distanceSquared(Vector2i vec) {
		int nx = vec.x - x;
		int ny = vec.y - y;
		return nx * nx + ny * ny;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector#distance(java.lang.Object)
	 */
	@Override
	public double distance(Vector2i vec) {
		return Math.sqrt(distanceSquared(vec));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2f()
	 */
	@Override
	public Vector2f toVector2f() {
		return new Vector2f(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2i()
	 */
	@Override
	public Vector2i toVector2i() {
		return clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2l()
	 */
	@Override
	public Vector2l toVector2l() {
		return new Vector2l(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.vector.Vector2#toVector2d()
	 */
	@Override
	public Vector2d toVector2d() {
		return new Vector2d(x, y);
	}

}
