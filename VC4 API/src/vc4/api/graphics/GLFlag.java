/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 *
 */
public enum GLFlag {

	POLYGON_OFFSET_FILL(32823),
	POLYGON_OFFSET_LINE(10754),
	POLYGON_OFFSET_POINT(10753),
	ALPHA_TEST(3008),
	BLEND(3042),
	COLOR_SUM(33880),
	CULL_FACE(2884),
	DEPTH_TEST(2929),
	FOG(2912),
	LIGHTING(2896),
	TEXTURE_2D(3553),
	TEXTURE_3D(32879),
	SCISSOR_TEST(3089);
	
	private int glnum;
	
	private GLFlag(int glnum) {
		this.glnum = glnum;
	}
	
	public int getGlInt(){
		return glnum;
	}
}
