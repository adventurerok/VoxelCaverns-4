/**
 * 
 */
package vc4.api.generator;

import vc4.api.entity.EntityPlayer;
import vc4.api.sound.Music;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public interface WorldGenerator {

	public void onWorldLoad(World world);
	public GeneratorOutput generate(World world, long x, long y, long z);
	public void populate(World world, long x, long y, long z);
	public Vector3d getSpawnPoint(World world);
	public Music getBiomeMusic(EntityPlayer player);
	public void renderSkyBox(World world, EntityPlayer player);
}
