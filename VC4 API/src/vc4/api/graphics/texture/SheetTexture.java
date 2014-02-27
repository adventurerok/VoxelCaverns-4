/**
 * 
 */
package vc4.api.graphics.texture;

import vc4.api.graphics.GLTexture;

/**
 * @author paul
 * 
 */
public interface SheetTexture {

	public int getTexture(int frame);

	public int getTexture();

	public int getCurrentFrame();

	public int getWidth();

	public int getHeight();

	public int getFrameTime();

	public int getArraySize();

	public GLTexture getType();

	public int getNumberOfFrames();

	/**
	 * @param smooth
	 */
	void setSmooth(boolean smooth);

	/**
	 * @param mipmap
	 */
	void setMipmap(boolean mipmap);

	/**
	 * 
	 */
	public void bind();
}
