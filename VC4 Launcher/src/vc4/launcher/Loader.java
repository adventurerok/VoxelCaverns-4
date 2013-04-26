package vc4.launcher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.UIManager;

import vc4.launcher.util.DirectoryLocator;
import vc4.launcher.util.YamlMap;


/**
 * Loads the launcher, and updates it
 * @author paul
 *
 */
public class Loader {

	public static int VERSION = 1;
	
	public static void main(String[] args) throws IOException {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		checkUpdates();
		new Launcher();
	}
	
	public static void checkUpdates() throws IOException{
		String path = "https://raw.github.com/adventurerok/VoxelCaverns-4/master/VC4%20Downloads/VC4-Launcher/";
		URL latest = new URL(path + "latest.yml");
		YamlMap map = new YamlMap(latest.openStream());
		int latestVersion = map.getInt("int");
		if(latestVersion <= VERSION) return;
		URL newer = new URL(path + "Launcher.jar");
		ReadableByteChannel rbc = Channels.newChannel(newer.openStream());
		FileOutputStream out = new FileOutputStream(DirectoryLocator.getPath() + "/bin/VC4-Launcher.jar");
		out.getChannel().transferFrom(rbc, 0, 1 << 28);
		out.close();
		String run = DirectoryLocator.getPath() + "/bin/VC4-Launcher.jar";
		String separator = System.getProperty("file.separator");
	    String jvm = System.getProperty("java.home") + separator + "bin" + separator + "javaw";
	    String memory = "256M";
	    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { jvm, "-Xmx" + memory, "-Xms" + memory, "-cp", run, "vc4.launcher.Loader", "heapset" });
	    processBuilder.redirectOutput(Redirect.INHERIT);
    	processBuilder.redirectError(Redirect.INHERIT);
		try {
			processBuilder.start();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
