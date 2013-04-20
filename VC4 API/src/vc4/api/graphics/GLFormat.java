/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLFormat {

	RGB(6407),
	RGBA(6408);
	
	private int glnum;
	
	private GLFormat(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
	
}
