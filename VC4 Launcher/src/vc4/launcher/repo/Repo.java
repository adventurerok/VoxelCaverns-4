package vc4.launcher.repo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import vc4.launcher.util.YamlMap;

public class Repo {

	private HashMap<String, Package> namedPackages = new HashMap<>();
	private ArrayList<Package> packages = new ArrayList<>();
	private String repoRoot;
	private String name;
	private boolean canDisable = true;
	private List<String> textFiles = new ArrayList<>();

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public List<String> getTextFiles() {
		return textFiles;
	}

	public Package getPackage(String name) {
		return namedPackages.get(name);
	}

	public void setCanDisable(boolean canDisable) {
		this.canDisable = canDisable;
	}

	public boolean canDisable() {
		return canDisable;
	}

	public void refresh() {
		for (Package p : packages) {
			try {
				p.refresh();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((repoRoot == null) ? 0 : repoRoot.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Repo other = (Repo) obj;
		if (repoRoot == null) {
			if (other.repoRoot != null) return false;
		} else if (!repoRoot.equals(other.repoRoot)) return false;
		return true;
	}

	public boolean loadInfo(URL url) {
		try {
			System.out.println("Loading repo info for: " + url.toString());
			String s = url.toString();
			if (!s.endsWith("/")) s = s + "/";
			repoRoot = s;
			s = s + "repo.yml";
			url = new URL(s);
			YamlMap map = new YamlMap(url.openStream());
			load(map);
			System.out.println("Loaded repo info for: " + url.toString());
			return true;
		} catch (Exception e) {
			System.out.println("Failed to download repo info for: " + url.toString());
			return false;
		}
	}

	public void autoUpdate() {
		System.out.println("Checking for updates in repo: " + name);
		for (Package p : packages) {
			p.autoUpdate();
		}
	}

	public String getRepoRoot() {
		return repoRoot;
	}

	@SuppressWarnings("unchecked")
	public void load(YamlMap map) throws IOException {
		YamlMap info = map.getSubMap("repo");
		name = info.getString("name");
		YamlMap packages = map.getSubMap("packages");
		Iterator<YamlMap> it = packages.getSubMapsIterator();
		while (it.hasNext()) {
			YamlMap m = it.next();
			String folder = m.getString("folder");
			Package p = new Package();
			URL url = new URL(repoRoot + folder + "/");
			p.loadInfo(url);
			namedPackages.put(p.getName(), p);
			this.packages.add(p);
		}
		if (map.hasKey("text")) {
			textFiles = (List<String>) map.getJavaList("text");
		}

	}

	public URL getUrl(String file) {
		try {
			return new URL(repoRoot + file);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid file");
		}
	}

	public String getName() {
		return name;
	}

}
