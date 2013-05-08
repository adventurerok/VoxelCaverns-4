/**
 * 
 */
package vc4.impl.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vc4.api.logging.Logger;
import vc4.api.plugin.*;
import vc4.api.util.DirectoryLocator;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class PluginLoader {

	private static HashMap<String, Plugin> loadedPlugins = new HashMap<>();
	private static ArrayList<URL> resourceURLs = new ArrayList<>();
	private static ArrayList<URL> externalResources = new ArrayList<>();
	
	static{
		try {
			//resourceURLs.add(new URL("jar:file:" + DirectoryLocator.getPath() + "/bin/VC4-Resources.zip!/"));
			String s = PluginLoader.class.getClassLoader().getResource("vc4/resources/resources.yml").toString();
			s = s.substring(0, s.lastIndexOf("/"));
			resourceURLs.add(new URL(s));
			s = PluginLoader.class.getClassLoader().getResource("vc4/resources/lang/en_GB.lang").toString();
			for(int d = 0; d < 2; ++d) s = s.substring(0, s.lastIndexOf("/"));
			resourceURLs.add(new URL(s));
			externalResources.add(new File(DirectoryLocator.getPath() + "/resources/").toURI().toURL());
		} catch (MalformedURLException e) {
			Logger.getLogger(PluginLoader.class).warning("Exception occured", e);
		}
	}
	
	public static String getPluginDirectory(){
		return DirectoryLocator.getPath() + "/plugins/";
	}
	
	public static void loadAndEnablePlugins(){
		loadPlugins(getPluginDirectory());
		enablePlugins();
	}
	
	public static void disableAndUnloadPlugins(){
		disablePlugins();
		unloadPlugins();
	}
	
	public static ArrayList<URL> getResourceURLs() {
		return resourceURLs;
	}
	
	@SuppressWarnings("resource")
	public static void loadPlugins(String pluginFolder){
		Logger logger = Logger.getLogger("PluginManager");
		File folder = new File(pluginFolder);
		if(!folder.exists()){
			logger.info("No plugin directory found, creating plugin directory");
			if(!folder.mkdirs()){
				logger.info("Failed to create plugin directory");
			}
			return;
		}
		File[] jars = folder.listFiles(new JarFileFilter());
		if(jars.length < 1){
			logger.info("No plugins found");
			return;
		}
		ArrayList<URL> pluginLoaderUrls = new ArrayList<>();
		ArrayList<LoadingPlugin> loading = new ArrayList<>();
		for(int d = 0; d < jars.length; ++d){
			String pName = jars[d].getName();
			try {
				externalResources.add(new File(DirectoryLocator.getPath() + "/plugins/" + pName + "/").toURI().toURL());
			} catch (MalformedURLException e1) {
				Logger.getLogger(PluginLoader.class).warning("Exception occured", e1);
			}
			pName = pName.substring(0, pName.length() - 4);
			try{
				JarFile jar = new JarFile(jars[d]);
				JarEntry desc = jar.getJarEntry("plugin.yml");
				if(desc == null){
					logger.warning("Failed to find plugin desc for " + pName);
					continue;
				}
				PluginInfoFile infoFile = new ImplPluginInfoFile(jar.getInputStream(desc), new URL("jar:file:" + jars[d].getPath().replace('\\', '/') + "!/"));
				if(infoFile.getResourcePath() != null) resourceURLs.add(infoFile.getResourceURL());
				pName = infoFile.getName();
				JarEntry mcEntry = jar.getJarEntry(infoFile.getMainClass().replace(".", "/") + ".class");
				if(mcEntry == null){
					logger.warning("Invalid main class \"" + infoFile.getMainClass() + "\" for plugin " + pName);
					continue;
				}
				pluginLoaderUrls.add(new URL("jar:file:" + jars[d].getPath().replace('\\', '/') + "!/"));
				loading.add(new LoadingPlugin(infoFile));
				jar.close();
			} catch(Exception e){
				logger.warning("Failed to identify plugin: " + pName, e);
			}
		}
		URLClassLoader pluginLoader = new URLClassLoader(pluginLoaderUrls.toArray(new URL[pluginLoaderUrls.size()]), PluginLoader.class.getClassLoader());
		for(int d = 0; d < loading.size(); ++d){
			LoadingPlugin plug = loading.get(d);
			try {
				Class<?> question = pluginLoader.loadClass(plug.infoFile.getMainClass());
				Class<? extends Plugin> pluginClass = question.asSubclass(Plugin.class);
				Plugin pluginInstance = pluginClass.newInstance();
				Field infoField = Plugin.class.getDeclaredField("infoFile");
				infoField.setAccessible(true);
				infoField.set(pluginInstance, plug.infoFile);
				loadedPlugins.put(plug.infoFile.getName(), pluginInstance);
			} catch (Exception e) {
				Logger.getLogger(PluginLoader.class).warning("Failed to load plugin: " + plug.infoFile.getName(), e);
			}
		}
	}
	
	public static void enablePlugins(){
		Logger logger = Logger.getLogger("PluginManager");
		for(Plugin p : loadedPlugins.values()){
			logger.info("Enabling plugin " + p.getPluginInfoFile().getName() + " version " + p.getPluginInfoFile().getVersion());
			p.onEnable();
		}
	}
	
	public static void disablePlugins(){
		for(Plugin p : loadedPlugins.values()){
			p.onDisable();
		}
	}
	
	
	public static void unloadPlugins(){
		for(Plugin p : loadedPlugins.values()){
			loadedPlugins.remove(p.getPluginInfoFile().getName());
			p = null;
		}
	}
	
	private static class JarFileFilter implements FilenameFilter{

		/* (non-Javadoc)
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}
		
	}
	
	private static class LoadingPlugin{
		PluginInfoFile infoFile;
		private LoadingPlugin(PluginInfoFile infoFile) {
			super();
			this.infoFile = infoFile;
		}
		
		
	}

	public static void onWorldLoad(World world) {
		for(Plugin p : loadedPlugins.values()){
			p.onWorldLoad(world);
		}
		
	}
}
