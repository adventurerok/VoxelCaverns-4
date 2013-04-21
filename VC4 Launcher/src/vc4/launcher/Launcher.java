package vc4.launcher;

import java.io.IOException;
import java.net.URL;

import vc4.launcher.gui.LauncherGui;

public class Launcher {

	private static Launcher singleton;
	
	private LauncherGui gui;
	private Package apiPackage, implPackage, clientPackage, serverPackage, vanillaPackage, editorPackage;
	
	public static Launcher getSingleton() {
		return singleton;
	}
	
	public Launcher() {
		singleton = this;
		Package pac = new Package();
		try {
			pac.loadInfo(new URL("file:C:/Temp/vc4-api"));
			apiPackage = pac;
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui = new LauncherGui();
		gui.setVisible(true);
	}
	
	public LauncherGui getGui() {
		return gui;
	}

	public Package getApiPackage() {
		return apiPackage;
	}

	public Package getImplPackage() {
		return implPackage;
	}

	public Package getClientPackage() {
		return clientPackage;
	}

	public Package getServerPackage() {
		return serverPackage;
	}

	public Package getVanillaPackage() {
		return vanillaPackage;
	}

	public Package getEditorPackage() {
		return editorPackage;
	}
	
}
