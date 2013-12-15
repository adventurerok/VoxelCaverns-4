/**
 * 
 */
package vc4.client.graphics;

import java.awt.Color;
import java.nio.*;
import java.util.EnumSet;

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import vc4.api.graphics.*;
import vc4.api.vector.*;

/**
 * @author paul
 * 
 */
public class ClientGL implements OpenGL {

	int[] viewport = new int[4];
	
	/**
	 * 
	 */
	public ClientGL() {
		Graphics.setImplementation(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#alphaFunc(vc4.api.graphics.CompareFunc, float)
	 */
	@Override
	public void alphaFunc(GLCompareFunc func, float ref) {
		GL11.glAlphaFunc(func.getGlInt(), ref);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#begin(vc4.api.graphics.GLPrimative)
	 */
	@Override
	public void begin(GLPrimative type) {
		GL11.glBegin(type.getGlInt());
	}

	@Override
	public void bindShader(int shader) {
		Graphics.getClientShaderManager().bindShader(shader);
	}

	@Override
	public void bindShader(String name) {
		Graphics.getClientShaderManager().bindShader(name);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#bindTexture(vc4.api.graphics.GLTexture, int)
	 */
	@Override
	public void bindTexture(GLTexture type, int texture) {
		GL11.glBindTexture(type.getGlInt(), texture);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#blendFunc(vc4.api.graphics.BlendFunc, vc4.api.graphics.BlendFunc)
	 */
	@Override
	public void blendFunc(GLBlendFunc sfactor, GLBlendFunc dfactor) {
		GL11.glBlendFunc(sfactor.getGlInt(), dfactor.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#blendFuncSeparate(vc4.api.graphics.GLBlendFunc, vc4.api.graphics.GLBlendFunc, vc4.api.graphics.GLBlendFunc, vc4.api.graphics.GLBlendFunc)
	 */
	@Override
	public void blendFuncSeparate(GLBlendFunc srcRGB, GLBlendFunc dstRGB, GLBlendFunc srcAlpha, GLBlendFunc dstAlpha) {
		GL14.glBlendFuncSeparate(srcRGB.getGlInt(), dstRGB.getGlInt(), srcAlpha.getGlInt(), dstAlpha.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#callList(int)
	 */
	@Override
	public void callList(int list) {
		GL11.glCallList(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#clear(java.util.EnumSet)
	 */
	@Override
	public void clear(EnumSet<GLBufferBit> bufferBits) {
		int clr = 0;
		for (GLBufferBit b : bufferBits) {
			clr |= b.getGlInt();
		}
		GL11.glClear(clr);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#clearColor(float, float, float, float)
	 */
	@Override
	public void clearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#color(byte, byte, byte)
	 */
	@Override
	public void color(byte r, byte g, byte b) {
		GL11.glColor3ub(r, g, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#color(byte, byte, byte, byte)
	 */
	@Override
	public void color(byte r, byte g, byte b, byte a) {
		GL11.glColor4ub(r, g, b, a);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#color(java.awt.Color)
	 */
	@Override
	public void color(Color color) {
		color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
	}

	@Override
	public void color(double r, double g, double b) {
		GL11.glColor3d(r, g, b);
	}

	@Override
	public void color(double r, double g, double b, double a) {
		GL11.glColor4d(r, g, b, a);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#color(float, float, float)
	 */
	@Override
	public void color(float r, float g, float b) {
		GL11.glColor3f(r, g, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#color(float, float, float, float)
	 */
	@Override
	public void color(float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
	}

	@Override
	public void color(Vector3d color) {
		GL11.glColor3d(color.x, color.y, color.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#color(vc4.api.vector.Vector3f)
	 */
	@Override
	public void color(Vector3f color) {
		GL11.glColor3f(color.x, color.y, color.z);
	}

	@Override
	public void color(Vector3i color) {
		GL11.glColor3f(color.x / 255f, color.y / 255f, color.z / 255f);
	}

	@Override
	public void color(Vector4d color) {
		GL11.glColor4d(color.x, color.y, color.z, color.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#color(vc4.api.vector.Vector4f)
	 */
	@Override
	public void color(Vector4f color) {
		GL11.glColor4f(color.x, color.y, color.z, color.w);
	}

	@Override
	public void color(Vector4i color) {
		GL11.glColor4f(color.x / 255f, color.y / 255f, color.z / 255f, color.w / 255f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#cullFace(vc4.api.graphics.Face)
	 */
	@Override
	public void cullFace(GLFace face) {
		GL11.glCullFace(face.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#deleteLists(int, int)
	 */
	@Override
	public void deleteLists(int list, int range) {
		GL11.glDeleteLists(list, range);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#depthFunc(vc4.api.graphics.CompareFunc)
	 */
	@Override
	public void depthFunc(GLCompareFunc func) {
		GL11.glDepthFunc(func.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#depthMask(boolean)
	 */
	@Override
	public void depthMask(boolean mask) {
		GL11.glDepthMask(mask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#disable(vc4.api.graphics.GLFlag)
	 */
	@Override
	public void disable(GLFlag flag) {
		GL11.glDisable(flag.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#disableVertexArrribArray(int)
	 */
	@Override
	public void disableVertexAttribArray(int index) {
		GL20.glDisableVertexAttribArray(index);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#drawArrays(vc4.api.graphics.GLPrimative, int, int)
	 */
	@Override
	public void drawArrays(GLPrimative type, int first, int count) {
		GL11.glDrawArrays(type.getGlInt(), first, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#enable(vc4.api.graphics.GLFlag)
	 */
	@Override
	public void enable(GLFlag flag) {
		GL11.glEnable(flag.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#enableVertexArrribArray(int)
	 */
	@Override
	public void enableVertexAttribArray(int index) {
		GL20.glEnableVertexAttribArray(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#end()
	 */
	@Override
	public void end() {
		GL11.glEnd();
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#endList()
	 */
	@Override
	public void endList() {
		GL11.glEndList();
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#generateMipmap(vc4.api.graphics.GLTexture)
	 */
	@Override
	public void generateMipmap(GLTexture target) {
		GL30.glGenerateMipmap(target.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#genLists(int)
	 */
	@Override
	public int genLists(int range) {
		return GL11.glGenLists(range);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#glGenTextures()
	 */
	@Override
	public int genTextures() {
		return GL11.glGenTextures();
	}

	@Override
	public int[] getViewport() {
		return viewport;
	}

	@Override
	public void initNames() {
		GL11.glInitNames();
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#lineWidth(float)
	 */
	@Override
	public void lineWidth(float width) {
		GL11.glLineWidth(width);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#loadIdentity()
	 */
	@Override
	public void loadIdentity() {
		GL11.glLoadIdentity();
	}

	@Override
	public void loadName(int name) {
		GL11.glLoadName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#lookAt(float, float, float, float, float, float, float, float, float)
	 */
	@Override
	public void lookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz) {
		GLU.gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#matrixMode(vc4.api.graphics.MatrixMode)
	 */
	@Override
	public void matrixMode(GLMatrixMode mode) {
		switch (mode) {
			case MODELVIEW:
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				break;
			case PROJECTION:
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				break;
			case TEXTURE:
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				break;
		}
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#newList(int, vc4.api.graphics.GLCompileFunc)
	 */
	@Override
	public void newList(int list, GLCompileFunc mode) {
		GL11.glNewList(list, mode.getGlInt());
	}

	@Override
	public void normal(double nx, double ny, double nz) {
		GL11.glNormal3d(nx, ny, nz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#normal(float, float, float)
	 */
	@Override
	public void normal(float nx, float ny, float nz) {
		GL11.glNormal3f(nx, ny, nz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#normal(int, int, int)
	 */
	@Override
	public void normal(int nx, int ny, int nz) {
		GL11.glNormal3f(nx, ny, nz);
	}

	@Override
	public void normal(Vector3d normal) {
		GL11.glNormal3d(normal.x, normal.y, normal.z);
	}

	@Override
	public void normal(Vector3f normal) {
		GL11.glNormal3f(normal.x, normal.y, normal.z);
	}

	@Override
	public void normal(Vector3i normal) {
		GL11.glNormal3i(normal.x, normal.y, normal.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#ortho(float, float, float, float, float, float)
	 */
	@Override
	public void ortho(float left, float right, float bottom, float top, float near, float far) {
		GL11.glOrtho(left, right, bottom, top, near, far);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#perspective(float, float, float, float)
	 */
	@Override
	public void perspective(float fovy, float aspect, float zNear, float zFar) {
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
	}

	@Override
	public void pickMatrix(float x, float y, float width, float height, int[] viewport) {
		if (width <= 0.0F || height <= 0.0F) return;
		translate((viewport[2] - (2.0F * (x - viewport[0]))) / width, (viewport[3] - (2.0F * (y - viewport[1]))) / height, 0.0F);
		scale(viewport[2] / width, viewport[3] / height, 1.0F);
	}

	@Override
	public void polygonMode(GLFace face, GLPolygonMode mode) {
		GL11.glPolygonMode(face.getGlInt(), mode.getGlInt());
	}

	@Override
	public void polygonOffset(float factor, float units) {
		GL11.glPolygonOffset(factor, units);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#popMatrix()
	 */
	@Override
	public void popMatrix() {
		GL11.glPopMatrix();
	}

	@Override
	public void popName() {
		GL11.glPopName();
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#pushMatrix()
	 */
	@Override
	public void pushMatrix() {
		GL11.glPushMatrix();
	}

	@Override
	public void pushName(int name) {
		GL11.glPushName(name);
	}

	@Override
	public int renderMode(GLRenderMode mode) {
		return GL11.glRenderMode(mode.getGlInt());
	}

	@Override
	public void rotate(double angle, double x, double y, double z) {
		GL11.glRotated(angle, x, y, z);
	}

	@Override
	public void rotate(double angle, Vector3d ang) {
		GL11.glRotated(angle, ang.x, ang.y, ang.z);
	}

	@Override
	public void rotate(double angle, Vector3f ang) {
		GL11.glRotated(angle, ang.x, ang.y, ang.z);
	}

	@Override
	public void rotate(double angle, Vector3i ang) {
		GL11.glRotated(angle, ang.x, ang.y, ang.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#rotate(float, float, float, float)
	 */
	@Override
	public void rotate(float angle, float x, float y, float z) {
		GL11.glRotatef(angle, x, y, z);
	}

	@Override
	public void rotate(float angle, Vector3d ang) {
		GL11.glRotated(angle, ang.x, ang.y, ang.z);
	}

	@Override
	public void rotate(float angle, Vector3f ang) {
		GL11.glRotatef(angle, ang.x, ang.y, ang.z);
	}

	@Override
	public void rotate(float angle, Vector3i ang) {
		GL11.glRotatef(angle, ang.x, ang.y, ang.z);
	}

	@Override
	public void scale(double x, double y) {
		GL11.glScaled(x, y, 0);
	}

	@Override
	public void scale(double x, double y, double z) {
		GL11.glScaled(x, y, z);
	}

	@Override
	public void scale(float x, float y) {
		GL11.glScalef(x, y, 0);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#scale(float, float, float)
	 */
	@Override
	public void scale(float x, float y, float z) {
		GL11.glScalef(x, y, z);
	}

	@Override
	public void scale(Vector2d scale) {
		GL11.glScaled(scale.x, scale.y, 0);
	}

	@Override
	public void scale(Vector2f scale) {
		GL11.glScalef(scale.x, scale.y, 0);
	}

	@Override
	public void scale(Vector2i scale) {
		GL11.glScalef(scale.x, scale.y, 0);
	}

	@Override
	public void scale(Vector3d scale) {
		GL11.glScaled(scale.x, scale.y, scale.z);
	}

	@Override
	public void scale(Vector3f scale) {
		GL11.glScalef(scale.x, scale.y, scale.z);
	}

	@Override
	public void scale(Vector3i scale) {
		GL11.glScalef(scale.x, scale.y, scale.z);
	}

	@Override
	public void scissor(int x, int y, int width, int height) {
		GL11.glScissor(x, y, width, height);
	}

	@Override
	public void selectBuffer(IntBuffer buffer) {
		GL11.glSelectBuffer(buffer);
	}
	
	@Override
	public void shaderUniform1f(String var, float x) {
		Graphics.getClientShaderManager().shaderUniform1f(var, x);
	}

	@Override
	public void shaderUniform1i(String var, int x) {
		Graphics.getClientShaderManager().shaderUniform1f(var, x);
	}

	@Override
	public void shaderUniform2f(String var, float x, float y) {
		Graphics.getClientShaderManager().shaderUniform2f(var, x, y);
	}

	@Override
	public void shaderUniform2f(String var, Vector2f vec) {
		Graphics.getClientShaderManager().shaderUniform2f(var, vec);
	}

	@Override
	public void shaderUniform2i(String var, int x, int y) {
		Graphics.getClientShaderManager().shaderUniform2i(var, x, y);
	}

	@Override
	public void shaderUniform2i(String var, Vector2i vec) {
		Graphics.getClientShaderManager().shaderUniform2i(var, vec);
	}

	@Override
	public void shaderUniform3f(String var, float x, float y, float z) {
		Graphics.getClientShaderManager().shaderUniform3f(var, x, y, z);
	}

	@Override
	public void shaderUniform3f(String var, Vector3f vec) {
		Graphics.getClientShaderManager().shaderUniform3f(var, vec);
	}

	@Override
	public void shaderUniform3i(String var, int x, int y, int z) {
		Graphics.getClientShaderManager().shaderUniform3i(var, x, y, z);
	}

	@Override
	public void shaderUniform3i(String var, Vector3i vec) {
		Graphics.getClientShaderManager().shaderUniform3i(var, vec);
	}


	@Override
	public void shaderUniform4f(String var, float x, float y, float z, float w){
		Graphics.getClientShaderManager().shaderUniform4f(var, x, y, z, w);
	}

	@Override
	public void shaderUniform4f(String var, Vector4f vec) {
		Graphics.getClientShaderManager().shaderUniform4f(var, vec);
	}

	@Override
	public void shaderUniform4i(String var, int x, int y, int z, int w) {
		Graphics.getClientShaderManager().shaderUniform4i(var, x, y, z, w);
	}

	@Override
	public void shaderUniform4i(String var, Vector4i vec) {
		Graphics.getClientShaderManager().shaderUniform4i(var, vec);
	}

	@Override
	public void texCoord(double s) {
		GL11.glTexCoord1d(s);
	}

	@Override
	public void texCoord(double s, double t) {
		GL11.glTexCoord2d(s, t);
		
	}

	@Override
	public void texCoord(double s, double t, double r) {
		GL11.glTexCoord3d(s, t, r);
		
	}

	@Override
	public void texCoord(double s, double t, double r, double q) {
		GL11.glTexCoord4d(s, t, r, q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#texCoord(float)
	 */
	@Override
	public void texCoord(float s) {
		GL11.glTexCoord1f(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#texCoord(float, float)
	 */
	@Override
	public void texCoord(float s, float t) {
		GL11.glTexCoord2f(s, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#texCoord(float, float, float)
	 */
	@Override
	public void texCoord(float s, float t, float r) {
		GL11.glTexCoord3f(s, t, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#texCoord(float, float, float, float)
	 */
	@Override
	public void texCoord(float s, float t, float r, float q) {
		GL11.glTexCoord4f(s, t, r, q);
	}

	@Override
	public void texCoord(Vector2d tex) {
		GL11.glTexCoord2d(tex.x, tex.y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texCoord(vc4.api.vector.Vector2f)
	 */
	@Override
	public void texCoord(Vector2f tex) {
		GL11.glTexCoord2f(tex.x, tex.y);
	}

	@Override
	public void texCoord(Vector3d tex) {
		GL11.glTexCoord3d(tex.x, tex.y, tex.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texCoord(vc4.api.vector.Vector3f)
	 */
	@Override
	public void texCoord(Vector3f tex) {
		GL11.glTexCoord3f(tex.x, tex.y, tex.z);
	}

	@Override
	public void texCoord(Vector4d tex) {
		GL11.glTexCoord4d(tex.x, tex.y, tex.z, tex.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texCoord(vc4.api.vector.Vector4f)
	 */
	@Override
	public void texCoord(Vector4f tex) {
		GL11.glTexCoord4f(tex.x, tex.y, tex.z, tex.w);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#glTexImage2D(vc4.api.graphics.GLTexture, int, vc4.api.graphics.GLInternalFormat, int, int, boolean, vc4.api.graphics.GLFormat, vc4.api.graphics.GLType, java.nio.ByteBuffer)
	 */
	@Override
	public void texImage2D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, boolean border, GLFormat format, GLType type, ByteBuffer data) {
		GL11.glTexImage2D(target.getGlInt(), level, internalFormat.getGlInt(), width, height, border ? 1: 0, format.getGlInt(), type.getGlInt(), data);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texImage3D(vc4.api.graphics.GLTexture, int, vc4.api.graphics.GLInternalFormat, int, int, int, boolean, vc4.api.graphics.GLFormat, vc4.api.graphics.GLType, java.nio.ByteBuffer)
	 */
	@Override
	public void texImage3D(GLTexture target, int level, GLInternalFormat internalFormat, int width, int height, int depth, boolean border, GLFormat format, GLType type, ByteBuffer data) {
		GL12.glTexImage3D(target.getGlInt(), level, internalFormat.getGlInt(), width, height, depth, border ? 1: 0, format.getGlInt(), type.getGlInt(), data);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterGenerateMipmap(vc4.api.graphics.GLTexture, boolean)
	 */
	@Override
	public void texParameterGenerateMipmap(GLTexture target, boolean mipmap) {
		GL11.glTexParameteri(target.getGlInt(), GL14.GL_GENERATE_MIPMAP, mipmap ? 1 : 0);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterMagFilter(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTextureFilter)
	 */
	@Override
	public void texParameterMagFilter(GLTexture target, GLTextureFilter filter) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_MAG_FILTER, filter.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterMinFilter(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTextureFilter, vc4.api.graphics.GLTextureFilter)
	 */
	@Override
	public void texParameterMinFilter(GLTexture target, GLTextureFilter filter, GLTextureFilter mipmap) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_MIN_FILTER, filter.getGlMipmapInt(mipmap));
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapR(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapR(GLTexture target, GLTexWrap wrap) {
		GL11.glTexParameteri(target.getGlInt(), GL12.GL_TEXTURE_WRAP_R, wrap.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapS(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapS(GLTexture target, GLTexWrap wrap) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_S, wrap.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapST(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapST(GLTexture target, GLTexWrap wrap) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_S, wrap.getGlInt());
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_T, wrap.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapSTR(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrap) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_S, wrap.getGlInt());
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_T, wrap.getGlInt());
		GL11.glTexParameteri(target.getGlInt(), GL12.GL_TEXTURE_WRAP_R, wrap.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapSTR(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapSTR(GLTexture target, GLTexWrap wrapst, GLTexWrap wrapr) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_S, wrapst.getGlInt());
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_T, wrapst.getGlInt());
		GL11.glTexParameteri(target.getGlInt(), GL12.GL_TEXTURE_WRAP_R, wrapr.getGlInt());
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#texParameterWrapT(vc4.api.graphics.GLTexture, vc4.api.graphics.GLTexWrap)
	 */
	@Override
	public void texParameterWrapT(GLTexture target, GLTexWrap wrap) {
		GL11.glTexParameteri(target.getGlInt(), GL11.GL_TEXTURE_WRAP_T, wrap.getGlInt());
	}

	@Override
	public void texSubImage3D(GLTexture target, int level, int xOffset, int yOffset, int zOffset, int width, int height, int depth, GLFormat format, GLType type, ByteBuffer data) {
		GL12.glTexSubImage3D(target.getGlInt(), level, xOffset, yOffset, zOffset, width, height, depth, format.getGlInt(), type.getGlInt(), data);
		
	}

	@Override
	public void translate(double x, double y) {
		GL11.glTranslated(x, y, 0);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#translate(double, double, double)
	 */
	@Override
	public void translate(double x, double y, double z) {
		GL11.glTranslated(x, y, z);
	}

	@Override
	public void translate(float x, float y) {
		GL11.glTranslatef(x, y, 0);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#translate(float, float, float)
	 */
	@Override
	public void translate(float x, float y, float z) {
		GL11.glTranslatef(x, y, z);
	}

	@Override
	public void translate(Vector2d trans) {
		GL11.glTranslated(trans.x, trans.y, 0);
	}

	@Override
	public void translate(Vector2f trans) {
		GL11.glTranslatef(trans.x, trans.y, 0);
	}

	@Override
	public void translate(Vector2i trans) {
		GL11.glTranslatef(trans.x, trans.y, 0);
	}

	@Override
	public void translate(Vector3d trans) {
		GL11.glTranslated(trans.x, trans.y, trans.z);
	}

	@Override
	public void translate(Vector3f trans) {
		GL11.glTranslatef(trans.x, trans.y, trans.z);
	}

	@Override
	public void translate(Vector3i trans) {
		GL11.glTranslatef(trans.x, trans.y, trans.z);
	}

	@Override
	public void unbindShader() {
		Graphics.getClientShaderManager().unbindShader();
	}

	@Override
	public void vertex(double x, double y) {
		GL11.glVertex2d(x, y);
	}

	@Override
	public void vertex(double x, double y, double z) {
		GL11.glVertex3d(x, y, z);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#vertex(float, float)
	 */
	@Override
	public void vertex(float x, float y) {
		GL11.glVertex2f(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#vertex(float, float, float)
	 */
	@Override
	public void vertex(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#vertex(int, int)
	 */
	@Override
	public void vertex(int x, int y) {
		GL11.glVertex2i(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#vertex(int, int, int)
	 */
	@Override
	public void vertex(int x, int y, int z) {
		GL11.glVertex3i(x, y, z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#vertex(vc4.api.vector.Vector2f)
	 */
	@Override
	public void vertex(Vector2f vertex) {
		GL11.glVertex2f(vertex.x, vertex.y);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#vertex(vc4.api.vector.Vector3f)
	 */
	@Override
	public void vertex(Vector3f vertex) {
		GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#vertexAttribPointer(int, int, boolean, int, java.nio.FloatBuffer)
	 */
	@Override
	public void vertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
		GL20.glVertexAttribPointer(index, size, normalized, stride, buffer);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.OpenGL#vertexWithTexture(float, float, float, float, float, float)
	 */
	@Override
	public void vertexWithTexture(float x, float y, float z, float r, float s, float t) {
		texCoord(r, s, t);
		vertex(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.OpenGL#viewport(int, int, int, int)
	 */
	@Override
	public void viewport(int x, int y, int width, int height) {
		viewport[0] = x;
		viewport[1] = y;
		viewport[2] = width;
		viewport[3] = height;
		GL11.glViewport(x, y, width, height);
	}

}
