package vc4.client.gui;

import vc4.api.gui.Cursor;
import vc4.client.Game;
import vc4.client.Window;

public class ClientCursor implements Cursor {

	float x, y;
	String name;

	public ClientCursor(String name, float x, float y) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public ClientCursor(String name) {
		super();
		this.name = name;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void bind() {
		((Game) Window.getClientWindow().getGame()).setCursor(this);
	}

}
