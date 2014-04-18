/**
 * 
 */
package vc4.api.plugin;

import java.util.*;

import vc4.api.cmd.*;
import vc4.api.io.DictionaryInfo;
import vc4.api.logging.Logger;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public abstract class Plugin {

	private PluginInfoFile infoFile;
	HashMap<String, ExecutableCommand> commands = new HashMap<>();
	ArrayList<ExecutableCommand> cmdList = new ArrayList<>();

	public ArrayList<ExecutableCommand> getSortedCommands() {
		return cmdList;
	}

	public HashMap<String, ExecutableCommand> getExecutableCommands() {
		return commands;
	}

	public void registerCommand(CommandInfo info, CommandHandler handler) {
		commands.put(info.getName(), new ExecutableCommand(info, handler));
		cmdList.add(new ExecutableCommand(info, handler));
		for (String s : info.getAliases())
			commands.put(s, new ExecutableCommand(info, handler));
		Collections.sort(cmdList);
	}

	public Logger getLogger() {
		return Logger.getLogger(getPluginInfoFile().getName());
	}

	public PluginInfoFile getPluginInfoFile() {
		return infoFile;
	}

	/**
	 * Gets the aliases that can be used to execute commands relating to this plugin. The more aliases there are, the more likely there will be one unused by other plugins
	 * 
	 * @return The plugin aliases, in the form of a String array
	 */
	public abstract String[] getAliases();

	/**
	 * 
	 */
	public abstract void onEnable();

	public abstract void onDisable();
	
	public ArrayList<DictionaryInfo> getDictInfos() {
		return null;
	}

	public void loadBlocks(World world) {

	}

	public void loadItems(World world) {

	}

	public void loadBiomes(World world) {

	}

	public void loadCraftingItems(World world) {

	}

	public void loadEntities(World world) {

	}

	public void loadAreas(World world) {

	}

	public void loadTileEntities(World world) {

	}

	public void loadItemEntities(World world) {

	}

	public void loadTraits(World world) {

	}

	public void onWorldLoad(World world) {

	}

	public void onWorldSave(World world) {

	}

	public void loadCraftingRecipes(World world) {
		// TASK Auto-generated method stub

	}

	public void preWorldLoad(World world) {
		// TASK Auto-generated method stub

	}

	public void loadContainers(World world) {
		// TASK Auto-generated method stub

	}
}
