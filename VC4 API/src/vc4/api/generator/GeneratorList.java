/**
 * 
 */
package vc4.api.generator;

import java.util.HashMap;

import vc4.api.block.Plant;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class GeneratorList {

	private static HashMap<String, WorldGenerator> generators = new HashMap<>();
	private static HashMap<Plant, PlantGenerator> plantGens = new HashMap<>();
	private static WorldGenerator defaultGen;
	
	public static void registerGenerator(String name, WorldGenerator generator){
		generators.put(name, generator);
	}
	
	public static void registerPlantGen(Plant plant, PlantGenerator gen){
		plantGens.put(plant, gen);
	}
	
	public static WorldGenerator getWorldGenerator(String name){
		return generators.get(name);
	}
	
	/**
	 * @param defaultGen the defaultGen to set
	 */
	public static void setDefaultGenerator(WorldGenerator defaultGen) {
		GeneratorList.defaultGen = defaultGen;
	}
	
	/**
	 * @return the defaultGen
	 */
	public static WorldGenerator getDefaultGenerator() {
		return defaultGen;
	}
	
	public static void onWorldLoad(World world){
		for(WorldGenerator g : generators.values()){
			g.onWorldLoad(world);
		}
		for(PlantGenerator g : plantGens.values()){
			g.onWorldLoad(world);
		}
	}
	
	public static PlantGenerator getPlantGenerator(Plant plant){
		return plantGens.get(plant);
	}
}
