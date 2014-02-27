package vc4.editor;

import java.util.EnumSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import vc4.api.graphics.*;
import vc4.api.graphics.texture.AnimatedTexture;

public class Window {

	private static OpenGL gl;

	private String _title;
	private int _width, _height;
	private boolean _closeRequested = false;

	AnimatedTexture test;

	Renderer tr;

	int texindex = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#run()
	 */
	public void run() {
		try {
			Display.setDisplayMode(new DisplayMode(_width, _height));
			Display.setTitle(_title);
			Display.setResizable(true);

			PixelFormat p = new PixelFormat().withDepthBits(24).withAlphaBits(8).withStencilBits(0);
			Display.create(p);
			if (gl == null) gl = Graphics.getOpenGL();

			load();
			resized();

			gl.clearColor(0.4F, 0.8F, 1F, 1F);
			while (!_closeRequested) {
				gl.clear(EnumSet.of(GLBufferBit.COLOR, GLBufferBit.DEPTH));
				gl.loadIdentity();

				update();
				draw();

				if (Display.wasResized()) resized();
				if (Display.isCloseRequested()) _closeRequested = true;
				Display.update();
				Display.sync(65);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}

			}
			unload();
			Display.destroy();

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	protected void draw() {

	}

	protected void update() {

	}

	protected void load() {
		gl.enable(GLFlag.BLEND);
		gl.blendFunc(GLBlendFunc.SRC_ALPHA, GLBlendFunc.ONE_MINUS_SRC_ALPHA);

		gl.depthFunc(GLCompareFunc.LEQUAL);
		gl.enable(GLFlag.ALPHA_TEST);
		gl.alphaFunc(GLCompareFunc.GREATER, 0.35F);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#resized()
	 */
	protected void resized() {
		_width = Display.getWidth();
		_height = Display.getHeight();
		enterRenderMode(RenderType.GAME);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#unload()
	 */
	protected void unload() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setTitle(java.lang.String)
	 */
	public String setTitle(String title) {
		String old = _title;
		_title = title;
		Display.setTitle(_title);
		return old;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#getTitle()
	 */
	public String getTitle() {
		return _title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setWidth(int)
	 */
	public int setWidth(int width) {
		if (width < 640) width = 640;
		int old = _width;
		_width = width;
		try {
			Display.setDisplayMode(new DisplayMode(_width, _height));
		} catch (LWJGLException e) {
			throw new GraphicsException(e);
		}
		return old;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#getWidth()
	 */
	public int getWidth() {
		return _width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setHeight(int)
	 */
	public int setHeight(int height) {
		if (height < 480) height = 480;
		int old = _height;
		_height = height;
		try {
			Display.setDisplayMode(new DisplayMode(_width, _height));
		} catch (LWJGLException e) {
			throw new GraphicsException(e);
		}
		return old;
	}

	public int getHeight() {
		return _height;
	}

	public void close() {
		_closeRequested = true;
	}

	public boolean isCloseRequested() {
		return _closeRequested;
	}

	public boolean isCreated() {
		return Display.isCreated();
	}

	public void enterRenderMode(RenderType mode) {
		gl.matrixMode(GLMatrixMode.PROJECTION);
		gl.loadIdentity();
		if (mode == RenderType.GAME) {
			gl.perspective(70, getAspectRatio(), 0.1F, 476F);
			gl.lookAt(0, 0, 0, 0, 0, 1, 0, 1, 0);
		} else gl.ortho(0, _width, _height, 0, 32, -32);
		gl.matrixMode(GLMatrixMode.MODELVIEW);
		gl.loadIdentity();
		gl.viewport(0, 0, _width, _height);
	}

	public float getAspectRatio() {
		return _width / (float) _height;
	}

}
