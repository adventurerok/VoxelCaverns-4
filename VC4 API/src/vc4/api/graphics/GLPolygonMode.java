/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLPolygonMode {

	FILL(6914), POINT(6912), LINE(6913);

	private int glnum;

	private GLPolygonMode(int glnum) {
		this.glnum = glnum;
	}

	public int getGlInt() {
		return glnum;
	}
}
