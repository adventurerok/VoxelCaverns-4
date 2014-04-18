package vc4.api.io;

import java.util.Map.Entry;

import vc4.api.map.BidiMap;

public class DictionaryInfo {

	private BidiMap<String, Integer> def = new BidiMap<>();
	private int min, max = Integer.MAX_VALUE;
	private String name = "unnameddicto101";
	
	
	public void prepareDictionary(Dictionary dict){
		dict.setMin(min).setMax(max);
		for(Entry<String, Integer> e : def.entrySet()){
			dict.put(e.getKey(), e.getValue());
		}
	}
	
	public boolean containsKey(String key){
		return def.containsKey(key);
	}
	
	public String getName() {
		return name;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getMin() {
		return min;
	}
	
	public DictionaryInfo(String name, int min) {
		super();
		this.name = name;
		this.min = min;
	}


	public DictionaryInfo(String name, int min, int max) {
		super();
		this.name = name;
		this.min = min;
		this.max = max;
	}
	
	
	public DictionaryInfo put(String name, int id){
		def.put(name, id);
		return this;
	}
	
	
	
}
