/**
 * 
 */
package vc4.api.generator;

import java.util.ArrayList;

import vc4.api.biome.ZoomGenerator;
import vc4.api.entity.EntityPlayer;
import vc4.api.gui.MapIcon;
import vc4.api.sound.Music;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3f;
import vc4.api.world.MapData;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public interface WorldGenerator {

	public void onWorldLoad(World world);
	public GeneratorOutput generate(World world, long x, long y, long z, MapData map);
	public void populate(World world, long x, long y, long z);
	public Vector3d getSpawnPoint(World world);
	public Music getBiomeMusic(EntityPlayer player);
	public void renderSkyBox(World world, EntityPlayer player);
	public void generateMapData(World world, MapData data);
	public boolean generatePlants(World world, long x, long y, long z);
	public boolean hasBiomeMapGenerator(World world);
	public abstract ZoomGenerator getBiomeMapGenerator(World world, int zoom);
	public abstract ArrayList<MapIcon> getMapIcons(World world, long x, long z, int size);
	public abstract Vector3f getLightColor(World world, MapData m, long x, long y, long z, int cx, int cz, int level);
}
