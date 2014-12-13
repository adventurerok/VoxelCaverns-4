/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLPrimitive {

	POINTS(0), LINES(1), LINE_STRIP(3), LINE_LOOP(2), TRIANGLES(4), TRIANGLE_STRIP(5), TRIANGLE_FAN(6), QUADS(7), QUAD_STRIP(8), POLYGON(9);

	private int glnum;

	private GLPrimitive(int glnum) {
		this.glnum = glnum;
	}

	public int getGlInt() {
		return glnum;
	}
}
