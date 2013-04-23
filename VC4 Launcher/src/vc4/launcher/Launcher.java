package vc4.launcher;

import java.io.IOException;
import java.net.URL;

import vc4.launcher.gui.frame.LauncherGui;

public class Launcher {

	private static Launcher singleton;
	
	private LauncherGui gui;
	private Repo vc4;
	
	public static Launcher getSingleton() {
		return singleton;
	}
	
	public Repo getVc4() {
		return vc4;
	}
	
	public Launcher() {
		singleton = this;
		Repo rec = new Repo();
		try {
			rec.loadInfo(new URL("https://raw.github.com/adventurerok/VoxelCaverns-4/master/VC4%20Downloads"));
			vc4 = rec;
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
		return vc4.getPackage("VC4-API");
	}

	public Package getImplPackage() {
		return vc4.getPackage("VC4-Impl");
	}

	public Package getClientPackage() {
		return vc4.getPackage("VC4-Client");
	}

	public Package getServerPackage() {
		return vc4.getPackage("VC4-Server");
	}

	public Package getVanillaPackage() {
		return vc4.getPackage("VC4-Vanilla");
	}

	public Package getEditorPackage() {
		return vc4.getPackage("VC4-Editor");
	}
	
}
