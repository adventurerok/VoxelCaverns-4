/**
 * 
 */
package vc4.client.font;

import java.awt.Color;

import vc4.api.font.Font;

/**
 * @author paul
 * 
 */
public class Background {

	public float startX, endX, startY, endY;
	public Color color;

	/**
	 * 
	 */
	public Background() {
	}

	public Background(float startX, float startY, float endY, Color color) {
		super();
		this.startX = endX = startX;
		this.startY = startY;
		this.endY = endY;
		this.color = color;
	}

	public Background(float startX, float y, Font font, Color color, float sizeMod) {
		super();
		this.startX = startX - 1;
		this.endX = startX + 1;
		this.startY = y + font.getMinY() * sizeMod;
		this.endY = y + font.getMaxY() * sizeMod;
		this.color = color;
	}

	public void addLength(float f) {
		endX += f;
	}

}
