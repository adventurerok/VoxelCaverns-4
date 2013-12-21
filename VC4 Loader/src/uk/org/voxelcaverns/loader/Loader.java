package uk.org.voxelcaverns.loader;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.UIManager;


/**
 * Loads the launcher, and updates it
 * @author paul
 *
 */
public class Loader {

	
	public static void main(String[] args) throws Exception {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		checkUpdates();
		runLauncher();
	}
	
	public static void checkUpdates() throws Exception{
		String path = "http://voxelcaverns.org.uk/repo/launcher/";
		URL latest = new URL(path + "latest.txt");
		BufferedReader bred = new BufferedReader(new InputStreamReader(latest.openStream()));
		int latestVersion = Integer.parseInt(bred.readLine().trim());
		bred.close();
		
		int yVers = getVersion();
		System.out.println("Latest: " + latestVersion + ", my: " + yVers);
		if(latestVersion <= yVers) return;
		System.out.println("Update found");
		URL newer = new URL(path + "Launcher.jar");
		ReadableByteChannel rbc = Channels.newChannel(newer.openStream());
		new File(DirectoryLocator.getPath() + "/bin/").mkdirs();
		FileOutputStream out = new FileOutputStream(DirectoryLocator.getPath() + "/bin/VC4-Launcher.jar");
		out.getChannel().transferFrom(rbc, 0, 1 << 28);
		out.close();
		
		File check = new File(DirectoryLocator.getPath() +  "/launcher/version");
		check.getParentFile().mkdirs();
		try(DataOutputStream fout = new DataOutputStream(new FileOutputStream(check))){
			fout.writeInt(latestVersion);
		}
		
	}
	
	public static int getVersion() throws Exception{
		File check = new File(DirectoryLocator.getPath() +  "/launcher/version");
		if(!check.exists()) return 0;
		try(DataInputStream bred = new DataInputStream(new FileInputStream(check))){
			return bred.readInt();
		}
		
	}
	
	public static void runLauncher() throws Exception{
		String myLoc = Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
		String run = DirectoryLocator.getPath() + "/bin/VC4-Launcher.jar";
		String separator = System.getProperty("file.separator");
	    String jvm = System.getProperty("java.home") + separator + "bin" + separator + "javaw";
	    String memory = "64M";
	    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { jvm, "-Xmx" + memory, "-Xms" + memory, "-cp", run, "vc4.launcher.Loader", "loader:" + myLoc });
	    processBuilder.redirectOutput(Redirect.INHERIT);
    	processBuilder.redirectError(Redirect.INHERIT);
		try {
			processBuilder.start();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
