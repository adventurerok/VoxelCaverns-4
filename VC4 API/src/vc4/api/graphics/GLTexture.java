/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLTexture {

	TEX_1D(3552),
	TEX_2D(3553),
	TEX_3D(32879),
	TEX_1D_ARRAY(35864),
	TEX_2D_ARRAY(35866);
	
	private int glnum;
	
	private GLTexture(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}

}
