package vc4.vanilla;

import vc4.api.Resources;
import vc4.api.graphics.texture.AnimatedTexture;

public class BlockTexture {

	public static int grassTop, grassSide, dirt, brick, stoneBrick, sandstoneBrick, obsidianBrick;
	public static int cobble, hellCobble, moss, hellrock, goldBrick, adamantiteBrick;
	public static int oakSapling, oakLeaves, oakWood, oakBase;
	public static int birchSapling, birchLeaves, birchWood, birchBase;
	public static int willowSapling, willowLeaves, willowWood, willowBase;
	public static int ashSapling, ashLeaves, ashWood, ashBase;
	public static int chestnutSapling, chestnutLeaves, chestnutWood, chestnutBase;
	public static int redwoodSapling, redwoodLeaves, redwoodWood, redwoodBase;
	public static int kapokSapling, kapokLeaves, kapokWood, kapokBase;
	public static int bookshelf, enchantedBookshelf, glass, fluid, obsidian, ore, stone;
	public static int sand, cracks, woodFront, cactusBottom, cactusTop, cactusSide;
	public static int vines, tallGrass, craftingTop, chestFront, chestTop, chestSide;
	public static int gravel;
	public static int[] crops = new int[9];
	public static int[] craftingTables = new int[5];
	
	public static void update(){
		AnimatedTexture tex = Resources.getAnimatedTexture("blocks");
		grassTop = tex.getArrayIndex("grasstop");
		grassSide = tex.getArrayIndex("grassside");
		dirt = tex.getArrayIndex("dirt");
		brick = tex.getArrayIndex("brick");
		stoneBrick = tex.getArrayIndex("stonebrick");
		sandstoneBrick = tex.getArrayIndex("sandstonebrick");
		obsidianBrick = tex.getArrayIndex("obsidianbrick");
		cobble = tex.getArrayIndex("cobble");
		hellCobble = tex.getArrayIndex("hellcobble");
		moss = tex.getArrayIndex("moss");
		hellrock = tex.getArrayIndex("hellrock");
		goldBrick = tex.getArrayIndex("goldbrick");
		adamantiteBrick = tex.getArrayIndex("adamantitebrick");
		oakSapling = tex.getArrayIndex("oaksapling");
		oakLeaves = tex.getArrayIndex("oakleaves");
		oakBase = tex.getArrayIndex("oakbase");
		oakWood = tex.getArrayIndex("oakwood");
		birchSapling = tex.getArrayIndex("birchsapling");
		birchLeaves = tex.getArrayIndex("birchleaves");
		birchBase = tex.getArrayIndex("birchbase");
		birchWood = tex.getArrayIndex("birchwood");
		willowSapling = tex.getArrayIndex("willowsapling");
		willowLeaves = tex.getArrayIndex("willowleaves");
		willowBase = tex.getArrayIndex("willowbase");
		willowWood = tex.getArrayIndex("willowwood");
		ashSapling = tex.getArrayIndex("ashsapling");
		ashLeaves = tex.getArrayIndex("ashleaves");
		ashBase = tex.getArrayIndex("ashbase");
		ashWood = tex.getArrayIndex("ashwood");
		chestnutSapling = tex.getArrayIndex("chestnutsapling");
		chestnutLeaves = tex.getArrayIndex("chestnutleaves");
		chestnutBase = tex.getArrayIndex("chestnutbase");
		chestnutWood = tex.getArrayIndex("chestnutwood");
		redwoodSapling = tex.getArrayIndex("redwoodsapling");
		redwoodLeaves = tex.getArrayIndex("redwoodleaves");
		redwoodBase = tex.getArrayIndex("redwoodbase");
		redwoodWood = tex.getArrayIndex("redwoodwood");
		kapokSapling = tex.getArrayIndex("kapoksapling");
		kapokLeaves = tex.getArrayIndex("kapokleaves");
		kapokBase = tex.getArrayIndex("kapokbase");
		kapokWood = tex.getArrayIndex("kapokwood");
		stone = tex.getArrayIndex("stone");
		bookshelf = tex.getArrayIndex("bookshelves");
		enchantedBookshelf = tex.getArrayIndex("bookshelvesenchanted");
		glass = tex.getArrayIndex("glass");
		fluid = tex.getArrayIndex("fluid");
		obsidian = tex.getArrayIndex("obsidian");
		ore = tex.getArrayIndex("ore");
		sand = tex.getArrayIndex("sand");
		cracks = tex.getArrayIndex("cracks");
		woodFront = tex.getArrayIndex("woodfront");
		cactusSide = tex.getArrayIndex("cactusside");
		cactusTop = tex.getArrayIndex("cactustop");
		cactusBottom = tex.getArrayIndex("cactusbottom");
		vines = tex.getArrayIndex("vines");
		for(int d = 0; d < 9; ++d){
			crops[d] = tex.getArrayIndex("weeds" + (d + 1));
		}
		tallGrass = tex.getArrayIndex("tallgrass");
		for(int d = 0; d < 2; ++d){
			craftingTables[d] = tex.getArrayIndex("craftingtable" + (d + 1));
		}
		craftingTop = tex.getArrayIndex("brownwood");
		chestFront = tex.getArrayIndex("chestfront");
		chestSide = tex.getArrayIndex("chestside");
		chestTop = tex.getArrayIndex("chesttop");
		gravel = tex.getArrayIndex("gravel");
	}
}
