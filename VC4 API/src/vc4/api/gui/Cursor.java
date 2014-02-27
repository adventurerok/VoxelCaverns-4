package vc4.api.gui;

public interface Cursor {

	public float getX();

	public float getY();

	public void setX(float x);

	public void setY(float y);

	public String getName();

	public void setName(String name);

	public void bind();
}
