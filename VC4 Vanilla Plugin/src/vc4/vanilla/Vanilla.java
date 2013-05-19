/**
 * 
 */
package vc4.vanilla;

import java.awt.Color;

import vc4.api.biome.Biome;
import vc4.api.biome.BiomeType;
import vc4.api.block.*;
import vc4.api.generator.GeneratorList;
import vc4.api.generator.PlantGrowth;
import vc4.api.plugin.Plugin;
import vc4.api.sound.Music;
import vc4.api.sound.MusicType;
import vc4.api.tool.*;
import vc4.api.world.World;
import vc4.vanilla.biome.*;
import vc4.vanilla.block.*;
import vc4.vanilla.generation.*;
import vc4.vanilla.generation.dungeon.Dungeon;
import vc4.vanilla.generation.trees.*;
import vc4.vanilla.item.ItemTool;

/**
 * @author paul
 *
 */
public class Vanilla extends Plugin {

	//Blocks
	public static Block grass, dirt, logV, logX, logZ, leaf, brick, mossBrick;
	public static Block sand, glass, ore, hellrock, lava, oreHell, water, obsidian;
	public static Block planks, planksHalf, bookshelf, planksStairs0, planksStairs4;
	public static Block planksStairs8, planksStairs12, brickStairs0, brickStairs4;
	public static Block brickStairs8, brickStairs12, brickHalf, bookshelfEnchanted;
	public static Block crackedBrick, snow, cactus, weeds, vines, willowVines;
	
	//Items
	
	//Plants
	public static Plant plantTreeOak;
	public static Plant plantTreeBirch;
	public static Plant plantTreeWillow;
	public static Plant plantTreeRedwood;
	public static Plant plantCactus;
	public static Plant plantWeed;
	
	//Tools
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
	
	public static Music musicOverworld = new Music("First_Day", MusicType.BIOME);
	public static Music musicDesert = new Music("desert_winds", MusicType.BIOME);
	public static Music musicHell = new Music("A_Night_Out", MusicType.BIOME);
	public static Music musicSky = new Music("Menu_Screen", MusicType.BIOME);
	
	public static Biome biomeOcean = new Biome("ocean", BiomeType.ocean, Color.blue).setHeights(1, -56);
	public static BiomeHilly biomePlains = new BiomePlains("plains", BiomeType.normal, Color.green);
	public static BiomeHilly biomeDesert = new BiomeHilly("desert", BiomeType.hot, Color.yellow);
	public static BiomeHilly biomeSnowPlains = new BiomeHilly("snowplains", BiomeType.cold, Color.white);
	public static BiomeHilly biomeForest = new BiomeHilly("forest", BiomeType.normal, Color.green);
	public static Biome biomePlainsHills = new BiomePlains("plains/hills", BiomeType.normal, Color.green).setHeights(60, 10);
	public static Biome biomeDesertHills = new Biome("desert/hills", BiomeType.hot, Color.yellow).setHeights(60, 10);
	public static Biome biomeSnowPlainsHills = new Biome("snowplains/hills", BiomeType.cold, Color.white).setHeights(60, 10);
	public static Biome biomeForestHills = new Biome("forest/hills", BiomeType.normal, Color.green).setHeights(60, 10);
	public static BiomeHilly biomeVolcanic = new BiomeVolcanic("volcanic", BiomeType.hot, Color.black, 3).setHeights(50, 3);
	public static Biome biomeVolcano = new BiomeVolcanic("volcanic/hills", BiomeType.hot, Color.black, 8).setHeights(100, 12);
	public static BiomeHilly biomeSnowForest = new BiomeHilly("snowforest", BiomeType.cold, Color.white);
	public static Biome biomeSnowForestHills = new Biome("snowforest/hills", BiomeType.cold, Color.white).setHeights(60, 10);
	
	
	private static ToolMaterial[] materials = new ToolMaterial[]{
		materialWood,
		materialStone,
		materialCopper,
		materialBronze,
		materialIron,
		materialKradonium,
		materialSilver,
		materialGold,
		materialMithril,
		materialTitanium,
		materialHellish,
		materialPlatinum,
		materialAdamantite,
		materialUnholy,
		materialSacred
	};
	
	private static ToolType[] types = new ToolType[]{
		ToolType.pickaxe,
		ToolType.spade,
		ToolType.axe,
		ToolType.hoe
	};
	
	
	/* (non-Javadoc)
	 * @see vc4.api.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		plantTreeOak = new Plant(0, 0, "tree.oak");
		plantTreeBirch = new Plant(0, 1, "tree.birch");
		plantTreeWillow = new Plant(0, 2, "tree.willow");
		plantTreeRedwood = new Plant(0, 5, "tree.redwood");
		plantCactus = new Plant(1, 0, "cactus");
		plantWeed = new Plant(2, 0, "weed");
		OverworldGenerator gen = new OverworldGenerator();
		GeneratorList.registerGenerator("overworld", gen);
		GeneratorList.registerGenerator("flat", new FlatlandsGenerator());
		GeneratorList.registerGenerator("sky", new SkylandGenerator());
		GeneratorList.registerPlantGen(plantTreeOak, new TreeGenBasic());
		GeneratorList.registerPlantGen(plantTreeBirch, new TreeGenBasic());
		GeneratorList.registerPlantGen(plantTreeWillow, new TreeGenWillow());
		GeneratorList.registerPlantGen(plantTreeRedwood, new TreeGenRedwood());
		GeneratorList.registerPlantGen(plantCactus, new PlantGenCactus());
	}

	/* (non-Javadoc)
	 * @see vc4.api.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onWorldLoad(World world) {
		BlockTexture.update();
		ItemTexture.update();
		grass = new BlockGrass(world.getRegisteredBlock("vanilla.grass"), Material.getMaterial("grass")).setName("grass");
		dirt = new Block(world.getRegisteredBlock("vanilla.dirt"), BlockTexture.dirt, Material.getMaterial("dirt")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.22)).setName("dirt");
		logV = new BlockLog(world.getRegisteredBlock("vanilla.log.V"), Material.getMaterial("wood"), 0).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		logX = new BlockLog(world.getRegisteredBlock("vanilla.log.X"), Material.getMaterial("wood"), 1).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		logZ = new BlockLog(world.getRegisteredBlock("vanilla.log.Z"), Material.getMaterial("wood"), 2).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(3, 0.1, 1.25)).setName("log");
		leaf = new BlockLeaf(world.getRegisteredBlock("vanilla.leaf"), Material.getMaterial("leaf")).setName("leaf");
		brick = new BlockBrick(world.getRegisteredBlock("vanilla.brick")).setName("brick");
		mossBrick = new BlockBrickMoss(world.getRegisteredBlock("vanilla.brick.moss")).setName("brick");
		sand = new BlockSand(world.getRegisteredBlock("vanilla.sand")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.20)).setName("sand");
		glass = new BlockGlass(world.getRegisteredBlock("vanilla.glass"), BlockTexture.glass, Material.getMaterial("glass")).setMineData(new MiningData().setPowers(10000000, 1, 50).setTimes(1, 0.09, 0.9)).setName("glass");
		ore = new BlockOre(world.getRegisteredBlock("vanilla.ore"), BlockTexture.stone);
		hellrock = new Block(world.getRegisteredBlock("vanilla.hellrock"), BlockTexture.hellrock, Material.getMaterial("hellrock")).setMineData(new MiningData().setRequired(ToolType.pickaxe).setPowers(1, 1, 25).setTimes(3, 0.08, 0.8)).setName("hellrock");
		lava = new BlockLava(world.getRegisteredBlock("vanilla.lava")).setName("lava");
		oreHell = new BlockOre(world.getRegisteredBlock("vanilla.ore.hell"), BlockTexture.hellrock);
		water = new BlockWater(world.getRegisteredBlock("vanilla.water")).setName("water");
		obsidian = new BlockObsidian(world.getRegisteredBlock("vanilla.obsidian")).setMineData(new MiningData().setRequired(ToolType.pickaxe).setPowers(1, 1, 35).setTimes(10, 0.3, 3)).setName("obsidian");
		planks = new BlockPlanks(world.getRegisteredBlock("vanilla.planks")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planks");
		planksHalf = new BlockPlanksHalf(world.getRegisteredBlock("vanilla.planks.half")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("plankshalf");
		bookshelf = new BlockBookshelf(world.getRegisteredBlock("vanilla.bookshelf")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("bookshelf");
		planksStairs0 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.0"), 0).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs4 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.4"), 4).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs8 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.8"), 8).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		planksStairs12 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.12"), 12).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(0, 1, 25).setTimes(1.5, 0.1, 0.75)).setName("planksstairs");
		brickHalf = new BlockBrickHalf(world.getRegisteredBlock("vanilla.brick.half")).setName("brickhalf");
		brickStairs0 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.0"), 0).setName("brickstairs");
		brickStairs4 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.4"), 4).setName("brickstairs");
		brickStairs8 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.8"), 8).setName("brickstairs");
		brickStairs12 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.12"), 12).setName("brickstairs");
		bookshelfEnchanted = new BlockBookshelfEnchanted(world.getRegisteredBlock("vanilla.bookshelf.enchanted")).setMineData(new MiningData().setRequired(ToolType.axe).setPowers(1, 5, 50).setTimes(3, 0.1, 0.75)).setName("bookshelfenchanted");
		crackedBrick = new BlockBrickCracked(world.getRegisteredBlock("vanilla.brick.cracked")).setName("crackedbrick");
		snow = new BlockSnow(world.getRegisteredBlock("vanilla.snow")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(1, 1, 15).setTimes(0.75, 0.45, 0.08)).setName("snow");
		cactus = new BlockCactus(world.getRegisteredBlock("vanilla.cactus")).setName("cactus");
		weeds = new BlockWeeds(world.getRegisteredBlock("vanilla.weeds")).setName("weeds");
		vines = new BlockVine(world.getRegisteredBlock("vanilla.vine"), BlockTexture.vines, "vine").setName("vine");
		willowVines = new BlockWillowVine(world.getRegisteredBlock("vanilla.willowvine"), BlockTexture.vines, "vine").setName("vine");
		generateToolItems(world);
		biomeOcean.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeOcean.addPlant(new PlantGrowth(plantTreeWillow, 2));
		biomeOcean.music = musicSky;
		biomeDesert.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeDesertHills.setBiomeBlocks(sand.uid, sand.uid, sand.uid);
		biomeDesert.music = musicDesert;
		biomeDesertHills.music = musicDesert;
		biomeDesert.addPlant(new PlantGrowth(plantCactus, 3));
		biomeDesertHills.addPlant(new PlantGrowth(plantCactus, 3));
		biomeDesert.setHills(biomeDesertHills.id);
		biomePlains.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlains.setIcingBlock(weeds.uid);
		biomePlainsHills.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomePlainsHills.setIcingBlock(weeds.uid);
		biomePlains.setHills(biomePlainsHills.id);
		biomeForest.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForest.addPlant(new PlantGrowth(plantTreeOak, 12));
		biomeForest.addPlant(new PlantGrowth(plantTreeBirch, 10));
		biomeForestHills.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeForestHills.addPlant(new PlantGrowth(plantTreeOak, 12));
		biomeForestHills.addPlant(new PlantGrowth(plantTreeBirch, 10));
		biomeForest.setHills(biomeForestHills.id);
		biomeSnowPlains.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeSnowPlains.setIcingBlock(snow.uid);
		biomeSnowForest.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeSnowForest.setIcingBlock(snow.uid);
		biomeSnowForest.addPlant(new PlantGrowth(plantTreeRedwood, 10));
		biomeSnowPlainsHills.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeSnowPlainsHills.setIcingBlock(snow.uid);
		biomeSnowForestHills.setBiomeBlocks(grass.uid, dirt.uid, dirt.uid);
		biomeSnowForestHills.setIcingBlock(snow.uid);
		biomeSnowForestHills.addPlant(new PlantGrowth(plantTreeRedwood, 10));
		biomeSnowPlains.setHills(biomeSnowPlainsHills.id);
		biomeSnowForest.setHills(biomeSnowForestHills.id);
		biomeVolcanic.setBiomeBlocks(obsidian.uid, obsidian.uid, obsidian.uid);
		biomeVolcanic.music = musicHell;
		biomeVolcano.setBiomeBlocks(obsidian.uid, obsidian.uid, obsidian.uid);
		biomeVolcano.music = musicHell;
		biomeVolcanic.setHills(biomeVolcano.id);
		WorldGenOres.onWorldLoad(world);
		Dungeon.onWorldLoad(world);
	}
	
	public void generateToolItems(World world){
		for(int d = 0; d < types.length; ++d){
			for(int f = 0; f < materials.length; ++f){
				String name = "vanilla." + types[d].getName() + "." + materials[f].getName();
				new ItemTool(world.getRegisteredItem(name), types[d], materials[f]);
			}
		}
	}

}
