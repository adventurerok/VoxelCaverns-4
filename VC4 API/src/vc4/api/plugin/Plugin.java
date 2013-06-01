/**
 * 
 */
package vc4.api.plugin;

import vc4.api.logging.Logger;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public abstract class Plugin {

	private PluginInfoFile infoFile;

	public Logger getLogger() {
		return Logger.getLogger(getPluginInfoFile().getName());
	}

	public PluginInfoFile getPluginInfoFile() {
		return infoFile;
	}

	/**
	 * 
	 */
	public abstract void onEnable();

	public abstract void onDisable();

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

	public void loadTileEntities(World world) {

	}

	public void loadItemEntities(World world) {

	}

	public void loadTraits(World world) {

	}

	public void onWorldLoad(World world) {

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
