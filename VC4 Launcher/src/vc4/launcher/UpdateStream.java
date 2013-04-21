package vc4.launcher;

import java.util.ArrayList;
import java.util.Map.Entry;

import vc4.launcher.enumeration.UpdateStreamType;

public class UpdateStream {

	private Version latest;
	private ArrayList<Version> versions = new ArrayList<>();
	UpdateStreamType type;
	
	public Version getLatest() {
		return latest;
	}
	
	public ArrayList<Version> getVersions() {
		return versions;
	}
	
	public void load(YamlMap map, String latest, UpdateStreamType type){
		this.type = type;
		for(Entry<Object, Object> o : map.getBaseMap().entrySet()){
			String name = o.getKey().toString();
			Version v = Version.load(map.getSubMap(name), name);
			v.updateStream = type;
			versions.add(v);
			if(this.latest == null && name.equals(latest)) this.latest = v;
		}
	}
}
