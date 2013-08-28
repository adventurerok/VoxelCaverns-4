package vc4.vanilla.item;

import java.awt.Color;

import vc4.api.item.Item;
import vc4.vanilla.block.BlockOre;

public class ItemAlloy extends Item{
	
	public static Color[] alloyColors = new Color[64];
	public static String[] alloyNames = new String[64];
	
	static{
		registerAlloy(0, "bronze", BlockOre.oreColors[1], BlockOre.oreColors[2]);
		registerAlloy(1, "coppad", BlockOre.oreColors[1], BlockOre.oreColors[4]);
		registerAlloy(2, "silvad", BlockOre.oreColors[6], BlockOre.oreColors[4]);
		registerAlloy(3, "billon", BlockOre.oreColors[1], BlockOre.oreColors[6]);
		registerAlloy(4, "invar", BlockOre.oreColors[3], BlockOre.oreColors[16]);
		registerAlloy(5, "mithad", BlockOre.oreColors[7], BlockOre.oreColors[4]);
		registerAlloy(6, "hellad", BlockOre.oreColors[9], BlockOre.oreColors[4]);
		
	}
	
	public static void registerAlloy(int id, String name, Color...mix){
		int r = 0, g = 0, b = 0, a = 0;
		for(Color c : mix){
			r += c.getRed();
			g += c.getGreen();
			b += c.getBlue();
			a += c.getAlpha();
		}
		r /= mix.length;
		g /= mix.length;
		b /= mix.length;
		a /= mix.length;
		Color fin = new Color(r, g, b, a);
		alloyColors[id] = fin;
		alloyNames[id] = name;
	}

	public ItemAlloy(int id, int textureIndex) {
		super(id, textureIndex);
		// TASK Auto-generated constructor stub
	}

	public ItemAlloy(int id) {
		super(id);
		// TASK Auto-generated constructor stub
	}

}
