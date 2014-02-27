/**
 * 
 */
package vc4.client.font;

import java.awt.Color;

/**
 * @author paul
 * 
 */
public class Line {

	public float startX, endX, y;
	public Color color;

	/**
	 * 
	 */
	public Line() {
	}

	public Line(float startX, float y, Color color) {
		super();
		this.startX = endX = startX;
		this.y = y;
		this.color = color;
	}

	public void addLength(float f) {
		endX += f;
	}

}
