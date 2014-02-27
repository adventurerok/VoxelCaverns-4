/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLTextureFilter {

	NEAREST(9728, 9984, 9986), LINEAR(9729, 9985, 9987);

	private int glnum;
	private int mipn;
	private int mipl;

	private GLTextureFilter(int glnum, int mipn, int mipl) {
		this.glnum = glnum;
		this.mipn = mipn;
		this.mipl = mipl;
	}

	public int getGlInt() {
		return glnum;
	}

	public int getGlMipmapInt(GLTextureFilter mipmap) {
		if (mipmap == NEAREST) return mipn;
		else if (mipmap == LINEAR) return mipl;
		else return glnum;
	}

}
