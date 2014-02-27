/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLCompareFunc {

	NEVER(0x200), LESS(513), EQUAL(514), LEQUAL(515), GREATER(516), GEQUAL(518), NOTEQUAL(517), ALWAYS(0x207);

	private int glnum;

	private GLCompareFunc(int glnum) {
		this.glnum = glnum;
	}

	public int getGlInt() {
		return glnum;
	}
}
