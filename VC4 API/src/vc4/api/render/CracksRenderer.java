package vc4.api.render;

import vc4.api.Resources;
import vc4.api.block.Block;
import vc4.api.graphics.*;
import vc4.api.graphics.texture.AnimatedTexture;
import vc4.api.util.AABB;
import vc4.api.world.World;

public class CracksRenderer {

	static OpenGL gl;
	
	static boolean moddedTex;

	public static void renderCracks(World world, long x, long y, long z, AABB bounds, double mined) {
		if (gl == null) gl = Graphics.getOpenGL();
		Block block = world.getBlockType(x, y, z);
		if(block.isAir()) return;
		if(mined > 1) mined = 1;
		if(mined < 0.05) mined = 0.05;
		mined *= 0.95;
		gl.disable(GLFlag.CULL_FACE);
		gl.bindShader("texture3d");
		AnimatedTexture tex = Resources.getAnimatedTexture("cracks");
		tex.bind();
		if(!moddedTex){
			tex.setSmooth(true);
			tex.setMipmap(true);
			moddedTex = true;
		}
		Renderer render = new DataRenderer();
		block.getRenderer().renderBlockCracks(world, x, y, z, render, mined);
		render.compile();
		render.render();
		gl.unbindShader();
	}
}
