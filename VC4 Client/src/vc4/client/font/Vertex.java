/**
 * 
 */
package vc4.client.font;

import java.awt.Color;

import vc4.api.vector.Vector2f;
import vc4.api.vector.Vector3f;

/**
 * @author paul
 * 
 */
public class Vertex {

	public Color color;
	public Vector3f tex;
	public Vector2f pos;

	public Vertex(Color color, Vector3f tex, Vector2f vector2f) {
		super();
		this.color = color;
		this.tex = tex;
		this.pos = vector2f;
	}

}
