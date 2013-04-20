/**
 * 
 */
package vc4.api.graphics;

import vc4.api.graphics.shader.ShaderManager;
import vc4.api.graphics.texture.TextureLoader;

/**
 * @author paul
 *
 */
public class Graphics {

	private static OpenGL _gl;
	private static TextureLoader _texLoader;
	private static ShaderManager _sm;
	
	public static void setImplementations(OpenGL gl, TextureLoader texLoader){
		_gl = gl;
		_texLoader = texLoader;
	}
	
	public static void setShaderManager(ShaderManager sm){
		_sm = sm;
	}
	
	/**
	 * Finds the Client's OpenGL implementation
	 * @return the clients OpenGL implementation, or null if server
	 */
	public static OpenGL getClientOpenGL(){
		return _gl;
	}
	
	
	/**
	 * Finds the Client's Texture Loader implementation
	 * @return the clients texture loader implementation, or null if server
	 */
	public static TextureLoader getClientTextureLoader(){
		return _texLoader;
	}
	
	/**
	 * Finds the Client's Shader Manager implementation
	 * @return The client's shader manager implementation, or null if server
	 */
	public static ShaderManager getClientShaderManager(){
		return _sm;
	}

}
