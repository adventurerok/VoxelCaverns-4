package vc4.launcher;

import vc4.launcher.gui.LauncherGui;

public class Launcher {

	private static Launcher singleton;
	
	private LauncherGui gui;
	
	public static Launcher getSingleton() {
		return singleton;
	}
	
	public Launcher() {
		singleton = this;
		gui = new LauncherGui();
		gui.setVisible(true);
	}
	
	public LauncherGui getGui() {
		return gui;
	}
	
}
