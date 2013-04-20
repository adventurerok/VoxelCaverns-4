/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLInternalFormat {

	RGB4(32847),
	RGB8(32849),
	RGB16(32852),
	RGBA4(32854),
	RGBA8(32856),
	RGBA16(32859),
	COMPRESSED_RGB(34029),
	COMPRESSED_RGBA(34030);
	
	private int glnum;
	
	private GLInternalFormat(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
