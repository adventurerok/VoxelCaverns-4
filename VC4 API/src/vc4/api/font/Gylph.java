/**
 * 
 */
package vc4.api.font;

import java.lang.reflect.Field;

import vc4.api.logging.Logger;

/**
 * @author paul
 * 
 */
public class Gylph {

	public int id, x, y, width, height, xoffset, yoffset, xadvance, page;

	public float sx, sy, ex, ey;

	public Gylph(int id, int x, int y, int width, int height, int xoffset, int yoffset, int xadvance, int page) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
		this.page = page;
		calculateFloats();
	}

	public Gylph(String text) {
		try {
			Class<Gylph> clz = Gylph.class;
			String parts[] = text.split(" ");
			for (String s : parts) {
				if (!s.contains("=")) continue;
				String subs[] = s.split("=");
				String var = subs[0];
				if (var.equals("chnl")) continue;
				int val = Integer.parseInt(subs[1]);
				Field f = clz.getDeclaredField(var);
				f.setInt(this, val);
			}
		} catch (Exception e) {
			Logger.getLogger(Gylph.class).warning("Error occured while creating Gylph", e);
		}
		calculateFloats();
	}

	private void calculateFloats() {
		sx = x / 256F;
		sy = y / 256F;
		ex = (x + width) / 256F;
		ey = (y + height) / 256F;
	}

}
