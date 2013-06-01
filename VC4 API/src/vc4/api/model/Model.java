package vc4.api.model;

import java.io.*;
import java.util.*;

public class Model {

	HashMap<String, ModelPart> parts = new HashMap<>();
	ArrayList<ModelPart> parentParts = new ArrayList<>();
	
	public void loadModel(InputStream in) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		String name = "";
		String parent = "";
		ArrayList<String> lp = new ArrayList<>();
		while((line = reader.readLine()) != null){
			line = line.trim();
			if(line.endsWith(":")){
				if(!name.isEmpty()){
					ModelPart part = new ModelPart();
					part.load(lp.toArray(new String[lp.size()]));
					parts.put(name, part);
					if(!parent.isEmpty()){
						parts.get(parent).addChild(part);
					}
				}
				String[] parts = line.substring(0, line.length() - 1).split(" ");
				name = parts[0];
				if(parts.length > 2 && parts[1].equals("super")){
					parent = parts[2];
				} else parent = "";
				lp = new ArrayList<>();
			} else lp.add(line);
		}
		if(!name.isEmpty()){
			ModelPart part = new ModelPart();
			part.load(lp.toArray(new String[lp.size()]));
			parts.put(name, part);
			if(!parent.isEmpty()){
				parts.get(parent).addChild(part);
			}
		}
	}
}
