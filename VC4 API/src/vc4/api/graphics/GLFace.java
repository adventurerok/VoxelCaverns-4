/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLFace {

	FRONT(1028),
	BACK(1029),
	FRONT_AND_BACK(1032);
	
	private int glnum;
	
	private GLFace(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
