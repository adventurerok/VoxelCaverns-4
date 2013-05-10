package vc4.vanilla.generation;

import java.util.Arrays;

import vc4.api.entity.EntityPlayer;
import vc4.api.generator.GeneratorOutput;
import vc4.api.generator.WorldGenerator;
import vc4.api.graphics.*;
import vc4.api.sound.Music;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class FlatlandsGenerator implements WorldGenerator {

	@Override
	public void onWorldLoad(World world) {
		

	}

	@Override
	public GeneratorOutput generate(World world, long x, long y, long z) {
		GeneratorOutput out = new GeneratorOutput();
		if(y < 0){
			Arrays.fill(out.blocks, world.getGeneratorTag().getShort("blockId", (short)1));
			Arrays.fill(out.data, world.getGeneratorTag().getNibble("blockData", (byte)0));
		}
		return out;
	}
	
	@Override
	public Music getBiomeMusic(EntityPlayer player) {
		return Vanilla.musicOverworld;
	}

	@Override
	public void populate(World world, long x, long y, long z) {
		
	}

	@Override
	public Vector3d getSpawnPoint(World world) {
		return new Vector3d(0, 1, 0);
	}

	@Override
	public void renderSkyBox(World world, EntityPlayer player) {
		Vector3d pos = player.getEyePos();
		OpenGL gl = Graphics.getClientOpenGL();
		gl.disable(GLFlag.DEPTH_TEST);
		Graphics.getClientShaderManager().unbindShader();
		if (pos.y < 192) {
			gl.begin(GLPrimative.QUADS);
			if (pos.y > -5200) gl.color(0.6f, 0.45f, 0.45f, 1);
			else gl.color(0.8f, 0.3f, 0.3f, 1);
			gl.vertex(-256, -256, -256);
			gl.vertex(256, -256, -256);
			gl.vertex(256, -256, 256);
			gl.vertex(-256, -256, 256);
			if (pos.y < -192) {
				gl.vertex(256, -256, -256);
				gl.vertex(256, 256, -256);
				gl.vertex(256, 256, 256);
				gl.vertex(256, -256, 256);

				gl.vertex(-256, -256, -256);
				gl.vertex(-256, 256, -256);
				gl.vertex(-256, 256, 256);
				gl.vertex(-256, -256, 256);

				gl.vertex(-256, -256, 256);
				gl.vertex(-256, 256, 256);
				gl.vertex(256, 256, 256);
				gl.vertex(256, -256, 256);

				gl.vertex(256, -256, -256);
				gl.vertex(256, 256, -256);
				gl.vertex(-256, 256, -256);
				gl.vertex(-256, -256, -256);
				if (pos.y < -256) {
					gl.vertex(-256, 256, -256);
					gl.vertex(256, 256, -256);
					gl.vertex(256, 256, 256);
					gl.vertex(-256, 256, 256);
				}
			}
			gl.end();
		}
	}

}
