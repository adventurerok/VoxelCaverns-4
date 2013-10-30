/**
 * 
 */
package vc4.client.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import vc4.api.graphics.Graphics;
import vc4.api.graphics.shader.ShaderManager;

/**
 * @author paul
 *
 */
public class ClientShaderManager implements ShaderManager {

	protected static int GL_FALSE = 0;
	
	int current = 0;
	
	int vertex, fragment;
	
	private HashMap<String, Integer> namedShaders = new HashMap<String, Integer>();
	private HashMap<Integer, ShaderProgram> shaderPrograms = new HashMap<Integer, ShaderProgram>();
	
	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#bindShader(int)
	 */
	@Override
	public void bindShader(int shader) {
		current = shader;
		glUseProgram(current);
	}
	
	/**
	 * 
	 */
	public ClientShaderManager() {
		Graphics.setShaderManager(this);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#bindShader(java.lang.String)
	 */
	@Override
	public void bindShader(String name) {
		current = namedShaders.get(name);
		glUseProgram(current);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#unbindShader()
	 */
	@Override
	public void unbindShader() {
		glUseProgram(0);
		current = 0;
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#createShader(java.net.URL, java.lang.String)
	 */
	@Override
	public int createShader(URL file, String name) throws IOException{
		current = glCreateProgram();
		if (current == 0) return current;
		vertex = createVertShader(file);
		fragment = createFragShader(file);

		if (vertex == 0 || fragment == 0) return 0;
		glAttachShader(current, vertex);
		glAttachShader(current, fragment);
		bindAllAttributes();
		glLinkProgram(current);
		if (glGetProgrami(current, GL_LINK_STATUS) == GL_FALSE) {
			printLogInfo(current);
			return 0;
		}
		glValidateProgram(current);
		if (glGetProgrami(current, GL_VALIDATE_STATUS) == GL_FALSE) {
			printLogInfo(current);
			return 0;
		}
		shaderPrograms.put(current, new ShaderProgram(current));
		namedShaders.put(name, current);
		return current;
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#shaderUniform1i(java.lang.String, int)
	 */
	@Override
	public void shaderUniform1i(String var, int x) {
		int u = uniformLocation(var);
		glUniform1i(u, x);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#shaderUniform1f(java.lang.String, float)
	 */
	@Override
	public void shaderUniform1f(String var, float x) {
		int u = uniformLocation(var);
		glUniform1f(u, x);
	}

	/* (non-Javadoc)
	 * @see vc4.api.graphics.shader.ShaderManager#shaderUniform3f(java.lang.String, float, float, float)
	 */
	@Override
	public void shaderUniform3f(String var, float x, float y, float z) {
		int u = uniformLocation(var);
		glUniform3f(u, x, y, z);
	}
	
	@Override
	public void shaderUniform4f(String var, float x, float y, float z, float w) {
		int u = uniformLocation(var);
		glUniform4f(u, x, y, z, w);
	}

	protected static boolean printLogInfo(int obj) {
		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		glGetProgram(obj, GL_INFO_LOG_LENGTH, iVal);

		int length = iVal.get();
		if (length > 1) {
			// We have some info we need to output.
			ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
			iVal.flip();
			glGetShaderInfoLog(obj, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			String out = new String(infoBytes);
			System.out.println("Info log:\n" + out);
		} else return true;
		return false;
	}
	
	private void bindAllAttributes() {
		glBindAttribLocation(current, 0, "inVertex");
		glBindAttribLocation(current, 1, "inData1");
		glBindAttribLocation(current, 2, "inNormal");
		glBindAttribLocation(current, 3, "inColor");
		glBindAttribLocation(current, 4, "inSecColor");
		glBindAttribLocation(current, 5, "inFog");
		glBindAttribLocation(current, 6, "inData6");
		glBindAttribLocation(current, 7, "inData7");
		glBindAttribLocation(current, 8, "inTex");
		glBindAttribLocation(current, 9, "inTex1");
		glBindAttribLocation(current, 10, "inTex2");
		glBindAttribLocation(current, 11, "inTex3");
		glBindAttribLocation(current, 12, "inTex4");
		glBindAttribLocation(current, 13, "inTex5");
		glBindAttribLocation(current, 14, "inTex6");
		glBindAttribLocation(current, 15, "inTex7");
	}
	
	protected int createVertShader(URL url) throws IOException {
		url = new URL(url.toString() + ".vert");
		InputStream input = url.openStream();

		vertex = glCreateShader(GL_VERTEX_SHADER);
		// if created, convert the vertex shader code to a String
		if (vertex == 0) { return 0; }
		String vertexCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while ((line = reader.readLine()) != null) {
				vertexCode += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Fail reading vertex shading code");
			return 0;
		}
		/*
		 * associate the vertex code String with the created vertex shader and compile
		 */
		glShaderSource(vertex, vertexCode);
		glCompileShader(vertex);
		// if there was a problem compiling, reset vertShader to zero
		if (glGetProgrami(vertex, GL_COMPILE_STATUS) == GL_FALSE) {
			printLogInfo(vertex);
			vertex = 0;
		}
		// if zero we won't be using the shader
		return vertex;
	}

	protected int createFragShader(URL url) throws IOException {
		url = new URL(url.toString() + ".frag");
		InputStream input = url.openStream();
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		if (fragment == 0) { return 0; }
		String fragCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while ((line = reader.readLine()) != null) {
				fragCode += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Fail reading fragment shading code");
			return 0;
		}
		glShaderSource(fragment, fragCode);
		glCompileShader(fragment);
		if (glGetProgrami(fragment, GL_COMPILE_STATUS) == GL_FALSE) {
			printLogInfo(fragment);
			fragment = 0;
		}
		return fragment;
	}
	
	private int uniformLocation(String name) {
		return shaderPrograms.get(current).getUniformLocation(name);
	}
	
	public static class ShaderProgram {

		private int id;
		private HashMap<String, Integer> uniforms = new HashMap<String, Integer>();

		public int getUniformLocation(String name) {
			if (uniforms.containsKey(name)) return uniforms.get(name);
			else {
				int a = glGetUniformLocation(id, name);
				uniforms.put(name, a);
				return a;
			}
		}

		public ShaderProgram(int id) {
			super();
			this.id = id;
		}
	}

}
