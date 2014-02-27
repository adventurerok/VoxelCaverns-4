package vc4.api.util;

import java.util.Random;

public class SuidUtils {

	private static Random rand = new Random();

	private static char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String[] hexS = new String[16];

	static {
		for (int d = 0; d < hex.length; ++d) {
			hexS[d] = Character.toString(hex[d]);
		}
	}

	public static byte[] parseSuid(String hex) {
		byte[] result = new byte[16];
		byte current = 0;
		for (int d = 0; d < 32; ++d) {
			if ((d % 2) == 0) current = (byte) (toHex(hex.charAt(d)) << 4);
			else {
				current += toHex(hex.charAt(d));
				result[d / 2] = current;
			}
		}
		return result;
	}

	private static byte toHex(char nibble) {
		nibble = Character.toLowerCase(nibble);
		if (nibble > 47 && nibble < 58) return (byte) (nibble - 48);
		else return (byte) (nibble - 87);
	}

	private static StringBuilder hex(StringBuilder builder, byte b) {
		return builder.append(hex[(b & 0xf0) >> 4]).append(hex[b & 0xf]);
	}

	public static String getSuidPath(byte[] suid) {
		if (suid == null || suid.length != 16) throw new RuntimeException("SUID must be 128-bit integer");
		StringBuilder result = new StringBuilder();
		hex(result, suid[0]).append('/');
		for (int d = 1; d < 16; ++d)
			hex(result, suid[d]);
		return result.toString();
	}

	public static String getSuidHex(byte[] suid) {
		if (suid == null || suid.length != 16) throw new RuntimeException("SUID must be 128-bit integer");
		StringBuilder result = new StringBuilder();
		for (int d = 0; d < 16; ++d)
			hex(result, suid[d]);
		return result.toString();
	}

	public static byte[] generateRandomSuid() {
		long nt = System.nanoTime();
		byte[] ntb = longToByte(nt);
		byte rnb[] = new byte[8];
		rand.nextBytes(rnb);
		byte[] res = new byte[16];
		for (int d = 0; d < 15; ++d) {
			if ((d % 2) == 0) res[d] = rnb[d / 2];
			else res[d] = ntb[d / 2];
		}
		return res;
	}

	private static byte[] longToByte(long l) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; ++i) {
			b[i] = (byte) (l >> (8 - i - 1 << 3));
		}
		return b;
	}

}
