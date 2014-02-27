package vc4.launcher.repo;

import vc4.launcher.util.YamlMap;

public class Runnable {

	String name, launch, path, xmx, xms;

	public Runnable(YamlMap map) {
		name = map.getString("name");
		launch = map.getString("launch");
		path = map.getString("path");
		xmx = map.getString("xmx");
		xms = map.getString("xms");
	}

	public String getName() {
		return name;
	}

	public String getLaunch() {
		return launch;
	}

	public String getPath() {
		return path;
	}

	public String getXmx() {
		return xmx;
	}

	public String getXms() {
		return xms;
	}
}
