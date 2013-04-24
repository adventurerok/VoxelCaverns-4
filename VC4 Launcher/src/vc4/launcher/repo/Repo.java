package vc4.launcher.repo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import vc4.launcher.util.YamlMap;

public class Repo {

	private HashMap<String, Package> namedPackages = new HashMap<>();
	private ArrayList<Package> packages = new ArrayList<>();
	private String repoRoot;
	private String name;
	private boolean canDisable = true;
	
	public ArrayList<Package> getPackages() {
		return packages;
	}
	
	public Package getPackage(String name){
		return namedPackages.get(name);
	}
	
	public void setCanDisable(boolean canDisable) {
		this.canDisable = canDisable;
	}
	
	public boolean canDisable() {
		return canDisable;
	}
	
	public void refresh(){
		for(Package p : packages){
			try{
				p.refresh();
			} catch(IOException e){
			}
		}
	}
	
	public void loadInfo(URL url) throws IOException{
		String s = url.toString();
		if (!s.endsWith("/")) s = s + "/";
		repoRoot = s;
		s = s + "repo.yml";
		url = new URL(s);
		YamlMap map = new YamlMap(url.openStream());
		load(map);
	}
	
	public void autoUpdate(){
		for(Package p : packages){
			p.autoUpdate();
		}
	}
	
	public String getRepoRoot() {
		return repoRoot;
	}

	public void load(YamlMap map) throws IOException {
		YamlMap info = map.getSubMap("repo");
		name = info.getString("name");
		YamlMap packages = map.getSubMap("packages");
		Iterator<YamlMap> it = packages.getSubMapsIterator();
		while(it.hasNext()){
			YamlMap m = it.next();
			String folder = m.getString("folder");
			Package p = new Package();
			URL url = new URL(repoRoot + folder + "/");
			p.loadInfo(url);
			namedPackages.put(p.getName(), p);
			this.packages.add(p);
		}
		
	}
	
	public String getName() {
		return name;
	}
	
}
