package vc4.api.util;

import java.awt.Color;

import vc4.api.math.MathUtils;


public class ColorUtils {

	public static Color[] lightColors = new Color[256];
	
//	static{
//		for(int d = 0; d < 16; ++d){
//			int dshift = d << 4;
//			Color c = ChatColor.getColor(15 - d);
//			if(d == 15) c = ChatColor.getColor(16);
//			for(int i = 15; i > -1; --i){
//				lightColors[dshift + i] = c;
//				c.r *= 0.83F;
//				c.g *= 0.83F;
//				c.b *= 0.83F;
//			}
//		}
//		
//	}
	
	
	public static Color differColors(Color c1, Color c2, float dec)
    {
		if(dec > 1.0F) dec = 1.0F;
		else if(dec < 0.0F) dec = 0.0F;
		
        //Port from VoxelCaverns 1 (Indev2D)
        short RDif = (short)(c2.getRed() - c1.getRed());
        short GDif = (short)(c2.getGreen() - c1.getGreen());
        short BDif = (short)(c2.getBlue() - c1.getBlue());

        int _R = (int)(c1.getRed() + (RDif * dec));
        int _G = (int)(c1.getGreen() + (GDif * dec));
        int _B = (int)(c1.getBlue() + (BDif * dec));

        int lowA = Math.min(c1.getAlpha(), c2.getAlpha());
        
        try{
        return new Color(_R, _G, _B, lowA);
        } catch(Exception e){
        	return c1;
        }
    }
	
	public static Color getMixedColor(Color c1, Color c2) {
		int _r = Math.min(c1.getRed(), c2.getRed());
		int _g = Math.min(c1.getGreen(), c2.getGreen());
		int _b = Math.min(c1.getBlue(), c2.getBlue());
		int lowA = Math.min(c1.getAlpha(), c2.getAlpha());
		return new Color(_r, _g, _b, lowA);
		
	}
	
	public static Color getAverageColor(Color c1, Color c2) {
		if(c1 == null) return c2;
		else if(c2 == null) return c1;
		int _r = MathUtils.avg(c1.getRed(), c2.getRed());
		int _g = MathUtils.avg(c1.getGreen(), c2.getGreen());
		int _b = MathUtils.avg(c1.getBlue(), c2.getBlue());
		int lowA = Math.min(c1.getAlpha(), c2.getAlpha());
		return new Color(_r, _g, _b, lowA);
	}
	
	public static Color getAverageColor(Color c1, Color c2, Color c3) {
		if(c1 == null) return getAverageColor(c2, c3);
		else if(c2 == null) return getAverageColor(c1, c3);
		else if(c3 == null) return getAverageColor(c2, c1);
		int _r = MathUtils.avg(c1.getRed(), c2.getRed(), c3.getRed());
		int _g = MathUtils.avg(c1.getGreen(), c2.getGreen(), c3.getGreen());
		int _b = MathUtils.avg(c1.getBlue(), c2.getBlue(), c3.getBlue());
		int lowA = Math.min(c1.getAlpha(), c2.getAlpha());
		return new Color(_r, _g, _b, lowA);
	}
	
	public static Color getAverageColor(Color c1, Color c2, Color c3, Color c4) {
		if(c1 == null) return getAverageColor(c2, c3, c4);
		else if(c2 == null) return getAverageColor(c1, c3, c4);
		else if(c3 == null) return getAverageColor(c2, c1, c4);
		else if(c4 == null) return getAverageColor(c1, c2, c3);
		int _r = MathUtils.avg(c1.getRed(), c2.getRed(), c3.getRed(), c4.getRed());
		int _g = MathUtils.avg(c1.getGreen(), c2.getGreen(), c3.getGreen(), c4.getRed());
		int _b = MathUtils.avg(c1.getBlue(), c2.getBlue(), c3.getBlue(), c4.getRed());
		int lowA = Math.min(c1.getAlpha(), c2.getAlpha());
		return new Color(_r, _g, _b, lowA);
	}
	
	public static Color getMixedColor(byte level, Color c2) {
		Color c1 = new Color(level * 17, level * 17, level * 17);
		int _r = Math.min(c1.getRed(), c2.getRed());
		int _g = Math.min(c1.getGreen(), c2.getGreen());
		int _b = Math.min(c1.getBlue(), c2.getBlue());
		
		return new Color(_r, _g, _b, c2.getAlpha());
	}
	public static Color getLightColor(byte light) {
		return lightColors[light & 0xFF];
	}
	
	public static Color brighterLinear(int amount, Color color){
		int nr = color.getRed() + amount;
		int ng = color.getGreen() + amount;
		int nb = color.getBlue() + amount;
		return new Color(Math.min(nr, 255), Math.min(ng, 255), Math.min(nb, 255), color.getAlpha());
	}
	public static Color darkerLinear(int amount, Color color){
		int nr = color.getRed() - amount;
		int ng = color.getGreen() - amount;
		int nb = color.getBlue() - amount;
		return new Color(Math.max(nr, 0), Math.max(ng, 0), Math.max(nb, 0), color.getAlpha());
	}
	
	
}
