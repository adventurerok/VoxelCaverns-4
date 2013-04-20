/**
 * 
 */
package vc4.api.math;

/**
 * @author paul
 *
 */
public class MathUtils {

	private static float f_ulp = 2 * Math.ulp(0F);
	private static double d_ulp = 2 * Math.ulp(0D);
	
	private static float SIN_TABLE[];
	
	public static final float sin(float angle)
    {
        return SIN_TABLE[(int)(angle * 10430.38F) & 0xffff];
    }

    public static final float cos(float angle)
    {
        return SIN_TABLE[(int)(angle * 10430.38F + 16384F) & 0xffff];
    }
    
    static{
    	SIN_TABLE = new float[0x10000];

        for (int i = 0; i < 0x10000; i++)
        {
            SIN_TABLE[i] = (float)Math.sin((i * Math.PI * 2D) / 65536D);
        }
    }
	
	public static int floor(float f){
		int i = (int)f;
        return f >= i ? i : i - 1;
	}
	
	public static long floor(double d){
		long i = (long)d;
        return d >= (long)d ? i : i - 1;
	}
	
	public static boolean equals(double a, double b){
		return (Math.abs(a-b) < d_ulp);
	}
	
	public static boolean equals(float a, float b){
		return (Math.abs(a-b) < f_ulp);
	}

	/**
	 * @param d
	 */
	public static long round(double d) {
		return floor(d + 0.5);
	}
	
	public static int avg(int n1, int n2) {
		return (n1 + n2) / 2;
	}

	public static int avg(int n1, int n2, int n3) {
		return (n1 + n2 + n3) / 3;
	}

	public static int avg(int n1, int n2, int n3, int n4) {
		return (n1 + n2 + n3 + n4) / 4;
	}

	/**
	 * @param x
	 */
	public static int ceil(float x) {
		int i = (int)x;
        return x <= (int)x ? i : i + 1;
	}

}
