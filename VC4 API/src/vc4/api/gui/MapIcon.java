package vc4.api.gui;

import vc4.api.Resources;
import vc4.api.vector.Vector2f;

public class MapIcon {

	Vector2f pos;
	int icon;

	public MapIcon(String icon, Vector2f pos) {
		this.icon = Resources.getAnimatedTexture("mapicons").getArrayIndex(icon);
		this.pos = pos;
	}

	public int getIcon() {
		return icon;
	}

	public Vector2f getPos() {
		return pos;
	}

}
