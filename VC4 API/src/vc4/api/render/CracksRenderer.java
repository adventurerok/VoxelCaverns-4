package vc4.api.render;

import vc4.api.Resources;
import vc4.api.graphics.*;
import vc4.api.util.AABB;

public class CracksRenderer {

	static OpenGL gl;
	private static final double small = 0.002;

	public static void renderCracks(long x, long y, long z, AABB bounds, double mined) {
		if (gl == null) gl = Graphics.getClientOpenGL();
		if(mined > 1) mined = 1;
		if(mined < 0.05) mined = 0.05;
		mined *= 0.95;
		gl.disable(GLFlag.CULL_FACE);
		Graphics.getClientShaderManager().bindShader("texture3d");
		Resources.getAnimatedTexture("cracks").bind();
		gl.begin(GLPrimative.QUADS);
		gl.texCoord(bounds.minZ, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxZ, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(bounds.maxZ, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(bounds.minZ, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);

		gl.texCoord(bounds.minZ, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxZ, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(bounds.maxZ, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(bounds.minZ, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);

		gl.texCoord(bounds.minX, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(bounds.minX, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);

		gl.texCoord(bounds.minX, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(bounds.maxX, 1 -bounds.maxY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(bounds.maxX, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(bounds.minX, 1 - bounds.minY, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);

		gl.texCoord(bounds.minX, 1 - bounds.minZ, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 - bounds.minZ, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 - bounds.maxZ, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(bounds.minX, 1 - bounds.maxZ, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);

		gl.texCoord(bounds.minX, 1 - bounds.minZ, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 - bounds.minZ, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(bounds.maxX, 1 - bounds.maxZ, mined);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(bounds.minX, 1 - bounds.maxZ, mined);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.end();
		Graphics.getClientShaderManager().unbindShader();
	}
}
