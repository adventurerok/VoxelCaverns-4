/**
 * 
 */
package vc4.api.font;

import java.io.*;
import java.util.*;

import vc4.api.graphics.*;
import vc4.api.graphics.texture.SheetTexture;
import vc4.api.logging.Logger;

/**
 * @author paul
 *
 */
public class Font {

	private static Random rand = new Random();
	
	private HashMap<Character, Gylph> gylphs = new HashMap<Character, Gylph>();
	private HashMap<Integer, ArrayList<Gylph>> advs = new HashMap<Integer, ArrayList<Gylph>>();
	private int size, lineHeight, base;
	private boolean bold;
	private SheetTexture texture;
	private int aLine, bLine;
	
	private int minY = 1024, maxY = 0;
	
	private Font boldPair;
	
	private String name;
	
	public void loadFont(InputStream font){
		BufferedReader r = new BufferedReader(new InputStreamReader(font));
		String line;
		try {
			while((line = r.readLine()) != null){
				if(line.startsWith("char ")){
					Gylph g = new Gylph(line);
					if(g.yoffset < minY) minY = g.yoffset;
					else if(g.yoffset + g.height > maxY) maxY = g.yoffset + g.height;
					Character nc = Character.valueOf((char) g.id);
					gylphs.put(nc, g);
					ArrayList<Gylph> nw = advs.get(g.xadvance);
					if(nw != null) nw.add(g);
					else{
						nw = new ArrayList<Gylph>();
						nw.add(g);
						advs.put(g.xadvance, nw);
					}
				} else if(line.startsWith("info ")){
					int[] ints = getInts(line, "size", "bold");
					size = ints[0];
					bold = ints[1] == 1;
				} else if(line.startsWith("common")){
					int[] ints = getInts(line, "lineHeight", "base");
					lineHeight = ints[0];
					base = ints[1];
				}
			}
		} catch (IOException e) {
			Logger.getLogger(Font.class).warning("Failed to read font", e);
		}
		aLine = base;
		bLine = lineHeight - base;
	}
	
	public Font(String name){
		this(name, false);
	}
	
	private Font(String name, boolean isBold){
		if(isBold) this.name = name.substring(0, name.lastIndexOf("_"));
		else this.name = name;
		String path = "vc4/resources/font/" + name;
		loadFont(Font.class.getClassLoader().getResourceAsStream(path + ".fnt"));
		if(!isBold){
			try {
				texture = Graphics.getSheetLoader().loadTexture(path);
				OpenGL gl = Graphics.getClientOpenGL();
//				for(int d = 0; d < texture.getNumberOfFrames(); ++d){
//					gl.bindTexture(GLTexture.TEX_2D_ARRAY, texture.getTexture(d));
//					gl.texParameterMinFilter(GLTexture.TEX_2D_ARRAY, GLTextureFilter.LINEAR, GLTextureFilter.LINEAR);
//					gl.generateMipmap(GLTexture.TEX_2D_ARRAY);
//				}
				gl.bindTexture(GLTexture.TEX_2D_ARRAY, 0);
			} catch (IOException e) {
				Logger.getLogger(Font.class).warning("Failed to load font texture", e);
			}
		}
		if(isBold) return;
		boldPair = new Font(name + "_bold", true);
		boldPair.boldPair = this;
	}
	
	private int[] getInts(String line, String...names){
		String[] parts = line.split(" ");
		int[] result = new int[names.length];
		for(String s : parts){
			if(!s.contains("=")) continue;
			String np[] = s.split("=");
			String var = np[0];
			try{
				int val = Integer.parseInt(np[1]);
				for(int d = 0; d < names.length; ++d){
					if(names[d].equals(var)) result[d] = val;
				}
			} catch(NumberFormatException e){
				
			}
		}
		return result;
	}
	
	public static void pairFonts(Font regular, Font bold){
		if(regular.bold && !bold.bold){
			pairFonts(bold, regular);
			return;
		}
		if(regular.bold || !bold.bold) return;
		regular.boldPair = bold;
		bold.boldPair = regular;
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @return the boldPair
	 */
	public Font getBoldPair() {
		return boldPair;
	}
	
	/**
	 * @return the bold
	 */
	public boolean isBold() {
		return bold;
	}
	
	/**
	 * @return the base
	 */
	public int getBase() {
		return base;
	}
	
	/**
	 * @return the lineHeight
	 */
	public int getLineHeight() {
		return lineHeight;
	}
	
	public SheetTexture getTexture() {
		return texture;
	}
	
	public Gylph getGylph(char c){
		Character nc = Character.valueOf(c);
		return gylphs.get(nc);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the aLine
	 */
	public int getaLine() {
		return aLine;
	}
	
	/**
	 * @return the bLine
	 */
	public int getbLine() {
		return bLine;
	}
	
	/**
	 * @return the maxY
	 */
	public int getMaxY() {
		return maxY;
	}
	
	/**
	 * @return the minY
	 */
	public int getMinY() {
		return minY;
	}
	
	public Gylph getRandomGylph(Gylph size){
		ArrayList<Gylph> g = advs.get(size.xadvance);
		if(g == null) return size;
		return g.get(rand.nextInt(g.size()));
	}
	

}
