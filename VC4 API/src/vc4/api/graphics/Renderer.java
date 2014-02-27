/**
 * 
 */
package vc4.api.graphics;

import java.awt.Color;

import vc4.api.vector.*;

/**
 * @author paul
 * 
 */
public interface Renderer {

	public abstract void addVertex(double x, double y, double z);

	public abstract void addVertex(Vertex v);

	public abstract void color(float r, float g, float b, float a);

	public abstract void color(Color color);

	public abstract void color(Color color, double multiply);

	public abstract void tex(float s, float t, float r, float q);

	public abstract void setTranslate(double x, double y, double z);

	public abstract void useQuadInputMode(boolean use);

	public abstract void compile();

	public abstract void render();

	public abstract void destroy();

	public abstract void tex(double s, double t, double r, double q);

	public abstract void tex(double s, double t, double r);

	public abstract void color(double r, double g, double b, double a);

	public abstract void tex(Vector4f tex);

	public abstract void color(Vector4f color);

	public abstract void addVertex(Vector3d vertex);

	public abstract void tex(Vector3f tex);

	public abstract void light(float r, float g, float b, boolean sky);

	public abstract void light(float r, float g, float b, float sky);

	public abstract void light(Vector3f light, boolean sky);

	public abstract void light(Vector3f light, float sky);

}