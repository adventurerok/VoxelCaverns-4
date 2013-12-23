package vc4.server.user;

public class UserfileLookup {
	
	private static char[] hex = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	private static String[] hexS = new String[16];
	
	static{
		for(int d = 0;d < hex.length; ++d){
			hexS[d] = Character.toString(hex[d]);
		}
	}
	
	public static String hex(byte b){
		return hexS[b >> 4] + hexS[b & 15];
	}
	
	private static StringBuilder hex(StringBuilder builder, byte b){
		return builder.append(hex[b >> 4]).append(hex[b & 15]);
	}
	
	public static String getUserfilePath(byte[] suid){
		if(suid == null || suid.length != 16) throw new RuntimeException("SUID must be 128-bit integer");
		StringBuilder result = new StringBuilder();
		hex(result, suid[0]).append('/');
		for(int d = 1; d < 16; ++d) hex(result, suid[d]);
		return result.toString();
	}
	
	public static String getUserfileHex(byte[] suid){
		if(suid == null || suid.length != 16) throw new RuntimeException("SUID must be 128-bit integer");
		StringBuilder result = new StringBuilder();
		for(int d = 0; d < 16; ++d) hex(result, suid[d]);
		return result.toString();
	}

}
