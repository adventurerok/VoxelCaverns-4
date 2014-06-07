/**
 * 
 */
package vc4.vanilla;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import vc4.api.VoxelCaverns;
import vc4.api.area.Area;
import vc4.api.biome.*;
import vc4.api.block.*;
import vc4.api.container.Container;
import vc4.api.crafting.CraftingManager;
import vc4.api.entity.Entity;
import vc4.api.entity.spawn.*;
import vc4.api.generator.*;
import vc4.api.gui.GuiOpenContainer;
import vc4.api.io.Dictionary;
import vc4.api.io.DictionaryInfo;
import vc4.api.item.Item;
import vc4.api.itementity.ItemEntity;
import vc4.api.list.IntList;
import vc4.api.plugin.Plugin;
import vc4.api.sound.Music;
import vc4.api.sound.MusicType;
import vc4.api.tileentity.TileEntity;
import vc4.api.tool.*;
import vc4.api.util.DirectoryLocator;
import vc4.api.world.World;
import vc4.vanilla.area.AreaVillage;
import vc4.vanilla.biome.*;
import vc4.vanilla.block.*;
import vc4.vanilla.container.ContainerChest;
import vc4.vanilla.crafting.RecipesBlocks;
import vc4.vanilla.entity.EntityNpc;
import vc4.vanilla.entity.EntityZombie;
import vc4.vanilla.generation.dungeon.Dungeon;
import vc4.vanilla.generation.plant.*;
import vc4.vanilla.generation.plant.tree.*;
import vc4.vanilla.generation.populate.WorldGenOres;
import vc4.vanilla.generation.world.*;
import vc4.vanilla.gui.GuiChest;
import vc4.vanilla.item.*;
import vc4.vanilla.itementity.ItemEntityChest;
import vc4.vanilla.npc.Names;
import vc4.vanilla.npc.Trade;
import vc4.vanilla.tileentity.TileEntityChest;
import vc4.vanilla.underbiome.UnderBiome;

/**
 * @author paul
 * 
 */
public class Vanilla extends Plugin {

	static Dictionary trades = null;
	static Dictionary underBiomes = null;

	public static Color[] woolColors = new Color[64];

	// Blocks
	public static Block grass, dirt, logV, logX, logZ, leaf, brick, mossBrick;
	public static Block sand, glass, ore, hellrock, lava, oreHell, water, obsidian;
	public static Block planks, planksHalf, bookshelf, planksStairs0, planksStairs4;
	public static Block planksStairs8, planksStairs12, brickStairs0, brickStairs4;
	public static Block brickStairs8, brickStairs12, brickHalf, bookshelfEnchanted;
	public static Block crackedBrick, snow, cactus, weeds, vines, willowVines;
	public static Block workbench, chest, table, chair, gravel, ladder, lightberries;
	public static Block algae, torch, reeds, wheat, barley, stakes, farmland, deadCrop;
	public static Block wire, notGate, repeaterGate, andGate, orGate, xorGate, norGate, nandGate;
	public static Block flipFlop, gapRepeaterGate, button, bitMonitor;
	public static Block redWire, blueWire, greenWire, yellowWire;
	public static Block wool0, wool1, present, explosive, pressureplate;

	// Items
	public static Item food, spawnStick, stick, metalBar, alloyBar, crop, seeds;
	public static Item fertilizer, cure, wrench;

	// Plants
	public static Plant plantTreeOak;
	public static Plant plantTreeBirch;
	public static Plant plantTreeWillow;
	public static Plant plantTreeRedwood;
	public static Plant plantTreeChestnut;
	public static Plant plantTreeKapokBig;
	public static Plant plantTreeKapok;
	public static Plant plantTreeCypress;
	public static Plant plantStumpCypress;
	public static Plant plantBushOak;
	public static Plant plantBushBirch;
	public static Plant plantBushWillow;
	public static Plant plantBushRedwood;
	public static Plant plantBushChestnut;
	public static Plant plantBushKapok;
	public static Plant plantBushCypress;
	public static Plant plantCactus;
	public static Plant plantGrassTall;
	public static Plant plantGrassWheat;
	public static Plant plantGrassBarley;
	public static Plant plantReedWater;
	public static Plant plantSugarcane;
	public static Plant plantBamboo;

	// Tools
	public static ToolMaterial materialWood = new ToolMaterial("wood", 32, 1);
	public static ToolMaterial materialStone = new ToolMaterial("stone", 64, 2.5);
	public static ToolMaterial materialCopper = new ToolMaterial("copper", 128, 6.25);
	public static ToolMaterial materialBronze = new ToolMaterial("bronze", 256, 10);
	public static ToolMaterial materialIron = new ToolMaterial("iron", 400, 20);
	public static ToolMaterial materialKradonium = new ToolMaterial("kradonium", 512, 37.5);
	public static ToolMaterial materialSilver = new ToolMaterial("silver", 700, 62.5);
	public static ToolMaterial materialGold = new ToolMaterial("gold", 896, 100);
	public static ToolMaterial materialMithril = new ToolMaterial("mithril", 1024, 150);
	public static ToolMaterial materialTitanium = new ToolMaterial("titanium", 1536, 212.5);
	public static ToolMaterial materialHellish = new ToolMaterial("hellish", 2048, 280);
	public static ToolMaterial materialPlatinum = new ToolMaterial("platinum", 3072, 380);
	public static ToolMaterial materialAdamantite = new ToolMaterial("adamantite", 4096, 500);
	public static ToolMaterial materialUnholy = new ToolMaterial("unholy", 5120, 750);
	public static ToolMaterial materialSacred = new ToolMaterial("sacred", 5120, 750);

	// Music
	public static Music musicOverworld = new Music("First_Day", MusicType.BIOME);
	public static Music musicDesert = new Music("Desert_Theme", MusicType.BIOME);
	public static Music musicHell = new Music("A_Night_Out", MusicType.BIOME);
	public static Music musicSky = new Music("Ocean_Theme", MusicType.BIOME);

	// Biomes
	public static BiomeHeightModel hills = new BiomeHeightModel(75, 25, 80, 10);
	public static BiomeHeightModel oceans = new BiomeHeightModel(-38, -80, 1, -100);
	public static BiomeHeightModel trenchs = new BiomeHeightModel(-70, -125, -10, -140);
	public static BiomeHeightModel mountains = new BiomeHeightModel(125, 55, 140, 20);
	public static BiomeHeightModel swampy = new BiomeHeightModel(2, -1, 4, -3);

	public static Biome biomeOcean;
	public static Biome biomeOceanTrench;
	public static BiomeHilly biomePlains;
	public static BiomeHilly biomePlainsS;
	public static Biome biomePlainsH;
	public static Biome biomePlainsSH;
	public static BiomeHilly biomePlainsWheat;
	public static Biome biomePlainsWheatH;
	public static BiomeHilly biomePlainsBarley;
	public static Biome biomePlainsBarleyH;
	public static BiomeHilly biomeDesert;
	public static Biome biomeDesertH;
	public static Biome biomeDesertOasis;
	public static BiomeHilly biomeForest;
	public static Biome biomeForestH;
	public static BiomeHilly biomeVolcanic;
	public static Biome biomeVolcanicH;
	public static BiomeHilly biomeForestRedwoodS;
	public static Biome biomeForestRedwoodSH;
	public static Biome biomeRockyH;
	public static Biome biomeSwamp;
	public static BiomeHilly biomeJungle;
	public static Biome biomeJungleH;
	public static BiomeHilly biomeForestBirch;
	public static Biome biomeForestBirchH;
	public static BiomeHilly biomeForestBamboo;
	public static Biome biomeForestBambooH;

	public static UnderBiome underStone;
	public static UnderBiome underCobbleCaverns;

	// Crafting
	public static short craftingHammer, craftingSaw, craftingTable, craftingFurnace;
	public static short craftingEnchantedBook;

	// Trades
	public static Trade tradeBlades, tradeArmor, tradePotions, tradeHealing, tradePickaxes;
	public static Trade tradeGuard;

	public static ArrayList<ArrayList<Integer>> biomes;
	public static IntList underBiomesList;
	public static ZoomGenerator underBiomesGen;

	private static ToolMaterial[] materials = new ToolMaterial[] { materialWood, materialStone, materialCopper, materialBronze, materialIron, materialKradonium, materialSilver, materialGold, materialMithril, materialTitanium,
			materialHellish, materialPlatinum, materialAdamantite, materialUnholy, materialSacred };

	private static ToolType[] types = new ToolType[] { ToolType.pickaxe, ToolType.spade, ToolType.axe, ToolType.hoe };

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		loadWoolColors();
		// normal
		plantTreeOak = new Plant("tree", "oak", "normal").setData((byte) 0);
		plantTreeBirch = new Plant("tree", "birch", "normal").setData((byte) 1);
		plantTreeWillow = new Plant("tree", "willow", "normal").setData((byte) 2);
		plantTreeRedwood = new Plant("tree", "redwood", "normal").setData((byte) 5);
		plantTreeKapok = new Plant("tree", "kapok", "normal").setData((byte) 6);
		plantTreeCypress = new Plant("tree", "cypress", "normal").setData((byte) 7);

		// big
		plantTreeKapokBig = new Plant("tree", "kapok", "big").setData((byte) 6);

		// stump
		plantStumpCypress = new Plant("tree", "cypress", "stump").setData((byte) 7);

		// bush
		plantBushOak = new Plant("tree", "oak", "bush").setData((byte) 0);
		plantBushBirch = new Plant("tree", "birch", "bush").setData((byte) 1);
		plantBushWillow = new Plant("tree", "willow", "bush").setData((byte) 2);
		plantBushRedwood = new Plant("tree", "redwood", "bush").setData((byte) 5);
		plantBushKapok = new Plant("tree", "kapok", "bush").setData((byte) 6);
		plantBushCypress = new Plant("tree", "cypress", "bush").setData((byte) 7);

		plantCactus = new Plant("cactus", "desert", "normal");

		// grass
		plantGrassTall = new Plant("grass", "weed", "normal");
		plantGrassWheat = new Plant("grass", "wheat", "crop");
		plantGrassBarley = new Plant("grass", "barley", "crop");

		// ?canes?
		plantReedWater = new Plant("reed", "water", "normal");
		plantSugarcane = new Plant("cane", "sugar", "crop");
		plantBamboo = new Plant("bamboo", "bamboo", "crop");

		WorldGenerator gen = new OverworldGenerator();
		GeneratorList.registerGenerator("overworld", gen);
		GeneratorList.registerGenerator("flat", new FlatlandsGenerator());
		GeneratorList.registerGenerator("sky", new SkylandGenerator());
		GeneratorList.registerGenerator("dungeontest", new DungeonTestGenerator());

		// normal
		GeneratorList.registerPlantGen(plantTreeOak, new TreeGenBasic());
		GeneratorList.registerPlantGen(plantTreeBirch, new TreeGenBasic());
		GeneratorList.registerPlantGen(plantTreeWillow, new TreeGenWillow());
		GeneratorList.registerPlantGen(plantTreeRedwood, new TreeGenRedwood());
		GeneratorList.registerPlantGen(plantTreeKapok, new TreeGenBasic());
		GeneratorList.registerPlantGen(plantTreeCypress, new TreeGenCypress());

		// big
		GeneratorList.registerPlantGen(plantTreeKapokBig, new TreeGenKapok());

		// stump
		GeneratorList.registerPlantGen(plantStumpCypress, new TreeGenCypress());

		// bush
		GeneratorList.registerPlantGen(plantBushOak, new TreeGenBush());
		GeneratorList.registerPlantGen(plantBushBirch, new TreeGenBush());
		GeneratorList.registerPlantGen(plantBushWillow, new TreeGenBush());
		GeneratorList.registerPlantGen(plantBushRedwood, new TreeGenBush());
		GeneratorList.registerPlantGen(plantBushCypress, new TreeGenBush());

		GeneratorList.registerPlantGen(plantCactus, new PlantGenCactus());
		GeneratorList.registerPlantGen(plantReedWater, new PlantGenReeds());
		GeneratorList.registerPlantGen(plantSugarcane, new PlantGenCane((byte) 1));
		GeneratorList.registerPlantGen(plantBamboo, new PlantGenCane((byte) 2));

		GuiOpenContainer.addContainerGui("chest", GuiChest.class);

		Names.load();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {

	}

	public void loadWoolColors() {
		BufferedImage buf = null;
		try {
			buf = ImageIO.read(Vanilla.class.getResourceAsStream("resources/images/64color.png"));
		} catch (IOException e) {
			e.printStackTrace();

		}
		if (buf != null) {
			for (int x = 0; x < 8; ++x) {
				for (int y = 0; y < 8; ++y) {
					woolColors[y * 8 + x] = new Color(buf.getRGB(x, y));
				}
			}
		}
	}

	@Override
	public void loadCraftingItems(World world) {
		craftingSaw = world.getRegisteredCrafting("vanilla.saw");
		craftingHammer = world.getRegisteredCrafting("vanilla.hammer");
		craftingTable = world.getRegisteredCrafting("vanilla.table");
		craftingEnchantedBook = world.getRegisteredCrafting("vanilla.enchantedbook");
		craftingFurnace = world.getRegisteredCrafting("vanilla.furnace");
		CraftingManager.setToolIcon(craftingSaw, "saw");
		CraftingManager.setToolIcon(craftingHammer, "hammer");
		CraftingManager.setToolIcon(craftingTable, "table");
		CraftingManager.setToolIcon(craftingEnchantedBook, "enchantedbook");
		CraftingManager.setToolIcon(craftingFurnace, "furnace");
	}

	@Override
	public void loadEntities(World world) {
		Entity.registerEntity("vanilla.villager", EntityNpc.class);
		Entity.registerEntity("vanilla.zombie", EntityZombie.class);
	}

	@Override
	public void loadAreas(World world) {
		Area.registerArea("vanilla.village", AreaVillage.class);
	}

	@Override
	public ArrayList<DictionaryInfo> getDictInfos() {
		ArrayList<DictionaryInfo> res = new ArrayList<>();
		res.add(new DictionaryInfo("underbiomes", 0));
		res.add(new DictionaryInfo("trades", 0));
		return res;
	}

	@Override
	public void preWorldLoad(World world) {
		if (VoxelCaverns.hasGraphics()) {
			BlockTexture.update();
			ItemTexture.update();
		}
		biomes = new ArrayList<>();
		for (int d = 0; d < BiomeType.getNumberOfTypes(); ++d)
			biomes.add(new ArrayList<Integer>());

		underBiomes = world.getDictionary("underbiomes");

	}

	@Override
	public void loadBlocks(World world) {
		grass = new BlockGrass(world.getRegisteredBlock("vanilla.grass"), Material.getMaterial("grass")).setName("grass");
		dirt = new BlockDirt(world.getRegisteredBlock("vanilla.dirt"), BlockTexture.dirt, Material.getMaterial("dirt")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.22)).setName("dirt");
		logV = new BlockLog(world.getRegisteredBlock("vanilla.log.V"), Material.getMaterial("wood"), 0).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		logX = new BlockLog(world.getRegisteredBlock("vanilla.log.X"), Material.getMaterial("wood"), 1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		logZ = new BlockLog(world.getRegisteredBlock("vanilla.log.Z"), Material.getMaterial("wood"), 2).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		leaf = new BlockLeaf(world.getRegisteredBlock("vanilla.leaf"), Material.getMaterial("leaf")).setLightOpacity(1).setName("leaf");
		brick = new BlockBrick(world.getRegisteredBlock("vanilla.brick")).setName("brick");
		mossBrick = new BlockBrickMoss(world.getRegisteredBlock("vanilla.brick.moss")).setName("brick");
		sand = new BlockSand(world.getRegisteredBlock("vanilla.sand")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.20)).setName("sand");
		glass = new BlockGlass(world.getRegisteredBlock("vanilla.glass"), BlockTexture.glass, Material.getMaterial("glass")).setLightOpacity(1).setMineData(new MiningData().setPowers(10000000, 1, 50).setTimes(1, 0.09, 0.9)).setName("glass");
		ore = new BlockOre(world.getRegisteredBlock("vanilla.ore"), BlockTexture.stone);
		hellrock = new Block(world.getRegisteredBlock("vanilla.hellrock"), BlockTexture.hellrock, Material.getMaterial("hellrock")).setMineData(new MiningData().setRequired(ToolType.pickaxe).setPowers(1, 1, 25).setTimes(3, 0.08, 0.8)).setName("hellrock");
		lava = new BlockLava(world.getRegisteredBlock("vanilla.lava")).setLightOpacity(1).setLightLevel(15).setName("lava");
		oreHell = new BlockOre(world.getRegisteredBlock("vanilla.ore.hell"), BlockTexture.hellrock);
		water = new BlockWater(world.getRegisteredBlock("vanilla.water")).setLightOpacity(1).setName("water");
		obsidian = new BlockObsidian(world.getRegisteredBlock("vanilla.obsidian")).setMineData(new MiningData().setRequired(ToolType.pickaxe).setPowers(1, 1, 35).setTimes(10, 0.3, 3)).setName("obsidian");
		planks = new BlockPlanks(world.getRegisteredBlock("vanilla.planks")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planks");
		planksHalf = new BlockPlanksHalf(world.getRegisteredBlock("vanilla.planks.half")).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("plankshalf");
		bookshelf = new BlockBookshelf(world.getRegisteredBlock("vanilla.bookshelf")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("bookshelf");
		planksStairs0 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.0"), 0).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs4 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.4"), 4).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs8 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.8"), 8).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs12 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.12"), 12).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		brickHalf = new BlockBrickHalf(world.getRegisteredBlock("vanilla.brick.half")).setLightOpacity(1).setName("brickhalf");
		brickStairs0 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.0"), 0).setLightOpacity(1).setName("brickstairs");
		brickStairs4 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.4"), 4).setLightOpacity(1).setName("brickstairs");
		brickStairs8 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.8"), 8).setLightOpacity(1).setName("brickstairs");
		brickStairs12 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.12"), 12).setLightOpacity(1).setName("brickstairs");
		bookshelfEnchanted = new BlockBookshelfEnchanted(world.getRegisteredBlock("vanilla.bookshelf.enchanted")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(1, 5, 50).setTimes(3, 0.1, 0.75)).setName("bookshelfenchanted");
		crackedBrick = new BlockBrickCracked(world.getRegisteredBlock("vanilla.brick.cracked")).setName("crackedbrick");
		snow = new BlockSnow(world.getRegisteredBlock("vanilla.snow")).setLightOpacity(1).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(1, 1, 15).setTimes(0.75, 0.45, 0.08)).setName("snow");
		cactus = new BlockCactus(world.getRegisteredBlock("vanilla.cactus")).setLightOpacity(1).setName("cactus");
		weeds = new BlockTallGrass(world.getRegisteredBlock("vanilla.weeds")).setLightOpacity(1).setName("weeds");
		vines = new BlockVine(world.getRegisteredBlock("vanilla.vine"), BlockTexture.vines, "vine").setLightOpacity(1).setName("vine");
		willowVines = new BlockWillowVine(world.getRegisteredBlock("vanilla.willowvine"), BlockTexture.vines, "vine").setLightOpacity(1).setName("vine");
		workbench = new BlockCraftingTable(world.getRegisteredBlock("vanilla.workbench"), 0, "wood").setName("craftingtable");
		chest = new BlockChest(world.getRegisteredBlock("vanilla.chest")).setName("chest");
		table = new BlockTable(world.getRegisteredBlock("vanilla.table")).setLightOpacity(1).setName("table");
		chair = new BlockChair(world.getRegisteredBlock("vanilla.chair")).setLightOpacity(1).setName("chair");
		gravel = new BlockGravel(world.getRegisteredBlock("vanilla.gravel")).setName("gravel");
		ladder = new BlockLadder(world.getRegisteredBlock("vanilla.ladder")).setLightOpacity(1).setName("ladder");
		lightberries = new BlockLightBerry(world.getRegisteredBlock("vanilla.lightberry")).setName("lightberry");
		algae = new BlockAlgae(world.getRegisteredBlock("vanilla.algae")).setMineData(new MiningData().setTimes(0.03, 0.02, 0.01)).setName("algae");
		torch = new BlockTorch(world.getRegisteredBlock("vanilla.torch")).setName("torch");
		reeds = new BlockReeds(world.getRegisteredBlock("vanilla.reeds")).setMineData(new MiningData().setTimes(0.01, 0.01, 0.01)).setLightOpacity(1).setName("reeds");
		wheat = new BlockCrop(world.getRegisteredBlock("vanilla.wheat"), 0).setMineData(new MiningData().setTimes(0.01, 0.01, 0.01)).setLightOpacity(1).setName("wheat");
		barley = new BlockCrop(world.getRegisteredBlock("vanilla.barley"), 1).setMineData(new MiningData().setTimes(0.01, 0.01, 0.01)).setLightOpacity(1).setName("barley");
		stakes = new BlockStakes(world.getRegisteredBlock("vanilla.stakes")).setMineData(new MiningData().setTimes(0.01, 0.01, 0.01)).setLightOpacity(1).setName("stakes");
		farmland = new BlockFarmland(world.getRegisteredBlock("vanilla.farmland")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.22)).setName("farmland");
		deadCrop = new BlockCrop(world.getRegisteredBlock("vanilla.deadcrop"), -1).setMineData(new MiningData().setTimes(0.01, 0.01, 0.01)).setLightOpacity(1).setName("deadcrop");
		wire = new BlockWire(world.getRegisteredBlock("vanilla.wire")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("wire");
		notGate = new BlockNotGate(world.getRegisteredBlock("vanilla.gate.not")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.not");
		repeaterGate = new BlockRepeaterGate(world.getRegisteredBlock("vanilla.gate.repeater")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.repeater");
		andGate = new BlockAndGate(world.getRegisteredBlock("vanilla.gate.and")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.and");
		orGate = new BlockOrGate(world.getRegisteredBlock("vanilla.gate.or")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.or");
		xorGate = new BlockXorGate(world.getRegisteredBlock("vanilla.gate.xor")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.xor");
		norGate = new BlockNorGate(world.getRegisteredBlock("vanilla.gate.nor")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.nor");
		nandGate = new BlockNandGate(world.getRegisteredBlock("vanilla.gate.nand")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.nand");
		flipFlop = new BlockFlipFlop(world.getRegisteredBlock("vanilla.flipflop")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("flipflop");
		gapRepeaterGate = new BlockGapRepeaterGate(world.getRegisteredBlock("vanilla.gate.gaprepeater")).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("gate.gaprepeater");
		button = new BlockButton(world.getRegisteredBlock("vanilla.button")).setMineData(new MiningData().setTimes(0.1, 0.1, 0.1)).setLightOpacity(1).setName("button");
		redWire = new BlockWireColored(world.getRegisteredBlock("vanilla.wire.red"), new Color(255, 0, 0), new Color(127, 0, 0), true).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("wire.red");
		blueWire = new BlockWireColored(world.getRegisteredBlock("vanilla.wire.blue"), new Color(0, 0, 255), new Color(0, 0, 127), true).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("wire.blue");
		greenWire = new BlockWireColored(world.getRegisteredBlock("vanilla.wire.green"), new Color(0, 255, 0), new Color(0, 127, 0), false).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("wire.green");
		yellowWire = new BlockWireColored(world.getRegisteredBlock("vanilla.wire.yellow"), new Color(255, 255, 0), new Color(127, 127, 0), false).setMineData(new MiningData().setTimes(0.03, 0.03, 0.03)).setName("wire.yellow");
		bitMonitor = new BlockBitMonitor(world.getRegisteredBlock("vanilla.monitor.bit")).setName("monitor.bit");
		wool0 = new BlockWool(world.getRegisteredBlock("vanilla.wool.0"), 0).setMineData(new MiningData().setPowers(0, 1, 25).setTimes(0.5, 0.2, 0.05)).setName("wool");
		wool1 = new BlockWool(world.getRegisteredBlock("vanilla.wool.1"), 1).setMineData(new MiningData().setPowers(0, 1, 25).setTimes(0.5, 0.2, 0.05)).setName("wool");
		present = new BlockPresent(world.getRegisteredBlock("vanilla.present")).setName("present");
		explosive = new BlockExplosive(world.getRegisteredBlock("vanilla.explosive")).setName("explosive");
		pressureplate = new BlockPressurePlate(world.getRegisteredBlock("vanilla.pressureplate")).setName("pressureplate");
	}

	@Override
	public void loadItems(World world) {
		generateToolItems(world);
		food = new ItemVanillaFood(world.getRegisteredItem("vanilla.food"));
		stick = new ItemStick(world.getRegisteredItem("vanilla.stick"), ItemTexture.stick).setName("stick");
		metalBar = new ItemMetal(world.getRegisteredItem("vanilla.metalbar"), ItemTexture.metalBar, "bar");
		alloyBar = new ItemAlloy(world.getRegisteredItem("vanilla.alloybar"), ItemTexture.metalBar, "bar");
		crop = new ItemCrop(world.getRegisteredItem("vanilla.crop")).setName("crop");
		seeds = new ItemSeeds(world.getRegisteredItem("vanilla.seeds")).setName("seeds");
		fertilizer = new ItemPlantHelp(world.getRegisteredItem("vanilla.fertilizer"), 0).setName("fertilizer");
		cure = new ItemPlantHelp(world.getRegisteredItem("vanilla.cure"), 1).setName("plantcure");
		wrench = new ItemWrench(world.getRegisteredItem("vanilla.wrench")).setName("wrench");
	}

	@Override
	public void loadItemEntities(World world) {
		ItemEntity.registerEntity("vanilla.chest", ItemEntityChest.class);
	}

	public void loadTrades(World world) {
		tradeBlades = new Trade("blades").addToList();
		tradeArmor = new Trade("armor").addToList();
		tradeHealing = new Trade("healing").addToList();
		tradePotions = new Trade("potions").addToList();
		tradePickaxes = new Trade("pickaxes").addToList();
		tradeGuard = new Trade("guard");
	}

	@Override
	public void loadBiomes(World world) {
		// Definitions
		biomeOcean = new BiomeOcean(world.getRegisteredBiome("vanilla.ocean")).setHeights(oceans);
		biomePlains = new BiomePlains(world.getRegisteredBiome("vanilla.plains"), "plains", BiomeType.normal, Color.green, 256, wheat.uid, 2, barley.uid);
		biomeDesert = new BiomeDesert(world.getRegisteredBiome("vanilla.desert"), "desert", BiomeType.hot, Color.yellow);
		biomePlainsS = new BiomeHilly(world.getRegisteredBiome("vanilla.snowplains"), "snowplains", BiomeType.cold, Color.white);
		biomeForest = new BiomeHilly(world.getRegisteredBiome("vanilla.forest"), "forest", BiomeType.normal, new Color(0, 176, 0));
		biomePlainsH = new BiomePlains(world.getRegisteredBiome("vanilla.plains.hills"), "plains/hills", BiomeType.normal, Color.green, 256, wheat.uid, 2, barley.uid).setHeights(hills);
		biomeDesertH = new Biome(world.getRegisteredBiome("vanilla.desert.hills"), "desert/hills", BiomeType.hot, Color.yellow).setHeights(hills);
		biomePlainsSH = new Biome(world.getRegisteredBiome("vanilla.snowplains.hills"), "snowplains/hills", BiomeType.cold, Color.white).setHeights(hills);
		biomeForestH = new Biome(world.getRegisteredBiome("vanilla.forest.hills"), "forest/hills", BiomeType.normal, Color.green).setHeights(hills);
		biomeVolcanic = new BiomeVolcanic(world.getRegisteredBiome("vanilla.volcanic"), "volcanic", BiomeType.hot, Color.black, 3).setHeights(new BiomeHeightModel(56, 20, 65, 3));
		biomeVolcanicH = new BiomeVolcanic(world.getRegisteredBiome("vanilla.volcanic.hills"), "volcanic/hills", BiomeType.hot, Color.black, 8).setHeights(new BiomeHeightModel(135, 50, 145, 12));
		biomeForestRedwoodS = new BiomeHilly(world.getRegisteredBiome("vanilla.snowforest"), "snowforest", BiomeType.cold, new Color(200, 240, 200));
		biomeForestRedwoodSH = new Biome(world.getRegisteredBiome("vanilla.snowforest.hills"), "snowforest/hills", BiomeType.cold, Color.white).setHeights(hills);
		biomeOceanTrench = new Biome(world.getRegisteredBiome("vanilla.ocean.trench"), "ocean/trench", BiomeType.ocean, Color.blue).setHeights(trenchs);
		biomeRockyH = new Biome(world.getRegisteredBiome("vanilla.rocky.hills"), "rocky/hills", BiomeType.hot, Color.gray);
		biomeDesertOasis = new Biome(world.getRegisteredBiome("vanilla.desert.oasis"), "desert/oasis", BiomeType.hot, new Color(152, 127, 70)).setHeights(new BiomeHeightModel(14, -7, 24, -12));
		biomeSwamp = new Biome(world.getRegisteredBiome("vanilla.swamp"), "swamp", BiomeType.normal, new Color(0xA3AE7E)).setHeights(swampy);
		biomeJungle = new BiomeJungle(world.getRegisteredBiome("vanilla.jungle"), "jungle", BiomeType.hot, new Color(255, 128, 0));
		biomeJungleH = new BiomeJungleHills(world.getRegisteredBiome("vanilla.jungle.hills"), "jungle/hills", BiomeType.hot, new Color(255, 132, 0));
		biomeForestBirch = new BiomeHilly(world.getRegisteredBiome("vanilla.forestbirch"), "birchforest", BiomeType.normal, new Color(0, 230, 0));
		biomeForestBirchH = new Biome(world.getRegisteredBiome("vanilla.forestbirch.hills"), "birchforest/hills", BiomeType.normal, new Color(0, 230, 0));
		biomeForestBamboo = new BiomeForestBamboo(world.getRegisteredBiome("vanilla.forestbamboo"), "bambooforest", BiomeType.normal, new Color(0xC1A156));
		biomeForestBambooH = new BiomeForestBamboo(world.getRegisteredBiome("vanilla.forestbamboo.hills"), "bambooforest/hills", BiomeType.normal, new Color(0xC1A156)).setHeights(hills);
		biomePlainsWheat = new BiomePlains(world.getRegisteredBiome("vanilla.plains.wheat"), "plains/wheat", BiomeType.normal, Color.green, 16, wheat.uid, 5, barley.uid);
		biomePlainsBarley = new BiomePlains(world.getRegisteredBiome("vanilla.plains.barley"), "plains/barley", BiomeType.normal, Color.green, 16, barley.uid, 5, wheat.uid);
		biomePlainsWheatH = new BiomePlains(world.getRegisteredBiome("vanilla.plains.wheat.hills"), "plains/wheat/hills", BiomeType.normal, Color.green, 16, wheat.uid, 5, barley.uid).setHeights(hills);
		biomePlainsBarleyH = new BiomePlains(world.getRegisteredBiome("vanilla.plains.barley.hills"), "plains/barley/hills", BiomeType.normal, Color.green, 16, barley.uid, 5, wheat.uid).setHeights(hills);

		// Set-up
		biomeOcean.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeOcean.addPlant(new PlantGrowth(plantTreeWillow, 5));
		biomeOcean.addPlant(new PlantGrowth(plantReedWater, 30));
		biomeOcean.music = musicSky;
		biomeOceanTrench.addPlant(new PlantGrowth(plantReedWater, 30));
		biomeOceanTrench.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeOceanTrench.music = musicSky;

		biomeDesert.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeDesertH.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeDesert.music = musicDesert;
		biomeDesertH.music = musicDesert;
		biomeDesertOasis.music = musicDesert;
		biomeDesert.addPlant(new PlantGrowth(plantCactus, 3));
		biomeDesertH.addPlant(new PlantGrowth(plantCactus, 3));
		biomeDesertOasis.setBiomeBlocks(grass.uid, dirt.uid, sand.uid);
		biomeDesertOasis.addPlant(new PlantGrowth(plantTreeOak, 3));
		biomeDesert.setHills(biomeDesertH.id);

		biomePlains.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlains.setIcingBlock(weeds.uid);
		biomePlainsH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsH.setIcingBlock(weeds.uid);
		biomePlains.setHills(biomePlainsH.id);
		biomePlainsWheat.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsWheat.setIcingBlock(weeds.uid);
		biomePlainsWheatH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsWheatH.setIcingBlock(weeds.uid);
		biomePlainsWheat.setHills(biomePlainsWheatH.id);
		biomePlainsBarley.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsBarley.setIcingBlock(weeds.uid);
		biomePlainsBarleyH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsBarleyH.setIcingBlock(weeds.uid);
		biomePlainsBarley.setHills(biomePlainsBarleyH.id);

		biomeForest.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForest.addPlant(new PlantGrowth(plantTreeOak, 18));
		biomeForest.addPlant(new PlantGrowth(plantTreeBirch, 12));
		biomeForestH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestH.addPlant(new PlantGrowth(plantTreeOak, 18));
		biomeForestH.addPlant(new PlantGrowth(plantTreeBirch, 12));
		biomeForest.setHills(biomeForestH.id);

		biomeForestBirch.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestBirch.addPlant(new PlantGrowth(plantTreeBirch, 30));
		biomeForestBirchH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestBirchH.addPlant(new PlantGrowth(plantTreeBirch, 30));
		biomeForestBirch.setHills(biomeForestBirchH.id);

		biomePlainsS.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsS.setIcingBlock(snow.uid);
		biomePlainsSH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsSH.setIcingBlock(snow.uid);
		biomePlainsS.setHills(biomePlainsSH.id);

		biomeForestRedwoodS.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestRedwoodS.setIcingBlock(snow.uid);
		biomeForestRedwoodS.addPlant(new PlantGrowth(plantTreeRedwood, 10));
		biomeForestRedwoodSH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestRedwoodSH.setIcingBlock(snow.uid);
		biomeForestRedwoodSH.addPlant(new PlantGrowth(plantTreeRedwood, 10));
		biomeForestRedwoodS.setHills(biomeForestRedwoodSH.id);

		biomeVolcanic.setBiomeBlocks(obsidian.uid, obsidian.uid, obsidian.uid);
		biomeVolcanic.music = musicHell;
		biomeVolcanicH.setBiomeBlocks(obsidian.uid, obsidian.uid, obsidian.uid);
		biomeVolcanicH.music = musicHell;
		biomeVolcanic.setHills(biomeVolcanicH.id);

		biomeRockyH.setBiomeBlocks(Block.stone.uid, Block.stone.uid, Block.stone.uid);
		biomeRockyH.setHeights(mountains);

		biomeSwamp.setIcingBlock(weeds.uid);
		biomeSwamp.addPlant(new PlantGrowth(plantTreeCypress, 9));
		biomeSwamp.addPlant(new PlantGrowth(plantStumpCypress, 9));
		biomeSwamp.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeSwamp.setColors(new Color(0x354C2D), new Color(75, 132, 99, 164), new Color(0x2E3D22));

		biomeJungle.setIcingBlock(weeds.uid);
		biomeJungleH.setIcingBlock(weeds.uid);
		biomeJungle.addPlant(new PlantGrowth(plantSugarcane, 8));
		biomeJungleH.addPlant(new PlantGrowth(plantSugarcane, 8));
		biomeJungle.addPlant(new PlantGrowth(plantTreeKapokBig, 4));
		biomeJungleH.addPlant(new PlantGrowth(plantTreeKapokBig, 4));
		biomeJungle.addPlant(new PlantGrowth(plantBushOak, 130));
		biomeJungleH.addPlant(new PlantGrowth(plantBushOak, 130));
		biomeJungle.addPlant(new PlantGrowth(plantTreeKapok, 20));
		biomeJungleH.addPlant(new PlantGrowth(plantTreeKapok, 20));
		biomeJungle.addPlant(new PlantGrowth(plantTreeOak, 4));
		biomeJungleH.addPlant(new PlantGrowth(plantTreeOak, 4));
		biomeJungle.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeJungleH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeJungle.setColors(new Color(0x2FA81C), new Color(0, 156, 254, 128), new Color(0x2EBB06));
		biomeJungleH.setColors(new Color(0x2FA81C), new Color(0, 156, 254, 128), new Color(0x2EBB06));
		biomeJungle.setHills(biomeJungleH.id);

		biomeForestBamboo.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestBambooH.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestBamboo.addPlant(new PlantGrowth(plantBamboo, 150));
		biomeForestBambooH.addPlant(new PlantGrowth(plantBamboo, 150));
		biomeForestBamboo.setHills(biomeForestBambooH.id);

		loadUnderBiomes(world);
	}

	public void loadUnderBiomes(World world) {
		underStone = new UnderBiome(underBiomes.get("vanilla.stone"), "stone", Color.black).setStoneBlock(Block.stone.uid, 0);
		underCobbleCaverns = new UnderBiome(underBiomes.get("vanilla.cobblecaverns"), "cobblecaverns", Color.green).setStoneBlock(Vanilla.brick.uid, 15).setCaveData(90, 10);

		underBiomesList = new IntList();
		for (int d = 0; d < 10; ++d)
			underBiomesList.add(underStone.id);
		underBiomesList.add(underCobbleCaverns.id);
	}

	public void loadBiomesList() {
		addBiomeToGenerator(BiomeType.ocean, biomeOcean.id, 10);

		addBiomeToGenerator(BiomeType.normal, biomePlains.id, 8);
		addBiomeToGenerator(BiomeType.normal, biomePlainsWheat.id, 1);
		addBiomeToGenerator(BiomeType.normal, biomePlainsBarley.id, 1);
		addBiomeToGenerator(BiomeType.normal, biomeForest.id, 8);
		addBiomeToGenerator(BiomeType.normal, biomeForestBirch.id, 5);
		addBiomeToGenerator(BiomeType.normal, biomeSwamp.id, 10);
		addBiomeToGenerator(BiomeType.normal, biomeForestBamboo.id, 5);

		addBiomeToGenerator(BiomeType.cold, biomePlainsS.id, 10);
		addBiomeToGenerator(BiomeType.cold, biomeForestRedwoodS.id, 10);

		addBiomeToGenerator(BiomeType.hot, biomeDesert.id, 10);
		addBiomeToGenerator(BiomeType.hot, biomeVolcanic.id, 5);
		addBiomeToGenerator(BiomeType.hot, biomeRockyH.id, 5);
		addBiomeToGenerator(BiomeType.hot, biomeJungle.id, 10);
	}

	public void addBiomeToGenerator(BiomeType type, int biomeid, int amount) {
		ArrayList<Integer> bios = biomes.get(type.getId());
		for (int d = 0; d < amount; ++d)
			bios.add(biomeid);
	}

	@Override
	public void loadTileEntities(World world) {
		TileEntity.registerEntity("vanilla.chest", TileEntityChest.class);
	}

	@Override
	public void loadContainers(World world) {
		Container.registerContainer("vanilla.chest", ContainerChest.class);
	}

	@Override
	public void loadCraftingRecipes(World world) {
		CraftingManager.addRecipes(new RecipesBlocks());
	}

	@Override
	public void onWorldLoad(World world) {
		spawnStick = new ItemSpawnWand(world.getRegisteredItem("vanilla.spawnwand"));
		WorldGenOres.onWorldLoad(world);
		Dungeon.onWorldLoad(world);
		trades = world.getDictionary("trades");
		loadTrades(world);
		SpawnControl.addSpawnEntry(new SpawnEntry(100, new BasicSpawner(EntityZombie.class), new AndFilter(new LightFilter(7, 0), new SkylightFilter(false), new HumanoidFilter())));
		loadBiomesList();
	}

	@Override
	public void onWorldSave(World world) {
		try {
			underBiomes.save(new FileOutputStream(DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/underbiomes.dictionary"));
		} catch (FileNotFoundException e) {
		}
		try {
			trades.save(new FileOutputStream(DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/trades.dictionary"));
		} catch (FileNotFoundException e) {
		}
	}

	public ArrayList<ArrayList<Integer>> getBiomes() {
		return biomes;
	}

	public void generateToolItems(World world) {
		for (int d = 0; d < types.length; ++d) {
			for (int f = 0; f < materials.length; ++f) {
				String name = "vanilla." + types[d].getName() + "." + materials[f].getName();
				if (types[d] != ToolType.hoe) new ItemTool(world.getRegisteredItem(name), types[d], materials[f]);
				else new ItemHoe(world.getRegisteredItem(name), materials[f]);
			}
		}
	}

	public static int getRegisteredTrade(String name) {
		return trades.get(name);
	}

	public static String getTradeName(int id) {
		return trades.getName(id);
	}

	@Override
	public String[] getAliases() {
		return new String[] { "vn", "vanilla", "v" };
	}

}
