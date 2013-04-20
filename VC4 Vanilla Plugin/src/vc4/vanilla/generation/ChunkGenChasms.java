/**
 * 
 */
package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.client.Client;
import vc4.api.generator.GeneratorOutput;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class ChunkGenChasms {

	public void generate(World world, long x, long y, long z, GeneratorOutput data){
		Random rand = world.createRandom(x, z, 887165761L);
		if(y < -186) return;
		if(rand.nextInt(Client.debugMode() ? 100 : 1000) != 0) return;
		int cx = 16;
		int cz = 16;
		long px = (x << 5) + cx;
		long pz = (z << 5) + cz;
		long py = y << 5;
		SimplexOctaveGenerator noise = new SimplexOctaveGenerator(world, 2);
		noise.setScale(1/15f);
		for(int cy = 31; cy >= 0; --cy){
			int nx = (int) (cx + noise.noise(py + cy, pz, 0.1f, 6));
			if(nx < 6)  nx = 6;
			else if(nx > 26) nx = 26;
			int nz = (int) (cz + noise.noise(px, py + cy, 0.1f, 6));
			if(nz < 6)  nz = 6;
			else if(nz > 26) nz = 26;
			for(int ax = nx - 7; ax < nx + 8; ++ax){
				int qx = Math.abs(ax - nx);
				for(int az = nz - 7; az < nz + 8; ++az){
					int qz = Math.abs(az - nz);
					double d = 1 - ((qx * qz) / 18d);
					if(noise.noise(px + ax, py + cy, pz + az, 0.1f, 1, true) < d){
						data.setBlockId(ax, cy, az, 0);
					}
				}
			}
		}
	}
}
