/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLTexWrap {

	CLAMP_TO_EDGE(33071),
	CLAMP_TO_BORDER(33069),
	MIRRORED_REPEAT(33648),
	REPEAT(10497);
	
	private int glnum;
	
	private GLTexWrap(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
