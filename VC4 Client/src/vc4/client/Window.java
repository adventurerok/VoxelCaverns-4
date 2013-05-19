package vc4.client;

import java.util.EnumSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import vc4.api.client.ClientWindow;
import vc4.api.graphics.*;
import vc4.api.graphics.texture.AnimatedTexture;

public class Window extends ClientWindow {

	private static OpenGL gl;

	private String _title;
	private int _width, _height;
	private boolean _closeRequested = false;
	
	private Game game;

	AnimatedTexture test;
	
	Renderer tr;

	int texindex = 0;

	/**
	 * 
	 */
	public Window() {
		game = new Game(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#run()
	 */
	@Override
	public void run() {
		try {
			Display.setDisplayMode(new DisplayMode(_width, _height));
			Display.setTitle(_title);
			Display.setResizable(true);

			PixelFormat p = new PixelFormat().withDepthBits(24).withAlphaBits(8).withStencilBits(0);
			Display.create(p);
			if (gl == null) gl = Graphics.getClientOpenGL();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#draw()
	 */
	@Override
	protected void draw() {
		game.draw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#update()
	 */
	@Override
	protected void update() {
		game.update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#load()
	 */
	@Override
	protected void load() {
		gl.enable(GLFlag.BLEND);
		gl.blendFunc(GLBlendFunc.SRC_ALPHA, GLBlendFunc.ONE_MINUS_SRC_ALPHA);
		
		gl.depthFunc(GLCompareFunc.LEQUAL);
		gl.enable(GLFlag.ALPHA_TEST);
		gl.alphaFunc(GLCompareFunc.GREATER, 0.35F);
		
		game.load();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#resized()
	 */
	@Override
	protected void resized() {
		_width = Display.getWidth();
		_height = Display.getHeight();
		enterRenderMode(RenderType.GAME);
		
		game.resized();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#unload()
	 */
	@Override
	protected void unload() {
		game.unload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setTitle(java.lang.String)
	 */
	@Override
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
	@Override
	public String getTitle() {
		return _title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setWidth(int)
	 */
	@Override
	public int setWidth(int width) {
		if (width < MIN_WIDTH) width = MIN_WIDTH;
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
	@Override
	public int getWidth() {
		return _width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#setHeight(int)
	 */
	@Override
	public int setHeight(int height) {
		if (height < MIN_HEIGHT) height = MIN_HEIGHT;
		int old = _height;
		_height = height;
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
	 * @see vc4.api.client.ClientWindow#getHeight()
	 */
	@Override
	public int getHeight() {
		return _height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#close()
	 */
	@Override
	public void close() {
		_closeRequested = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#isCloseRequested()
	 */
	@Override
	public boolean isCloseRequested() {
		return _closeRequested;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#isCreated()
	 */
	@Override
	public boolean isCreated() {
		return Display.isCreated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientWindow#enterRenderMode(vc4.api.graphics.RenderType)
	 */
	@Override
	public void enterRenderMode(RenderType mode) {
		gl.matrixMode(GLMatrixMode.PROJECTION);
		gl.loadIdentity();
		if (mode == RenderType.GAME) {
			gl.perspective(game.getFieldOfVision(), getAspectRatio(), 0.1F, 476F);
			gl.lookAt(0, 0, 0, 0, 0, 1, 0, 1, 0);
		} else gl.ortho(0, _width, _height, 0, 32, -32);
		gl.matrixMode(GLMatrixMode.MODELVIEW);
		gl.loadIdentity();
		gl.viewport(0, 0, _width, _height);
	}
	
	/**
	 * @return the game
	 */
	@Override
	public Game getGame() {
		return game;
	}

	public float getAspectRatio() {
		return _width / (float)_height;
	}

}
