package vc4.launcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Repo {

	private HashMap<String, Package> namedPackages = new HashMap<>();
	private ArrayList<Package> packages = new ArrayList<>();
	private String repoRoot;
	private String name;
	
	public ArrayList<Package> getPackages() {
		return packages;
	}
	
	public Package getPackage(String name){
		return namedPackages.get(name);
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
		s = s + "package.yml";
		url = new URL(s);
		YamlMap map = new YamlMap(url.openStream());
		load(map);
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
		}
		
	}
	
	public String getName() {
		return name;
	}
	
}
