package vc4.vanilla.npc;

import java.io.*;
import java.util.*;

import vc4.api.logging.Logger;

public class NameFile {

	private static ArrayList<String> vowels = new ArrayList<>(10);
	private static ArrayList<String> consonants = new ArrayList<>(50);
	
	static{
		vowels.add("a");
		vowels.add("e");
		vowels.add("i");
		vowels.add("o");
		vowels.add("u");
		//vowels.add("y");
		for(int d = 97; d < 123; ++d){
			char r = (char)d;
			boolean b = false;
			for(int f = 0; f < vowels.size(); ++f){
				if(vowels.get(f).equals(Character.toString(r))){
					b = true;
					break;
				}
			}
			if(!b) consonants.add(Character.toString(r));
		}
	}
	
	String[][] parts = new String[127][];
	HashMap<String, NameFormula[]> forumlas = new HashMap<>();
	
	public NameFormula[] getForumlas(String type){
		return forumlas.get(type);
	}
	
	public void load(InputStream input){
		try(@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new InputStreamReader(input))){
			String line;
			int stage = 0;
			while((line = in.readLine()) != null){
				if(line.startsWith("#")) continue;
				if(line.startsWith("@")){
					if(line.startsWith("@definitions")) stage = 1;
					else if(line.startsWith("@names")) stage = 2;
					continue;
				}
				if(stage == 0) throw new IOException("Not in acceptable state. Please use @definitions or @names");
				String ps[] = line.split("=");
				if(ps.length < 2) throw new IOException("Not defining anything");
				if(stage == 1){
					int ind = 0;
					try{
						ind = Integer.parseInt(ps[0]);
					} catch(NumberFormatException e) {
						if(ps[0].length() != 1) throw new IOException("Only char and int parts (0-127 ascii) are accepted: " + ps[0]);
						ind = ps[0].charAt(0);
					}
					if(ind < 0 || ind > 127) throw new IOException("Only char and int parts (0-127 ascii) are accepted: " + ps[0]);
					String cosep[] = ps[1].split(":");
					ArrayList<String> strings = new ArrayList<>();
					for(int z = 0; z < cosep.length; ++z){
						String sects[] = cosep[z].split(",");
						for(int d = 0; d < sects.length; ++d){
							if(sects[d].startsWith("\\")){
								if(sects[d].charAt(1) == 'v'){
									if(z == 0)strings.addAll(vowels);
									else if(z == 1) strings.removeAll(vowels);
								}
								else if(sects[d].charAt(1) == 'c'){
									if(z == 0) strings.addAll(consonants);
									else if(z == 1) strings.removeAll(consonants);
								}
								else if(sects[d].charAt(1) == '@'){
									String ref = sects[d].substring(2);
									int vnd = 0;
									try{
										vnd = Integer.parseInt(ref);
									} catch(NumberFormatException e) {
										if(ref.length() != 1) throw new IOException("Only char and int parts (0-127 ascii) are accepted: " + ref);
										vnd = ref.charAt(0);
									}
									if(vnd < 0 || vnd > 127) throw new IOException("Only char and int parts (0-127 ascii) are accepted: " + ref);
									String[] load = parts[vnd];
									for(int q = 0; q < load.length; ++q){
										if(z == 0) strings.add(load[q]);
										else if(z == 1) strings.remove(load[q]);
									}
								}
								else throw new IOException("No group with that code: " + sects[d]);
							} else {
								if(z == 0) strings.add(sects[d]);
								else if(z == 1) strings.remove(sects[d]);
							}
						}
					}
					parts[ind] = strings.toArray(new String[strings.size()]);
				} else if(stage == 2){
					String sects[] = ps[1].split(",");
					ArrayList<NameFormula> strings = new ArrayList<>();
					for(int d = 0; d < sects.length; ++d){
						strings.add(new NameFormula(sects[d]));
					}
					forumlas.put(ps[0], strings.toArray(new NameFormula[strings.size()]));

				}
			}
			in.close();
		} catch (IOException e) {
			Logger.getLogger(NameFile.class).warning("Failed to read name file", e);
		}
	}
	
	public String generateName(String type, Random rand){
		NameFormula[] forumlas = this.forumlas.get(type);
		if(forumlas == null) return null;
		long weight = 0;
		for(int d = 0; d < forumlas.length; ++d) weight += forumlas[d].weight;
		weight = rand.nextLong() % weight;
		String form = null;
		for(int d = 0; d < forumlas.length; ++d){
			weight -= forumlas[d].weight;
			if(weight <= 0){
				form = forumlas[d].formula;
				break;
			}
		}
		if(form == null) return null;
		StringBuilder out = new StringBuilder();
		for(int d = 0; d < form.length(); ++d){
			String[] chose = parts[form.charAt(d)];
			out.append(chose[rand.nextInt(chose.length)]);
		}
		return out.toString();
	}
	
	public String generateName(NameFormula[] forumlas, Random rand){
		if(forumlas == null) return null;
		long weight = 0;
		for(int d = 0; d < forumlas.length; ++d) weight += forumlas[d].weight;
		weight = rand.nextLong() % weight;
		String form = null;
		for(int d = 0; d < forumlas.length; ++d){
			weight -= forumlas[d].weight;
			if(weight <= 0){
				form = forumlas[d].formula;
				break;
			}
		}
		if(form == null) return null;
		StringBuilder out = new StringBuilder();
		for(int d = 0; d < form.length(); ++d){
			String[] chose = parts[form.charAt(d)];
			out.append(chose[rand.nextInt(chose.length)]);
		}
		return out.toString();
	}
	
	
	
	public class NameFormula{
		String formula;
		
		public String getFormula() {
			return formula;
		}
		
		public long getWeight() {
			return weight;
		}
		
		long weight = 1;
		public NameFormula(String formula) {
			super();
			this.formula = formula;
			for(int d = 0; d < formula.length(); ++d){
				weight *= parts[formula.charAt(d)].length;
			}
		}
		
	}
}
