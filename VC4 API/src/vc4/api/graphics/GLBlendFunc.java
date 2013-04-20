/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLBlendFunc {

	ZERO(0),
	ONE(1),
	SRC_COLOR(768),
	ONE_MINUS_SRC_COLOR(769),
	DST_COLOR(774),
	ONE_MINUS_DST_COLOR(775),
	SRC_ALPHA(770),
	ONE_MINUS_SRC_ALPHA(771),
	DST_ALPHA(772),
	ONE_MINUS_DST_ALPHA(773),
	CONSTANT_COLOR(32769),
	ONE_MINUS_CONSTANT_COLOR(32770),
	CONSTANT_ALPHA(32771),
	ONE_MINUS_CONSTANT_ALPHA(32772),
	SRC_ALPHA_SATURATE(776),
	SRC1_COLOR(35065),
	ONE_MINUS_SRC1_COLOR(35066),
	SRC1_ALPHA(34185),
	ONE_MINUS_SRC1_ALPHA(35067);
	
	private int glnum;
	
	private GLBlendFunc(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
