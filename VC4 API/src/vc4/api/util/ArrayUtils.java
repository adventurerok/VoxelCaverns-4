package vc4.api.util;

/**
 * @author Paul Durbaba
 * 
 */
public class ArrayUtils {

	public static float[] toPrimatives(Float in[]) {
		float result[] = new float[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			result[dofor] = in[dofor] != null ? in[dofor].floatValue() : 0;
		}
		return result;
	}

	public static long[] toPrimatives(Long in[]) {
		long result[] = new long[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			result[dofor] = in[dofor] != null ? in[dofor].longValue() : 0;
		}
		return result;
	}

	public static int[] toPrimatives(Integer in[]) {
		int result[] = new int[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			if (in[dofor] != null) result[dofor] = in[dofor].intValue();
			else result[dofor] = 0;
		}
		return result;
	}

	public static short[] toPrimatives(Short in[]) {
		short result[] = new short[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			if (in[dofor] != null) result[dofor] = in[dofor].shortValue();
			else result[dofor] = 0;
		}
		return result;
	}

	public static double[] toPrimatives(Double in[]) {
		double result[] = new double[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			if (in[dofor] != null) result[dofor] = in[dofor].doubleValue();
			else result[dofor] = 0;
		}
		return result;
	}

	public static float[] toFloatPrimatives(Double in[]) {
		float result[] = new float[in.length];
		for (int dofor = 0; dofor < in.length; dofor++) {
			if (in[dofor] != null) result[dofor] = (float) ((double) in[dofor]);
			else result[dofor] = 0;
		}
		return result;
	}
}
