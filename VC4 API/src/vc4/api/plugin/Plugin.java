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
	
	public Logger getLogger(){
		return Logger.getLogger(getPluginInfoFile().getName());
	}
	
	public PluginInfoFile getPluginInfoFile(){
		return infoFile;
	}

	/**
	 * 
	 */
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public abstract void onWorldLoad(World world);
}
