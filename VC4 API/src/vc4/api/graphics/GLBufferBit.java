/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 */
public enum GLBufferBit {

	COLOR(16384), DEPTH(256), STENCIL(1024), ACCUM(512);

	private int gli;

	GLBufferBit(int glint) {
		gli = glint;
	}

	public int getGlInt() {
		return gli;
	}
}
