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
	
	public static byte[] parseSUID(String hex){
		byte[] result = new byte[16];
		byte current = 0;
		for(int d = 0; d < 32; ++d){
			if((d % 2) == 0) current = (byte) (toHex(hex.charAt(d)) << 4);
			else {
				current += toHex(hex.charAt(d));
				result[d / 2] = current;
			}
		}
		return result;
	}
	
	private static byte toHex(char nibble){
		nibble = Character.toLowerCase(nibble);
		if(nibble > 47 && nibble < 58) return (byte) (nibble - 48);
		else return (byte) (nibble - 87);
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
