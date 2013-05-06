/**
 * 
 */
package vc4.api.graphics;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.EnumSet;

import vc4.api.vector.*;

/**
 * @author paul
 *
 */
public interface OpenGL {

	
	public void clearColor(float r, float g, float b, float a);
	public void clear(EnumSet<GLBufferBit> bufferBits);
	public void matrixMode(GLMatrixMode mode);
	public void loadIdentity();
	public void viewport(int x, int y, int width, int height);
	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar);
	public void perspective(float fovy, float aspect, float zNear, float zFar);
	public void lookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz);
	public void enable(GLFlag flag);
	public void disable(GLFlag flag);
	public void depthFunc(GLCompareFunc func);
	public void alphaFunc(GLCompareFunc func, float ref);
	public void blendFunc(GLBlendFunc sfactor, GLBlendFunc dfactor);
	public void cullFace(GLFace face);
	public void begin(GLPrimative type);
	public void end();
	public void vertex(int x, int y, int z);
	public void vertex(float x, float y, float z);
	public void vertex(int x, int y);
	public void vertex(float x, float y);
	public void vertex(Vector2f vertex);
	public void vertex(Vector3f vertex);
	public void color(byte r, byte g, byte b);
	public void color(float r, float g, float b);
	public void color(byte r, byte g, byte b, byte a);
	public void color(float r, float g, float b, float a);
	public void color(Vector3f color);
	public void color(Vector4f color);
	public void texCoord(float s);
	public void texCoord(float s, float t);
	public void texCoord(float s, float t, float r);
	public void texCoord(float s, float t, float r, float q);
	public void texCoord(Vector2f tex);
	public void texCoord(Vector3f tex);
	public void texCoord(Vector4f tex);
	public void normal(int nx, int ny, int nz);
	public void normal(float nx, float ny, float nz);
	public int genTextures();
	public void bindTexture(GLTexture type, int texture);
	public void texImage2D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, boolean border, GLFormat format, GLType type, ByteBuffer data);
	public void texImage3D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, int depth, boolean border, GLFormat format, GLType type, ByteBuffer data);
	public void texSubImage3D(GLTexture target, int level, int xOffset, int yOffset,  int zOffset, int width, int height, int depth, GLFormat format, GLType type, ByteBuffer data);
	public void texParameterMagFilter(GLTexture target, GLTextureFilter filter);
	public void texParameterMinFilter(GLTexture target, GLTextureFilter filter, GLTextureFilter mipmap);
	public void texParameterWrapS(GLTexture target, GLTexWrap wrap);
	public void texParameterWrapT(GLTexture target, GLTexWrap wrap);
	public void texParameterWrapR(GLTexture target, GLTexWrap wrap);
	public void texParameterWrapST(GLTexture target, GLTexWrap wrap);
	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrap);
	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrapst, GLTexWrap wrapr);
	public void texParameterGenerateMipmap(GLTexture target, boolean mipmap);
	public void rotate(float angle, float x, float y, float z);
	public void translate(float x, float y, float z);
	public void translate(double x, double y, double z);
	public void scale(float x, float y, float z);
	public void callList(int list);
	public int genLists(int range);
	public void endList();
	public void newList(int list, GLCompileFunc mode);
	public void drawArrays(GLPrimative type, int first, int count);
	public void vertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer);
	public void enableVertexArrribArray(int index);
	public void disableVertexArrribArray(int index);
	public void deleteLists(int list, int range);
	public void generateMipmap(GLTexture target);
	public void lineWidth(float width);
	public void depthMask(boolean mask);
	public void blendFuncSeparate(GLBlendFunc srcRGB, GLBlendFunc dstRGB, GLBlendFunc srcAlpha, GLBlendFunc dstAlpha);
	/**
	 * 
	 */
	public void pushMatrix();
	public void popMatrix();
	
	public void vertexWithTexture(float x, float y, float z, float r, float s, float t);
	/**
	 * @param color
	 */
	public void color(Color color);
	public void texCoord(double s, double t);
	public void vertex(double x, double y, double z);
	public void texCoord(double s, double t, double r);
	

}
