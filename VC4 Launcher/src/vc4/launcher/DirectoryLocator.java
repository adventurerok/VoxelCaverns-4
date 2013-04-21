package vc4.launcher;

import java.io.File;


public class DirectoryLocator {

	private static String path = null;
	
	/**
	 * Finds the path
	 * @return The path, without a '/' on the end
	 */
	public static String getPath(){
		if(path != null) return path;
		path = getAppDir("_vc4").getPath();
		return path;
	}
	
	public static File getAppDir(String app){
		OS o = OS.getOs();
		String userDir = System.getProperty("user.home", ".");
		File file = null;
		app = app + '/';
		switch(o){
		case LINUX:
		case SOLARIS:
			file = new File(userDir, app + '/');
            break;
		case WINDOWS:
			String appData = System.getenv("APPDATA");
            if (appData != null) file = new File(appData, app);
            else file = new File(userDir, app);
            break;
		case MAC:
			//Not supposed to be app/
			 file = new File(userDir, (new StringBuilder()).append("Library/Application Support/").append(app.substring(0, app.length() - 1)).toString());
             break;
		default:
			file = new File(userDir, app);
            break;
		}
		
		if (file == null || (!file.exists() && !file.mkdirs())) throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        else return file;
	}
}
