/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLCompileFunc {

	COMPILE(4864), COMPILE_AND_EXECUTE(4865);

	private int glnum;

	private GLCompileFunc(int glnum) {
		this.glnum = glnum;
	}

	public int getGlInt() {
		return glnum;
	}
}
