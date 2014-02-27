/**
 * 
 */
package vc4.impl.plugin;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import vc4.api.plugin.InvalidInfoFileException;
import vc4.api.plugin.PluginInfoFile;
import vc4.api.yaml.YamlMap;

/**
 * @author paul
 * 
 */
public class ImplPluginInfoFile implements PluginInfoFile {

	private String name, version, description, mainClass, resources;
	private Object[] developers;
	private int intVersion;
	private YamlMap doc;
	private URL jarUrl;

	/**
	 * @return the developers
	 */
	@Override
	public Object[] getDevelopers() {
		return developers;
	}

	@Override
	public URL getJarUrl() {
		return jarUrl;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @return the intVersion
	 */
	@Override
	public int getIntVersion() {
		return intVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.plugin.PluginInfoFile#getYamlDocument()
	 */
	@Override
	public YamlMap getYamlDocument() {
		return null;
	}

	public ImplPluginInfoFile(InputStream in, URL jar) {
		this.jarUrl = jar;
		doc = new YamlMap(in);
		if (!doc.hasKey("name") || !doc.hasKey("version") || !doc.hasKey("intversion") || !doc.hasKey("developers") || !doc.hasKey("main")) { throw new InvalidInfoFileException("Plugin info file missing one or more essential keys (name, version, intversion, developers, main)"); }
		name = doc.getString("name");
		version = doc.getString("version");
		developers = doc.getList("developers");
		mainClass = doc.getString("main");
		try {
			intVersion = doc.getInt("intversion");
		} catch (Exception e) {
			throw new InvalidInfoFileException("intversion must be a 32-bit signed integer");
		}
		if (doc.hasKey("description")) description = doc.getString("description");
		if (doc.hasKey("resources")) resources = doc.getString("resources").replace(".", "/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.plugin.PluginInfoFile#getMainClass()
	 */
	@Override
	public String getMainClass() {
		return mainClass;
	}

	@Override
	public String getResourcePath() {
		return resources;
	}

	@Override
	public URL getResourceURL() {
		try {
			return new URL(jarUrl.toString() + resources);
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
