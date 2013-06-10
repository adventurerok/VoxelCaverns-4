package vc4.vanilla.npc;

import java.io.*;
import java.util.Random;

import vc4.api.logging.Logger;

public class VillageNames {

	private static Names firstNames;
	private static Names lastNames;
	
	
	public static void load(){
		InputStream in = VillageNames.class.getClassLoader().getResourceAsStream("vc4/vanilla/resources/text/villagenames.txt");
		try(BufferedReader read = new BufferedReader(new InputStreamReader(in))){
			String[] lines = new String[7];
			for(int d = 0; d < 7; ++d){
				lines[d] = read.readLine().trim();
			}
			load(lines);
		} catch (IOException e) {
			Logger.getLogger(VillageNames.class).warning("Error while reading village Names", e);
		}
	}

	private static void load(String[] lines) {
		String[] firstStart = lines[0].split("[\\s]");
		String[] firstMiddle = lines[1].split("[\\s]");
		String[] firstEnd = lines[2].split("[\\s]");
		String[] lastStart = lines[4].split("[\\s]");
		String[] lastMiddle = lines[5].split("[\\s]");
		String[] lastEnd = lines[6].split("[\\s]");
		firstNames = new Names(firstStart, firstMiddle, firstEnd);
		lastNames = new Names(lastStart, lastMiddle, lastEnd);
		Logger.getLogger("VC4").info(firstNames.maxNames() + " first names, " + (lastNames.maxNames() + 1) + " last names, total " + (firstNames.maxNames() * (lastNames.maxNames() + 1)) + " possible names");
	}
	
	public static String getRandomFirst(Random rand){
		return firstNames.getRandomName(rand);
	}
	
	public static String getRandomLast(Random rand){
		if(rand.nextInt(3) == 0) return lastNames.getRandomName(rand);
		else return "";
	}
}
