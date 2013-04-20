/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLType {

	BYTE(5120),
	SHORT(5122),
	UNSIGNED_BYTE(5121),
	UNSIGNED_SHORT(5123);
	
	private int glnum;
	
	private GLType(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
