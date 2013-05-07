/**
 * 
 */
package vc4.vanilla;

import vc4.api.block.Block;
import vc4.api.block.BlockStone;
import vc4.api.block.Material;
import vc4.api.block.Plant;
import vc4.api.generator.GeneratorList;
import vc4.api.plugin.Plugin;
import vc4.api.tool.*;
import vc4.api.world.World;
import vc4.vanilla.block.*;
import vc4.vanilla.generation.FlatlandsGenerator;
import vc4.vanilla.generation.OverworldGenerator;
import vc4.vanilla.generation.WorldGenOres;
import vc4.vanilla.generation.dungeon.Dungeon;
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
	
	//Items
	
	//Plants
	public static Plant plantTreeOak;
	
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
		plantTreeOak = new Plant(12, 0, "tree.oak");
		OverworldGenerator gen = new OverworldGenerator();
		GeneratorList.registerGenerator("overworld", gen);
		GeneratorList.registerGenerator("flat", new FlatlandsGenerator());
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
		dirt = new Block(world.getRegisteredBlock("vanilla.dirt"), 1, Material.getMaterial("dirt")).setMineData(new MiningData().setRequired(ToolType.spade).setPowers(0, 2, 20).setTimes(0.45, 0.01, 0.22)).setName("dirt");
		logV = new BlockLog(world.getRegisteredBlock("vanilla.log.V"), Material.getMaterial("wood"), 0).setName("log");
		logX = new BlockLog(world.getRegisteredBlock("vanilla.log.X"), Material.getMaterial("wood"), 1).setName("log");
		logZ = new BlockLog(world.getRegisteredBlock("vanilla.log.Z"), Material.getMaterial("wood"), 2).setName("log");
		leaf = new BlockLeaf(world.getRegisteredBlock("vanilla.leaf"), Material.getMaterial("leaf")).setName("leaf");
		brick = new BlockBrick(world.getRegisteredBlock("vanilla.brick")).setName("brick");
		mossBrick = new BlockBrickMoss(world.getRegisteredBlock("vanilla.brick.moss")).setName("brick");
		sand = new BlockSand(world.getRegisteredBlock("vanilla.sand")).setName("sand");
		glass = new BlockGlass(world.getRegisteredBlock("vanilla.glass"), 3, Material.getMaterial("glass")).setName("glass");
		ore = new BlockOre(world.getRegisteredBlock("vanilla.ore"), 14);
		hellrock = new BlockStone(world.getRegisteredBlock("vanilla.hellrock"), 21, Material.getMaterial("hellrock")).setName("hellrock");
		lava = new BlockLava(world.getRegisteredBlock("vanilla.lava")).setName("lava");
		oreHell = new BlockOre(world.getRegisteredBlock("vanilla.ore.hell"), 21);
		water = new BlockWater(world.getRegisteredBlock("vanilla.water")).setName("water");
		obsidian = new BlockObsidian(world.getRegisteredBlock("vanilla.obsidian")).setName("obsidian");
		planks = new BlockPlanks(world.getRegisteredBlock("vanilla.planks")).setName("planks");
		planksHalf = new BlockPlanksHalf(world.getRegisteredBlock("vanilla.planks.half")).setName("plankshalf");
		bookshelf = new BlockBookshelf(world.getRegisteredBlock("vanilla.bookshelf")).setName("bookshelf");
		planksStairs0 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.0"), 0).setName("planksstairs");
		planksStairs4 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.4"), 4).setName("planksstairs");
		planksStairs8 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.8"), 8).setName("planksstairs");
		planksStairs12 = new BlockPlanksStairs(world.getRegisteredBlock("vanilla.planks.stairs.12"), 12).setName("planksstairs");
		brickHalf = new BlockBrickHalf(world.getRegisteredBlock("vanilla.brick.half")).setName("brickhalf");
		brickStairs0 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.0"), 0).setName("brickstairs");
		brickStairs4 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.4"), 4).setName("brickstairs");
		brickStairs8 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.8"), 8).setName("brickstairs");
		brickStairs12 = new BlockBrickStairs(world.getRegisteredBlock("vanilla.brick.stairs.12"), 12).setName("brickstairs");
		bookshelfEnchanted = new BlockBookshelfEnchanted(world.getRegisteredBlock("vanilla.bookshelf.enchanted")).setName("bookshelfenchanted");
		
		generateToolItems(world);
		
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
