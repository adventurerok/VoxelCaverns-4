/**
 * 
 */
package vc4.api.graphics;

import vc4.api.graphics.shader.ShaderManager;
import vc4.api.graphics.texture.AnimatedTextureLoader;
import vc4.api.graphics.texture.SheetTextureLoader;

/**
 * @author paul
 *
 */
public class Graphics {

	private static OpenGL _gl;
	private static AnimatedTextureLoader _animatedLoader;
	private static SheetTextureLoader _sheetLoader;
	private static ShaderManager _sm;
	
	public static void setImplementation(OpenGL gl){
		_gl = gl;
	}
	
	public static void setShaderManager(ShaderManager sm){
		_sm = sm;
	}
	
	public static void setAnimatedLoader(AnimatedTextureLoader animatedLoader) {
		_animatedLoader = animatedLoader;
	}
	
	public static void setSheetLoader(SheetTextureLoader sheetLoader) {
		_sheetLoader = sheetLoader;
	}
	
	/**
	 * Finds the Client's OpenGL implementation
	 * @return the clients OpenGL implementation, or null if server
	 */
	public static OpenGL getOpenGL(){
		return _gl;
	}
	
	
	public static SheetTextureLoader getSheetLoader() {
		return _sheetLoader;
	}
	
	public static AnimatedTextureLoader getAnimatedLoader() {
		return _animatedLoader;
	}
	
	/**
	 * Finds the Client's Shader Manager implementation
	 * @return The client's shader manager implementation, or null if server
	 */
	public static ShaderManager getClientShaderManager(){
		return _sm;
	}

}
