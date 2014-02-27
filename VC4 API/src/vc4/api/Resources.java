package vc4.api;

import java.net.URL;
import java.util.List;

import vc4.api.font.Font;
import vc4.api.graphics.texture.AnimatedTexture;
import vc4.api.graphics.texture.SheetTexture;
import vc4.api.model.Model;

public abstract class Resources {

	protected static Resources _res;

	public abstract AnimatedTexture agetAnimatedTexture(String name);

	public abstract SheetTexture agetSheetTexture(String name);

	public abstract Font agetFont(String name);

	public abstract List<URL> agetResourceURLs();

	public abstract Model agetModel(String name);

	public static AnimatedTexture getAnimatedTexture(String name) {
		return _res.agetAnimatedTexture(name);
	}

	public static SheetTexture getSheetTexture(String name) {
		return _res.agetSheetTexture(name);
	}

	public static Font getFont(String name) {
		return _res.agetFont(name);
	}

	public static Model getModel(String name) {
		return _res.agetModel(name);
	}

	public static List<URL> getResourceURLs() {
		return _res.agetResourceURLs();
	}

	public static Resources getRes() {
		return _res;
	}

}
