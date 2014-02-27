/**
 * 
 */
package vc4.api.graphics.texture;

import vc4.api.graphics.GLTexture;

/**
 * @author paul
 * 
 */
public interface AnimatedTexture {

	public int getTexture();

	public int getWidth();

	public int getHeight();

	public int getArraySize();

	public GLTexture getType();

	public int getArrayIndex(String tex);

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

	public abstract void updateAnimation(int tickTime);

	public abstract boolean hasArrayIndex(String tex);
}
