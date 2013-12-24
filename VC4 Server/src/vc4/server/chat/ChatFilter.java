package vc4.server.chat;

import java.util.ArrayList;

public class ChatFilter {

	static int filterLevel = 0; //off by default
	
	static ArrayList<String> swearFilter = new ArrayList<>();
	static ArrayList<String> rudeFilter = new ArrayList<>();
	
	private static String obscene = "*******************************************"
			+ "*****************************************************************"
			+ "*****************************************************************"
			+ "*****************************************************************";
	
	static{
		
		loadDefault();
	}
	
	public static boolean filter(String in){
		if(filterLevel < 1) return true;
		in = in.toLowerCase();
		in = replaceNums(in);
		for(String s :  swearFilter){
			if(in.contains(s)) return false;
		}
		if(filterLevel < 2) return true;
		for(String s : rudeFilter){
			if(in.contains(s)) return false;
		}
		return true;
	}
	
	
	
	public static String replace(String in){
		if(filterLevel < 1) return in;
		in = in.toLowerCase();
		in = replaceNums(in);
		for(String s :  swearFilter){
			if(in.contains(s)) in = in.replace(s, obscene.substring(0, s.length()));
		}
		if(filterLevel < 2) return in;
		for(String s : rudeFilter){
			if(in.contains(s)) in = in.replace(s, obscene.substring(0, s.length()));
		}
		return in;
	}
	
	static char[] numReplace = new char[]{'o','i','q','e','h','s','g','l','b','p'};
	
	private static String replaceNums(String in){
		StringBuilder out = new StringBuilder();
		for(int d = 0; d < in.length(); ++d){
			char a = in.charAt(d);
			if(a > 47 && a < 58) a = numReplace[a - 48];
			out.append(a);
		}
		return out.toString(); 
	}
	
	
	public static void loadDefault(){
		rudeFilter.clear();
		swearFilter.clear();
		addSwears("fuck", "shit", "cunt", "penis", "vagina", "twat", " sex");
		addRudes("gay", "lesbien"," homo ","retard", "hell", " tit ", " breast", "titty", "wank", " butt", "poop");
	}
	
	public static void addSwears(String...swears){
		for(String s : swears) addSwear(s);
	}
	
	public static void addRudes(String...rudes){
		for(String s : rudes) addRude(s);
	}
	
	public static void addSwear(String swear){
		swear = swear.trim().toLowerCase();
		if(swearFilter.contains(swear)) return;
		swearFilter.add(swear);
	}
	
	public static void addRude(String rude){
		rude = rude.trim().toLowerCase();
		if(rudeFilter.contains(rude)) return;
		rudeFilter.add(rude);
	}
	
	
}
