package vc4.api.gui;

import java.awt.Rectangle;

public class PercentResizor implements Resizer {

	float x, y;
	float width, height;
	int absWidth, absHeight;
	
	
	
	public PercentResizor(float x, float y, float width, float height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}


	public PercentResizor(float x, float y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.absWidth = width;
		this.absHeight = height;
	}
	
	


	public PercentResizor(float x, float y, float width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.absHeight = height;
	}
	
	


	public PercentResizor(float x, float y, int width, float height) {
		super();
		this.x = x;
		this.y = y;
		this.absWidth = width;
		this.height = height;
	}


	@Override
	public void resize(Component target) {
		Component par = target.getParent();
		int sx = par.getX() + (int)(par.getWidth() * x);
		int sy = par.getY() + (int)(par.getHeight() * y);
		int sw = absWidth != 0 ? absWidth : (int)(par.getWidth() * width);
		int sh = absHeight != 0 ? absHeight : (int)(par.getHeight() * height);
		target.setBounds(new Rectangle(sx, sy, sw, sh));
	}
	

}
