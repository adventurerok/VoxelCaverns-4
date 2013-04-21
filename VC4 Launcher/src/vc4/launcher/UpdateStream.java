package vc4.launcher;

import java.util.ArrayList;
import java.util.Map.Entry;

public class UpdateStream {

	private Version latest;
	private ArrayList<Version> versions;
	
	public Version getLatest() {
		return latest;
	}
	
	public ArrayList<Version> getVersions() {
		return versions;
	}
	
	public void load(YamlMap map, String latest){
		for(Entry<Object, Object> o : map.getBaseMap().entrySet()){
			String name = o.getKey().toString();
			Version v = Version.load(map.getSubMap(name), name);
			versions.add(v);
			if(this.latest == null && name.equals("latest")) this.latest = v;
		}
	}
}
