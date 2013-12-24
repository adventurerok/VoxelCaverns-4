package vc4.server.chat;

public class NameFilter {

	char[] allow = "abcdefghijklmnopqrstuvwxyz0123456789_#-".toCharArray();
	
	public boolean filter(String name){
		if(name.length() < 2) return false;
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
