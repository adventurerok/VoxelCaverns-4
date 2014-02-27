/**
 * 
 */
package vc4.api.graphics;

import java.awt.Color;
import java.nio.*;
import java.util.EnumSet;

import vc4.api.vector.*;

/**
 * @author paul
 * 
 */
public interface OpenGL {

	public void polygonOffset(float factor, float units);

	public void polygonMode(GLFace face, GLPolygonMode mode);

	public void alphaFunc(GLCompareFunc func, float ref);

	public void begin(GLPrimative type);

	public void bindShader(int shader);

	public void bindShader(String name);

	public void bindTexture(GLTexture type, int texture);

	public void blendFunc(GLBlendFunc sfactor, GLBlendFunc dfactor);

	public void blendFuncSeparate(GLBlendFunc srcRGB, GLBlendFunc dstRGB, GLBlendFunc srcAlpha, GLBlendFunc dstAlpha);

	public void callList(int list);

	public void clear(EnumSet<GLBufferBit> bufferBits);

	public void clearColor(float r, float g, float b, float a);

	public void color(byte r, byte g, byte b);

	public void color(byte r, byte g, byte b, byte a);

	public void color(Color color);

	public void color(double r, double g, double b);

	public void color(double r, double g, double b, double a);

	public void color(float r, float g, float b);

	public void color(float r, float g, float b, float a);

	public void color(Vector3d color);

	public void color(Vector3f color);

	public void color(Vector3i color);

	public void color(Vector4d color);

	public void color(Vector4f color);

	public void color(Vector4i color);

	public void cullFace(GLFace face);

	public void deleteLists(int list, int range);

	public void depthFunc(GLCompareFunc func);

	public void depthMask(boolean mask);

	public void disable(GLFlag flag);

	public void disableVertexAttribArray(int index);

	public void drawArrays(GLPrimative type, int first, int count);

	public void enable(GLFlag flag);

	public void enableVertexAttribArray(int index);

	public void end();

	public void endList();

	public void generateMipmap(GLTexture target);

	public int genLists(int range);

	public int genTextures();

	public void initNames();

	public void lineWidth(float width);

	public void loadIdentity();

	public void lookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz);

	public void matrixMode(GLMatrixMode mode);

	public void newList(int list, GLCompileFunc mode);

	public void normal(double nx, double ny, double nz);

	public void normal(float nx, float ny, float nz);

	public void normal(int nx, int ny, int nz);

	public void normal(Vector3d normal);

	public void normal(Vector3f normal);

	public void normal(Vector3i normal);

	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar);

	public void perspective(float fovy, float aspect, float zNear, float zFar);

	public void pickMatrix(float x, float y, float width, float height, int[] viewport);

	public void popMatrix();

	public void pushMatrix();

	public void pushName(int name);

	public void popName();

	public void loadName(int name);

	public int renderMode(GLRenderMode mode);

	public void rotate(double angle, double x, double y, double z);

	public void rotate(double angle, Vector3d ang);

	public void rotate(double angle, Vector3f ang);

	public void rotate(double angle, Vector3i ang);

	public void rotate(float angle, float x, float y, float z);

	public void rotate(float angle, Vector3d ang);

	public void rotate(float angle, Vector3f ang);

	public void rotate(float angle, Vector3i ang);

	public void scale(double x, double y);

	public void scale(double x, double y, double z);

	public void scale(float x, float y);

	public void scale(float x, float y, float z);

	public void scale(Vector2d scale);

	public void scale(Vector2f scale);

	public void scale(Vector2i scale);

	public void scale(Vector3d scale);

	public void scale(Vector3f scale);

	public void scale(Vector3i scale);

	public void scissor(int x, int y, int width, int height);

	public void selectBuffer(IntBuffer buffer);

	// public void setupProjection(Projection mode);
	public void shaderUniform1f(String var, float x);

	public void shaderUniform1i(String var, int x);

	public void shaderUniform2f(String var, float x, float y);

	public void shaderUniform2f(String var, Vector2f vec);

	public void shaderUniform2i(String var, int x, int y);

	public void shaderUniform2i(String var, Vector2i vec);

	public void shaderUniform3f(String var, float x, float y, float z);

	public void shaderUniform3f(String var, Vector3f vec);

	public void shaderUniform3i(String var, int x, int y, int z);

	public void shaderUniform3i(String var, Vector3i vec);

	public void shaderUniform4f(String var, float x, float y, float z, float w);

	public void shaderUniform4f(String var, Vector4f vec);

	public void shaderUniform4i(String var, int x, int y, int z, int w);

	public void shaderUniform4i(String var, Vector4i vec);

	public void texCoord(double s);

	public void texCoord(double s, double t);

	public void texCoord(double s, double t, double r);

	public void texCoord(double s, double t, double r, double q);

	public void texCoord(float s);

	public void texCoord(float s, float t);

	public void texCoord(float s, float t, float r);

	public void texCoord(float s, float t, float r, float q);

	public void texCoord(Vector2d tex);

	public void texCoord(Vector2f tex);

	public void texCoord(Vector3d tex);

	public void texCoord(Vector3f tex);

	public void texCoord(Vector4d tex);

	public void texCoord(Vector4f tex);

	public void texImage2D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, boolean border, GLFormat format, GLType type, ByteBuffer data);

	public void texImage3D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, int depth, boolean border, GLFormat format, GLType type, ByteBuffer data);

	public void texParameterGenerateMipmap(GLTexture target, boolean mipmap);

	public void texParameterMagFilter(GLTexture target, GLTextureFilter filter);

	public void texParameterMinFilter(GLTexture target, GLTextureFilter filter, GLTextureFilter mipmap);

	public void texParameterWrapR(GLTexture target, GLTexWrap wrap);

	public void texParameterWrapS(GLTexture target, GLTexWrap wrap);

	public void texParameterWrapST(GLTexture target, GLTexWrap wrap);

	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrap);

	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrapst, GLTexWrap wrapr);

	public void texParameterWrapT(GLTexture target, GLTexWrap wrap);

	public void texSubImage3D(GLTexture target, int level, int xOffset, int yOffset, int zOffset, int width, int height, int depth, GLFormat format, GLType type, ByteBuffer data);

	public void translate(double x, double y);

	public void translate(double x, double y, double z);

	public void translate(float x, float y);

	public void translate(float x, float y, float z);

	public void translate(Vector2d trans);

	public void translate(Vector2f trans);

	public void translate(Vector2i trans);

	public void translate(Vector3d trans);

	public void translate(Vector3f trans);

	public void translate(Vector3i trans);

	public void unbindShader();

	public void vertex(double x, double y);

	public void vertex(double x, double y, double z);

	public void vertex(float x, float y);

	public void vertex(float x, float y, float z);

	public void vertex(int x, int y);

	public void vertex(int x, int y, int z);

	public void vertex(Vector2f vertex);

	public void vertex(Vector3f vertex);

	public void vertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer);

	public void vertexWithTexture(float x, float y, float z, float r, float s, float t);

	public void viewport(int x, int y, int width, int height);

	public int[] getViewport();

}
