/**
 * 
 */
package vc4.vanilla.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

/**
 * @author paul
 *
 */
public class BlockOre extends BlockMultitexture {

	public static Color[] oreColors = new Color[32];
	public static String[] oreNames = new String[32];
	
	
	public static void registerOre(int id, String name, Color color){
		oreColors[id] = color;
		oreNames[id] = name;
	}
	
	static{
		registerOre(0, "coal", new Color(32, 32, 32)); //Tier 1 fuel (cook's 10, 5 torches)
		registerOre(1, "copper", new Color(127, 51, 0)); //T1
		registerOre(2, "tin", new Color(90, 70, 70)); //T1
		registerOre(3, "iron", new Color(158, 139, 139)); //T2
		registerOre(4, "kradonium", new Color(139, 0, 255)); //T3 (Tier 1 wires)
		registerOre(5, "silver", new Color(192, 192, 192)); //T4
		registerOre(6, "gold", new Color(255, 226, 102)); //T5
		registerOre(7, "mithril", new Color(0, 38, 255)); //T6
		registerOre(8, "titanium", new Color(133, 105, 94)); //T7 (Tier 2 wires)
		registerOre(9, "hellish", new Color(225, 50, 0)); //T8
		registerOre(10, "platinum", new Color(168, 167, 165)); //T9
		registerOre(11, "adamantite", new Color(60, 93, 60)); //T10
		
		registerOre(16, "nickel", new Color(187, 185, 170));
		registerOre(17, "zinc", new Color(120, 120, 150));
		
		registerOre(20, "sulphur", new Color(154, 127, 46)); //Tier 1 explosive
		registerOre(21, "francium", new Color(178, 205, 132)); //Tier 2 explosive
		
		registerOre(25, "ash", new Color(217, 217, 225)); //Tier 2 fuel (cooks 25 , 12 torches)
		registerOre(26, "ember", new Color(255, 126, 16)); //Tier 3 fuel (cooks 75 , 33 torches)
	}
	
	/**
	 * @param uid
	 * @param texture
	 * @param material
	 */
	public BlockOre(int uid, int tex) {
		super(uid, tex, "ore");
		
		mtIndex = BlockTexture.ore;
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getCreativeItems()
	 */
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> items = new ArrayList<>();
		for(int d = 0; d < 32; ++d){
			if(oreColors[d] != null){
				items.add(new ItemStack(uid, d, 1));
			}
		}
		return items;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getColor(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public Color getColorMultitexture(World world, long x, long y, long z, int side) {
		return getColor(world.getBlockData(x, y, z));
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getColor(vc4.api.item.ItemStack, int)
	 */
	@Override
	public Color getColorMultitexture(ItemStack current, int side) {
		return getColor(current.getDamage());
	}
	
	

	/**
	 * @param blockData
	 * @return
	 */
	private Color getColor(int data) {
		return oreColors[data];
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getModifiedName(vc4.api.item.ItemStack)
	 */
	@Override
	protected String getModifiedName(ItemStack item) {
		return "ore." + oreNames[item.getDamage()];
	}

}
