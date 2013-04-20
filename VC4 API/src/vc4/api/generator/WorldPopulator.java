/**
 * 
 */
package vc4.api.generator;

import vc4.api.world.World;

/**
 * @author paul
 *
 */
public interface WorldPopulator {

	
	public void populate(World world, long x, long y, long z);
}
