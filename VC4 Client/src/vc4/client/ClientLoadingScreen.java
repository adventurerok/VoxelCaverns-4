/**
 * 
 */
package vc4.client;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import vc4.api.client.LoadingScreen;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.RenderType;

/**
 * @author paul
 *
 */
public class ClientLoadingScreen implements LoadingScreen{
	
	String title = "Loading...", info;
	FontRenderer font;
	Window window;

	

	public ClientLoadingScreen(FontRenderer font, Window window) {
		super();
		this.font = font;
		this.window = window;
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.LoadingScreen#setLoadingTitle(java.lang.String)
	 */
	@Override
	public void setLoadingTitle(String title) {
		this.title = title;
		if(Display.wasResized()) window.resized();
		
		glClearColor(0.3F, 0.5F, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		window.enterRenderMode(RenderType.GUI);
		
		font.renderString(10, 10, title);
		if(info != null) font.renderString(30, 30, info);
		
		Display.update();
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.LoadingScreen#getLoadingTitle()
	 */
	@Override
	public String getLoadingTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.LoadingScreen#setLoadingInfo(java.lang.String)
	 */
	@Override
	public void setLoadingInfo(String info) {
		this.info = info;
		if(Display.wasResized()) window.resized();
		
		glClearColor(0.3F, 0.5F, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		window.enterRenderMode(RenderType.GUI);
		font.renderString(10, 10, title);
		if(info != null) font.renderString(30, 30, info);
		
		Display.update();
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.LoadingScreen#getLoadingInfo()
	 */
	@Override
	public String getLoadingInfo() {
		return info;
	}

}
