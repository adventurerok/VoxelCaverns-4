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
import vc4.api.world.World;
import vc4.vanilla.block.BlockBrick;
import vc4.vanilla.block.BlockBrickMoss;
import vc4.vanilla.block.BlockGlass;
import vc4.vanilla.block.BlockGrass;
import vc4.vanilla.block.BlockLava;
import vc4.vanilla.block.BlockLeaf;
import vc4.vanilla.block.BlockLog;
import vc4.vanilla.block.BlockObsidian;
import vc4.vanilla.block.BlockOre;
import vc4.vanilla.block.BlockSand;
import vc4.vanilla.block.BlockWater;
import vc4.vanilla.generation.FlatlandsGenerator;
import vc4.vanilla.generation.OverworldGenerator;
import vc4.vanilla.generation.WorldGenOres;

/**
 * @author paul
 *
 */
public class Vanilla extends Plugin {

	
	public static Block grass, dirt, logV, logX, logZ, leaf, brick, mossBrick;
	public static Block sand, glass, ore, hellrock, lava, oreHell, water, obsidian;
	public static Plant treeOak;
	
	/* (non-Javadoc)
	 * @see vc4.api.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		treeOak = new Plant(12, 0, "tree.oak");
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
		grass = new BlockGrass(world.getRegisteredBlock("vanilla.grass"), Material.getMaterial("grass")).setName("grass");
		dirt = new Block(world.getRegisteredBlock("vanilla.dirt"), 1, Material.getMaterial("dirt")).setName("dirt");
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
		WorldGenOres.onWorldLoad(world);
	}

}
