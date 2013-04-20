/**
 * 
 */
package vc4.api.graphics.shader;

import java.io.IOException;
import java.net.URL;

/**
 * @author paul
 *
 */
public interface ShaderManager {

	public void bindShader(int shader);
	public void bindShader(String name);
	public void unbindShader();
	public int createShader(URL file, String name) throws IOException;
	public void shaderUniform1i(String var, int x);
	public void shaderUniform1f(String var, float x);
	public void shaderUniform3f(String var, float x, float y, float z);
}
