package vc4.client;

import java.io.File;

import org.lwjgl.Sys;

import vc4.api.client.ClientLauncher;
import vc4.api.client.ClientWindow;
import vc4.api.logging.ChatBoxHandler;
import vc4.api.logging.FileOutputHandler;
import vc4.api.logging.Logger;
import vc4.api.util.DirectoryLocator;
import vc4.client.font.ClientFontRendererFactory;
import vc4.client.graphics.*;
import vc4.client.input.ClientKeyboard;
import vc4.client.input.ClientMouse;
import vc4.impl.GameLoader;
import vc4.impl.io.VCH4SaveFormat;

public class Launcher implements ClientLauncher{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args != null && args.length > 0 && args[0] != null){
			if(args[0].equals("nosave")) VCH4SaveFormat.ENABLED = false;
		}
		new ClientResources();
		System.setProperty("org.lwjgl.librarypath", new File(DirectoryLocator.getPath() + "/bin/natives/").getAbsolutePath());
		GameLoader.load(new ChatBoxHandler(), new FileOutputHandler());
		Logger.getLogger("VC4").info("LWJGL Version: " + Sys.getVersion());
		new ClientVoxelCaverns();
		new ClientGL();
		new ClientAnimatedTextureLoader();
		new ClientSheetTextureLoader();
		new ClientShaderManager();
		new ClientKeyboard();
		new ClientMouse();
		new ClientFontRendererFactory();
		Launcher l = new Launcher();
		l.loadLauncherPreferences();
		l.checkForUpdates();
		//new Gylph("char id=32   x=72    y=29    width=1     height=1     xoffset=0     yoffset=25    xadvance=16    page=0  chnl=15");
		ClientWindow c = l.createGameWindow("VoxelCaverns", 800, 600);
		c.run();
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.ClientLauncher#createGameWindow(java.lang.String, int, int)
	 */
	@Override
	public Window createGameWindow(String title, int width, int height) {
		Window w = new Window();
		w.setTitle(title);
		w.setWidth(width);
		w.setHeight(height);
		return w;
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.ClientLauncher#loadLauncherPreferences()
	 */
	@Override
	public void loadLauncherPreferences() {
	}

	/* (non-Javadoc)
	 * @see vc4.api.client.ClientLauncher#checkForUpdates()
	 */
	@Override
	public void checkForUpdates() {
	}

}
