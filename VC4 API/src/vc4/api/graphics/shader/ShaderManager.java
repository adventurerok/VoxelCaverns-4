/**
 * 
 */
package vc4.api.graphics.shader;

import java.io.IOException;
import java.net.URL;

import vc4.api.vector.*;

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
	public void shaderUniform2i(String var, int x, int y);
	public void shaderUniform3i(String var, int x, int y, int z);
	public void shaderUniform4i(String var, int x, int y, int z, int w);
	public void shaderUniform2i(String var, Vector2i vec);
	public void shaderUniform3i(String var, Vector3i vec);
	public void shaderUniform4i(String var, Vector4i vec);
	public void shaderUniform1f(String var, float x);
	public void shaderUniform2f(String var, float x, float y);
	public void shaderUniform3f(String var, float x, float y, float z);
	public void shaderUniform4f(String var, float x, float y, float z, float w);
	public void shaderUniform2f(String var, Vector2f vec);
	public void shaderUniform3f(String var, Vector3f vec);
	public void shaderUniform4f(String var, Vector4f vec);
}
