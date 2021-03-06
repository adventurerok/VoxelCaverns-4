/**
 * 
 */
package vc4.api.render;

import java.awt.Color;
import java.nio.FloatBuffer;

import vc4.api.graphics.*;
import vc4.api.list.FloatList;
import vc4.api.util.BufferUtils;
import vc4.api.vector.*;

/**
 * @author paul
 * 
 */
public class ChunkRenderer implements Renderer {

	private static OpenGL gl;

	FloatBuffer buffer;
	FloatList data = new FloatList(); // Using normal list is extremely slow

	Vector4f color = new Vector4f(1, 1, 1, 1);
	Vector4f tex = new Vector4f(0, 0, 0, 0);
	Vector4f light = new Vector4f(1, 1, 1, 0);

	Vector3d trans = new Vector3d(0, 0, 0);

	Vertex[] quad = new Vertex[4];
	Vertex[] tri = new Vertex[3];
	int qpos = 0;
	int tpos = 0;

	boolean quadInput = false;

	int amountOfVertexes = 0;

	boolean createdList = false;
	int listId = 0;

	private boolean compiled;

	/**
	 * 
	 */
	public ChunkRenderer() {
		if (gl == null) gl = Graphics.getOpenGL();
	}

	public boolean isCompiled() {
		return compiled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#vertex(double, double, double)
	 */
	@Override
	public void vertex(double x, double y, double z) {
		if (!quadInput) {
			vertex(new Vertex(new Vector3d(x + trans.x, y + trans.y, z + trans.z), color, tex));
			return;
		}
		if (qpos < 4) {
			quad[qpos++] = new Vertex(new Vector3d(x + trans.x, y + trans.y, z + trans.z), color, tex);
		}
		if (qpos == 4) {
			vertex(quad[0]);
			vertex(quad[2]);
			vertex(quad[1]);

			vertex(quad[0]);
			vertex(quad[3]);
			vertex(quad[2]);

			qpos = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#vertex(vc4.impl.world.rendering.DataRenderer.Vertex)
	 */
	@Override
	public void vertex(Vertex v) {
		if (tpos < 3) {
			tri[tpos++] = v;
		}
		if (tpos == 3) {
			Vector3f normal = new Vector3f(0, 0, 0);
			Vector3f U = tri[1].position.subtract(tri[0].position).toVector3f();
			Vector3f V = tri[2].position.subtract(tri[0].position).toVector3f();

			normal.x = U.y * V.z - U.z * V.y;
			normal.y = U.z * V.x - U.x * V.z;
			normal.z = U.x * V.y - U.y * V.x;
			inputVertex(tri[0], normal);
			inputVertex(tri[1], normal);
			inputVertex(tri[2], normal);
			tpos = 0;
		}

	}

	private void inputVertex(Vertex v, Vector3f normal) {
		data.add((float) (v.position.x));
		data.add((float) (v.position.y));
		data.add((float) (v.position.z));
		data.add(v.color.x);
		data.add(v.color.y);
		data.add(v.color.z);
		data.add(v.color.w);
		data.add(v.tex.x);
		data.add(v.tex.y);
		data.add(v.tex.z);
		data.add(v.tex.w);
		data.add(normal.x);
		data.add(normal.y);
		data.add(normal.z);
		data.add(light.x);
		data.add(light.y);
		data.add(light.z);
		data.add(light.w);
		++amountOfVertexes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#color(float, float, float, float)
	 */
	@Override
	public void color(float r, float g, float b, float a) {
		color.x = r;
		color.y = g;
		color.z = b;
		color.w = a;
	}

	@Override
	public void light(float r, float g, float b, boolean sky) {
		light.x = r;
		light.y = g;
		light.z = b;
		light.w = sky ? 1 : 0;
	}

	@Override
	public void color(double r, double g, double b, double a) {
		color.x = (float) r;
		color.y = (float) g;
		color.z = (float) b;
		color.w = (float) a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#tex(float, float, float, float)
	 */
	@Override
	public void tex(float s, float t, float r, float q) {
		tex.x = s;
		tex.y = t;
		tex.z = r;
		tex.w = q;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#setTranslate(double, double, double)
	 */
	@Override
	public void setTranslate(double x, double y, double z) {
		trans.x = x;
		trans.y = y;
		trans.z = z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#useQuadInputMode(boolean)
	 */
	@Override
	public void useQuadInputMode(boolean use) {
		quadInput = use;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#compile()
	 */
	@Override
	public void compile() {
		if (amountOfVertexes < 1) return;
		buffer = BufferUtils.createFloatBuffer(data.size()); // List.size()
		buffer.put(data.toArray()); // Same as List.toArray()
		buffer.flip();
		data = null;
		tri = null;
		quad = null;
		color = null;
		tex = null;
		trans = null;
		compiled = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.impl.world.rendering.Renderer#render()
	 */
	@Override
	public void render() {
		if (amountOfVertexes < 1) return;
		if (!createdList) {
			listId = gl.genLists(1);
			gl.newList(listId, GLCompileFunc.COMPILE);
			boolean normal = true;
			buffer.position(0);
			gl.vertexAttribPointer(0, 3, normal, 72, buffer);
			buffer.position(3);
			gl.vertexAttribPointer(3, 4, normal, 72, buffer);
			buffer.position(7);
			gl.vertexAttribPointer(8, 4, normal, 72, buffer);
			buffer.position(11);
			gl.vertexAttribPointer(2, 3, normal, 72, buffer);
			buffer.position(14);
			gl.vertexAttribPointer(6, 4, normal, 72, buffer);
			gl.drawArrays(GLPrimitive.TRIANGLES, 0, amountOfVertexes);
			gl.endList();
			buffer = null;
			createdList = true;
		}
		gl.callList(listId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.Renderer#color(java.awt.Color)
	 */
	@Override
	public void color(Color color) {
		color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
	}

	/**
	 * 
	 */
	@Override
	public void destroy() {
		gl.deleteLists(listId, 1);
		buffer = null;
		data = null;
	}

	@Override
	public void tex(double s, double t, double r, double q) {
		tex((float) s, (float) t, (float) r, (float) q);

	}

	@Override
	public void color(Color color, double multiply) {
		color((color.getRed() / 255f) * multiply, (color.getGreen() / 255f) * multiply, (color.getBlue() / 255f) * multiply, color.getAlpha() / 255f);

	}

	@Override
	public void color(Vector4f color) {
		this.color = color;
	}

	@Override
	public void tex(Vector4f tex) {
		this.tex = tex;
	}

	@Override
	public void addVertex(Vector3d vertex) {
		vertex(vertex.x, vertex.y, vertex.z);
	}

	@Override
	public void tex(Vector3f tex) {
		tex(tex.x, tex.y, tex.z, 0);

	}

	@Override
	public void light(float r, float g, float b, float sky) {
		light.x = r;
		light.y = g;
		light.z = b;
		light.w = sky;
	}

	@Override
	public void light(Vector3f light, boolean sky) {
		this.light.x = light.x;
		this.light.y = light.y;
		this.light.z = light.z;
		this.light.w = sky ? 1 : 0;
	}

	@Override
	public void light(Vector3f light, float sky) {
		this.light.x = light.x;
		this.light.y = light.y;
		this.light.z = light.z;
		this.light.w = sky;
	}

	@Override
	public void tex(double s, double t, double r) {
		tex.x = (float) s;
		tex.y = (float) t;
		tex.z = (float) r;
		tex.w = 0;
	}

}
