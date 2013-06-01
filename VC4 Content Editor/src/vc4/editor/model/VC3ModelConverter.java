package vc4.editor.model;

import java.io.*;
import java.math.BigDecimal;


public class VC3ModelConverter {

	static String mod;
	
	static BigDecimal[] translate = new BigDecimal[]{new BigDecimal("-0.125"), new BigDecimal("0"), new BigDecimal("0")};
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		ByteArrayOutputStream byt = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(byt);
		String s;
		while ((s = stdin.readLine()) != null && s.length()!= 0) {
			s = s.trim();
			s = s.replace("new ModelQuad(", "");
			s = s.replace(")),", "");
			s = s.replace("new ", "");
			s = s.replace("Vector3f(", "");
			s = s.replace("Vector2f(", "");
			s = s.replace(")", "");
			s = s.replace("F", "");
			s = s.replaceAll("[\\s]", "");
			s = s.replace(",", " ");
			String nums[] = s.split(" ");
			BigDecimal[] ds = new BigDecimal[nums.length];
			for(int d = 0; d < ds.length; ++d) ds[d] = new BigDecimal(nums[d]);
			convert(ds, out);
		}
		String result = byt.toString("UTF-8");
		System.out.println(result);
	}
	
	//Index
	//sx    sy    sz    ex    ey    ez    tsx    tsy    tex    tey
	//00    01    02    03    04    05    006    007    008    009
	
	private static void convert(BigDecimal[] nums, PrintStream out){
		if(nums[1].compareTo(nums[4]) == 0){ //start.y = end.y
			//v1
			out.println(texLine(nums[6], nums[7]));
			out.println(vertLine(nums[0], nums[1], nums[2]));
			//v2
			out.println(texLine(nums[8], nums[7]));
			out.println(vertLine(nums[3], nums[1], nums[2]));
			//v3
			out.println(texLine(nums[8], nums[9]));
			out.println(vertLine(nums[3], nums[1], nums[5]));
			//v1
			out.println(texLine(nums[6], nums[7]));
			out.println(vertLine(nums[0], nums[1], nums[2]));
			//v3
			out.println(texLine(nums[8], nums[9]));
			out.println(vertLine(nums[3], nums[1], nums[5]));
			//v4
			out.println(texLine(nums[6], nums[9]));
			out.println(vertLine(nums[0], nums[1], nums[5]));
		} else {
			//v1
			out.println(texLine(nums[6], nums[7]));
			out.println(vertLine(nums[0], nums[1], nums[2]));
			//v2
			out.println(texLine(nums[8], nums[7]));
			out.println(vertLine(nums[3], nums[1], nums[5]));
			//v3
			out.println(texLine(nums[8], nums[9]));
			out.println(vertLine(nums[3], nums[4], nums[5]));
			//v1
			out.println(texLine(nums[6], nums[7]));
			out.println(vertLine(nums[0], nums[1], nums[2]));
			//v3
			out.println(texLine(nums[8], nums[9]));
			out.println(vertLine(nums[3], nums[4], nums[5]));
			//v4
			out.println(texLine(nums[6], nums[9]));
			out.println(vertLine(nums[0], nums[4], nums[2]));
		}
	}
	
	private static String texLine(BigDecimal tx, BigDecimal ty){
		return "t " + tx.toPlainString() + " " + ty.toPlainString() + " 0";
	}
	
	private static String vertLine(BigDecimal x, BigDecimal y, BigDecimal z){
		return "v " + x.add(translate[0]).toPlainString() + " " + y.add(translate[1]).toPlainString() + " " + z.add(translate[2]).toPlainString();
	}
}
