/**
 * 
 */
package vc4.client.gui;

/**
 * @author paul
 * 
 */
public class ChatLine {

	long created;

	public ChatLine(long created, String text) {
		super();
		this.created = created;
		this.text = text;
	}

	String text;

	public float getAlpha() {
		float dist = (System.nanoTime() - created) / 1000000000F;
		if (dist < 2) return 1;
		float d3 = (1F - (dist - 2));
		return Math.max(0F, d3);
	}

}
