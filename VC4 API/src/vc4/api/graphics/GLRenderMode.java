/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLRenderMode {

	RENDER(7168), SELECT(7170), FEEDBACK(7169);

	private int glnum;

	private GLRenderMode(int glnum) {
		this.glnum = glnum;
	}

	public int getGlInt() {
		return glnum;
	}
}
