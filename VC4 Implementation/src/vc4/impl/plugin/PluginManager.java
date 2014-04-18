/**
 * 
 */
package vc4.impl.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vc4.api.cmd.Command;
import vc4.api.cmd.ExecutableCommand;
import vc4.api.io.DictionaryInfo;
import vc4.api.logging.Logger;
import vc4.api.plugin.Plugin;
import vc4.api.plugin.PluginInfoFile;
import vc4.api.server.User;
import vc4.api.util.DirectoryLocator;
import vc4.api.world.World;
import vc4.impl.world.ImplWorld;

/**
 * @author paul
 * 
 */
public class PluginManager {

	private static HashMap<String, Plugin> loadedPlugins = new HashMap<>();
	private static ArrayList<URL> resourceURLs = new ArrayList<>();
	private static HashMap<String, Plugin> pluginCommands = new HashMap<>();

	public static void handleCommand(Command command) {
		String s = command.getCommand();
		Plugin p = pluginCommands.get(s);
		if (p == null) {
			command.getSender().message("{l:cmd.noplugin," + s + "}");
			return;
		}
		if (command.getArgsLength() == 0) {
			printPluginHelp(p, command.getSender(), 1);
			return;
		}
		if (command.getArgAsInt(0, -11204) != -11204) {
			printPluginHelp(p, command.getSender(), command.getArgAsInt(0, -1));
			return;
		}
		String plugin = s;
		String cmd = command.getArg(0);
		String args[] = Arrays.copyOfRange(command.getArgs(), 1, command.getArgs().length);
		Command plugCmd = new Command(plugin, cmd, args, command.getSender());
		if (p.getExecutableCommands().get(plugCmd.getCommand()) == null) {
			command.getSender().message("{l:cmd.nocommand," + plugCmd.getCommand() + "}");
			return;
		}
		if (!p.getExecutableCommands().get(plugCmd.getCommand()).getInfo().getCommandUsage().check(plugCmd)) return;
		p.getExecutableCommands().get(plugCmd.getCommand()).getHandler().handleCommand(plugCmd);
	}

	public static void printPluginHelp(Plugin plugin, User user, int page) {
		if (plugin.getSortedCommands().size() < 1) {
			user.message("{l:cmd.nocommands," + plugin.getPluginInfoFile().getName() + "}");
			return;
		}
		page -= 1;
		if (page * 8 > plugin.getSortedCommands().size() || page < 0) {
			user.message("{l:cmd.nohelppage," + (page + 1) + "}");
			return;
		}
		user.message("{l:cmd.pluginhelp," + plugin.getPluginInfoFile().getName() + "," + (page + 1) + "," + ((plugin.getSortedCommands().size() + 7) / 8) + "}");
		int start = page * 8;
		for (int d = start; d < start + 8 && d < plugin.getSortedCommands().size(); ++d) {
			ExecutableCommand exc = plugin.getSortedCommands().get(d);
			user.message("- " + exc.getInfo().getName() + " " + exc.getInfo().getUsage() + " : " + exc.getInfo().getDesc());
		}
		if ((page + 1) * 8 <= plugin.getSortedCommands().size()) user.message("{l:cmd.nextpage,/" + plugin.getAliases()[0] + " " + (page + 2) + "}");
	}

	static {
		try {
			// resourceURLs.add(new URL("jar:file:" + DirectoryLocator.getPath() + "/bin/VC4-Resources.zip!/"));
			try {
				String s = PluginManager.class.getClassLoader().getResource("vc4/resources/resources.yml").toString();
				s = s.substring(0, s.lastIndexOf("/"));
				resourceURLs.add(new URL(s));
			} catch (Exception e) {
			}
			String s = PluginManager.class.getClassLoader().getResource("vc4/resources/lang/en_GB.lang").toString();
			for (int d = 0; d < 2; ++d)
				s = s.substring(0, s.lastIndexOf("/"));
			resourceURLs.add(new URL(s));
			resourceURLs.add(new File(DirectoryLocator.getPath() + "/resources/").toURI().toURL());
		} catch (Exception e) {
			Logger.getLogger(PluginManager.class).warning("Exception occured", e);
		}
	}

	public static String getPluginDirectory() {
		return DirectoryLocator.getPath() + "/plugins/";
	}

	public static void loadAndEnablePlugins() {
		loadPlugins(getPluginDirectory());
		enablePlugins();
	}

	public static void disableAndUnloadPlugins() {
		disablePlugins();
		unloadPlugins();
	}

	public static ArrayList<URL> getResourceURLs() {
		return resourceURLs;
	}

	@SuppressWarnings("resource")
	public static void loadPlugins(String pluginFolder) {
		Logger logger = Logger.getLogger("PluginManager");
		File folder = new File(pluginFolder);
		if (!folder.exists()) {
			logger.info("No plugin directory found, creating plugin directory");
			if (!folder.mkdirs()) {
				logger.info("Failed to create plugin directory");
			}
			return;
		}
		File[] jars = folder.listFiles(new JarFileFilter());
		if (jars.length < 1) {
			logger.info("No plugins found");
			return;
		}
		ArrayList<URL> pluginLoaderUrls = new ArrayList<>();
		ArrayList<LoadingPlugin> loading = new ArrayList<>();
		for (int d = 0; d < jars.length; ++d) {
			String pName = jars[d].getName();
			pName = pName.substring(0, pName.length() - 4);
			try {
				resourceURLs.add(new File(DirectoryLocator.getPath() + "/plugins/" + pName + "/").toURI().toURL());
			} catch (MalformedURLException e1) {
				Logger.getLogger(PluginManager.class).warning("Exception occured", e1);
			}
			try {
				JarFile jar = new JarFile(jars[d]);
				JarEntry desc = jar.getJarEntry("plugin.yml");
				if (desc == null) {
					logger.warning("Failed to find plugin desc for " + pName);
					continue;
				}
				PluginInfoFile infoFile = new ImplPluginInfoFile(jar.getInputStream(desc), new URL("jar:file:" + jars[d].getPath().replace('\\', '/') + "!/"));
				if (infoFile.getResourcePath() != null) resourceURLs.add(infoFile.getResourceURL());
				pName = infoFile.getName();
				JarEntry mcEntry = jar.getJarEntry(infoFile.getMainClass().replace(".", "/") + ".class");
				if (mcEntry == null) {
					logger.warning("Invalid main class \"" + infoFile.getMainClass() + "\" for plugin " + pName);
					continue;
				}
				pluginLoaderUrls.add(new URL("jar:file:" + jars[d].getPath().replace('\\', '/') + "!/"));
				loading.add(new LoadingPlugin(infoFile));
				jar.close();
			} catch (Exception e) {
				logger.warning("Failed to identify plugin: " + pName, e);
			}
		}
		URLClassLoader pluginLoader = new URLClassLoader(pluginLoaderUrls.toArray(new URL[pluginLoaderUrls.size()]), PluginManager.class.getClassLoader());
		for (int d = 0; d < loading.size(); ++d) {
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
				Logger.getLogger(PluginManager.class).warning("Failed to load plugin: " + plug.infoFile.getName(), e);
			}
		}
	}

	public static void enablePlugins() {
		Logger logger = Logger.getLogger("PluginManager");
		for (Plugin p : loadedPlugins.values()) {
			logger.info("Enabling plugin " + p.getPluginInfoFile().getName() + " version " + p.getPluginInfoFile().getVersion());
			for (String s : p.getAliases())
				pluginCommands.put(s, p);
			p.onEnable();
		}
	}

	public static void disablePlugins() {
		for (Plugin p : loadedPlugins.values()) {
			p.onDisable();
			pluginCommands.values().remove(p);
		}

	}

	public static void unloadPlugins() {
		for (Plugin p : loadedPlugins.values()) {
			loadedPlugins.remove(p.getPluginInfoFile().getName());
			p = null;
		}
	}

	private static class JarFileFilter implements FilenameFilter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}

	}

	private static class LoadingPlugin {
		PluginInfoFile infoFile;

		private LoadingPlugin(PluginInfoFile infoFile) {
			super();
			this.infoFile = infoFile;
		}

	}
	
	public static void loadDicts(ImplWorld world){
		ArrayList<DictionaryInfo> all = new ArrayList<>();
		ArrayList<DictionaryInfo> curr;
		for(Plugin p : loadedPlugins.values()){
			curr = p.getDictInfos();
			if(curr == null) continue;
			all.addAll(curr);
		}
		for(DictionaryInfo i : all){
			world.registerDictionaryInfo(i);
		}
	}

	public static void onWorldLoad(World world) {
		for (Plugin p : loadedPlugins.values())
			p.preWorldLoad(world);
		for (Plugin p : loadedPlugins.values())
			p.loadCraftingItems(world);
		for (Plugin p : loadedPlugins.values())
			p.loadBlocks(world);
		for (Plugin p : loadedPlugins.values())
			p.loadItems(world);
		for (Plugin p : loadedPlugins.values())
			p.loadCraftingRecipes(world);
		for (Plugin p : loadedPlugins.values())
			p.loadContainers(world);
		for (Plugin p : loadedPlugins.values())
			p.loadTraits(world);
		for (Plugin p : loadedPlugins.values())
			p.loadEntities(world);
		for (Plugin p : loadedPlugins.values())
			p.loadItemEntities(world);
		for (Plugin p : loadedPlugins.values())
			p.loadTileEntities(world);
		for (Plugin p : loadedPlugins.values())
			p.loadBiomes(world);
		for (Plugin p : loadedPlugins.values())
			p.loadAreas(world);
		for (Plugin p : loadedPlugins.values())
			p.onWorldLoad(world);

	}

	public static void onWorldSave(World world) {
		for (Plugin p : loadedPlugins.values())
			p.onWorldSave(world);
	}
}
