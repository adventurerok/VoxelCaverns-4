package vc4.server.chat;

public class NameFilter {

	static char[] allow = "abcdefghijklmnopqrstuvwxyz0123456789_#-".toCharArray();
	
	public static boolean filter(String name){
		if(name.length() < 2) return false;
		name = name.toLowerCase();
		if(name.charAt(0) < 58 && name.charAt(0) > 47) return false;
		for(int d = 0; d <  name.length(); ++d){
			char s = name.charAt(d);
			int i;
			for(i = 0; i < allow.length;){
				if(allow[i] == s) break;
				++i;
			}
			if(i == allow.length) return false;
		}
		return ChatFilter.filter(name);
	}
}
