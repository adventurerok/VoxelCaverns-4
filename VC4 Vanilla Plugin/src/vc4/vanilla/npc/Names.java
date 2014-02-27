package vc4.vanilla.npc;

import java.io.InputStream;
import java.util.Random;

import vc4.vanilla.npc.NameFile.NameFormula;

public class Names {

	private static NameFile file;
	private static NameFormula[] first;
	private static NameFormula[] last;
	private static NameFormula[] village;

	public static void load() {
		InputStream in = Names.class.getClassLoader().getResourceAsStream("vc4/vanilla/resources/text/names.txt");
		file = new NameFile();
		file.load(in);
		first = file.getForumlas("first");
		last = file.getForumlas("last");
		village = file.getForumlas("village");
	}

	public static String generateFirstName(Random rand) {
		return file.generateName(first, rand);
	}

	public static String generateLastName(Random rand) {
		return file.generateName(last, rand);
	}

	public static String generateVillageName(Random rand) {
		return file.generateName(village, rand);
	}

}