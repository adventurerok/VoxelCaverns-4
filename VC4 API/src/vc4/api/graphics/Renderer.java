/**
 * 
 */
package vc4.api.graphics;

import java.awt.Color;


/**
 * @author paul
 *
 */
public interface Renderer {

	public abstract void addVertex(double x, double y, double z);
	public abstract void addVertex(Vertex v);

	public abstract void color(float r, float g, float b, float a);
	public abstract void color(Color color);

	public abstract void tex(float s, float t, float r, float q);

	public abstract void setTranslate(double x, double y, double z);

	public abstract void useQuadInputMode(boolean use);

	public abstract void compile();

	public abstract void render();
	public abstract void tex(double s, double t, double r, double q);

}