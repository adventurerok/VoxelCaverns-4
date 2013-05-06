package vc4.api.render;

import vc4.api.Resources;
import vc4.api.graphics.*;
import vc4.api.util.AABB;

public class CracksRenderer {

	static OpenGL gl;
	private static final double small = 0.002;
	
	public static void renderCracks(long x, long y, long z, AABB bounds, double mined){
		if(gl == null) gl = Graphics.getClientOpenGL();
		int num = (int) Math.min(mined * 7, 6);
		double pos = num / 8d;
		gl.disable(GLFlag.CULL_FACE);
		Graphics.getClientShaderManager().bindShader("texture");
		Resources.getSheetTexture("cracks").bind();
		gl.begin(GLPrimative.QUADS);
		gl.texCoord(pos + bounds.minZ / 8, 1 - bounds.maxY, 0d);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxZ / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.maxZ / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.minZ / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);
		
		gl.texCoord(pos + bounds.minZ / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxZ / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.maxZ / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.minZ / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);
		
		gl.texCoord(pos + bounds.minX / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.minX / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);
		
		gl.texCoord(pos + bounds.minX / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.maxX / 8, 1 - bounds.maxY);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.maxX / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.minX / 8, 1 - bounds.minY);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);
		
		gl.texCoord(pos + bounds.minX / 8, bounds.minZ);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, bounds.minZ);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, bounds.maxZ);
		gl.vertex(x + bounds.minX - small, y + bounds.maxY + small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.minX / 8, bounds.maxZ);
		gl.vertex(x + bounds.maxX + small, y + bounds.maxY + small, z + bounds.minZ - small);
		
		gl.texCoord(pos + bounds.minX / 8, bounds.minZ);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, bounds.minZ);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.maxZ + small);
		gl.texCoord(pos + bounds.maxX / 8, bounds.maxZ);
		gl.vertex(x + bounds.minX - small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.texCoord(pos + bounds.minX / 8, bounds.maxZ);
		gl.vertex(x + bounds.maxX + small, y + bounds.minY - small, z + bounds.minZ - small);
		gl.end();
		Graphics.getClientShaderManager().unbindShader();
	}
}
