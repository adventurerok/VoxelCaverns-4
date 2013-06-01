package vc4.impl;

import java.io.*;
import java.util.Map.Entry;

import vc4.api.logging.Logger;
import vc4.api.map.BidiMap;

public class Dictionary {

	private BidiMap<String, Integer> dict = new BidiMap<>();
	private int min, max = Integer.MAX_VALUE;
	
	public Dictionary() {
		
	}
	
	
	
	public Dictionary(int min) {
		super();
		this.min = min;
	}



	public Dictionary(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}



	public int get(String name){
		Integer i = dict.get(name);
		if (i == null) {
			i = nextEmpty();
			dict.put(name, i);
		}
		return i.intValue();
	}
	
	public Dictionary put(String name, int id){
		dict.put(name, id);
		return this;
	}
	
	public String getName(int id){
		return dict.getKey(id);
	}
	
	private int nextEmpty(){
		for(int d = min; d < max; ++d){
			if(!dict.containsValue(d)) return d;
		}
		throw new RuntimeException("Dictionary full");
	}
	
	public void save(OutputStream o){
		PrintStream out = new PrintStream(o);
		out.println("#VoxelCaverns dictionary file");
		for(Entry<String, Integer> e : dict.entrySet()){
			if(e.getValue() < min || e.getValue() >= max) continue;
			out.println(e.getKey() + "=" + e.getValue());
		}
		out.close();
	}
	
	public void load(InputStream i){
		try{
			BufferedReader r = new BufferedReader(new InputStreamReader(i));
			String line;
			while((line = r.readLine()) != null){
				line = line.trim();
				if(line.startsWith("#")) continue;
				String[] parts = line.split("=");
				int ina = Integer.parseInt(parts[1]);
				dict.put(parts[0], ina);
			}
		} catch(Exception e){
			Logger.getLogger("VC4").info("Corrupt dictionary file found");
		}
	}
}
