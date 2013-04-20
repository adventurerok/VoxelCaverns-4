/**
 * 
 */
package vc4.api.plugin;

import vc4.api.yaml.YamlMap;

/**
 * @author paul
 *
 */
public interface PluginInfoFile {

	public String getName();
	public String getVersion();
	public int getIntVersion();
	public Object[] getDevelopers();
	public String getDescription();
	public String getMainClass();
	public YamlMap getYamlDocument();
}
