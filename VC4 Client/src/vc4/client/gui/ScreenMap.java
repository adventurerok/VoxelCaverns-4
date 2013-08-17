package vc4.client.gui;

import java.awt.Color;
import java.nio.ByteBuffer;

import vc4.api.biome.Biome;
import vc4.api.client.Client;
import vc4.api.graphics.*;
import vc4.api.gui.Component;
import vc4.api.gui.TextBox;
import vc4.api.input.Input;
import vc4.api.input.Key;
import vc4.api.util.BufferUtils;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class ScreenMap extends Component {

	
	
	private Vector2l oldPos;
	
	int texture = 0;
	
	int size = 256;
	int oldZoom = 8;
	int zoom = 8;
	private int[] biomeMap = new int[size * size];
	
	@Override
	public void draw() {
		if(((Component)Client.getGame()).getFocusComponent() instanceof TextBox) return;
		if(!Input.getClientKeyboard().isKeyDown(Key.M)) return;
		oldZoom = zoom;
		if(Input.getClientKeyboard().keyPressed(Key.EQUALS) && zoom > 0) --zoom;
		if(Input.getClientKeyboard().keyPressed(Key.MINUS) && zoom < 10) ++zoom;
		int zadd = zoom;
		int step = 1 << (zoom - 1);
		OpenGL gl = Graphics.getOpenGL();
		Vector3l pposl = Client.getPlayer().getBlockPos();
		Vector2l pos = new Vector2l((pposl.x - (step * size)) >> zadd, (pposl.z - (step * size)) >> zadd);
		if(oldZoom != zoom || !pos.equals(oldPos)){
			World world = Client.getPlayer().getWorld();
			biomeMap = world.getGenerator().getBiomeMapGenerator(world, zoom).generate(pos.x, pos.y, size);
			ByteBuffer data = BufferUtils.createByteBuffer(size * size * 3);
			for(int y = 0; y < size; ++y){
				for(int x = 0; x < size; ++x){
					Color col = Biome.byId(biomeMap[y * size + x]).mapColor;
					data.put((byte) col.getRed());
					data.put((byte) col.getGreen());
					data.put((byte) col.getBlue());
				}
			}
			data.flip();
			if(texture == 0) texture = gl.genTextures();
			gl.bindTexture(GLTexture.TEX_2D, texture);
			gl.texImage2D(GLTexture.TEX_2D, 0, GLInternalFormat.RGB8, size, size, false, GLFormat.RGB, GLType.UNSIGNED_BYTE, data);
			gl.texParameterMagFilter(GLTexture.TEX_2D, GLTextureFilter.LINEAR);
			gl.texParameterMinFilter(GLTexture.TEX_2D, GLTextureFilter.LINEAR, null);
		}
		gl.unbindShader();
		gl.enable(GLFlag.TEXTURE_2D);
		gl.bindTexture(GLTexture.TEX_2D, texture);
		int x = getParent().getWidth() / 2 - 256;
		int y = getParent().getHeight() / 2 - 256;
		gl.begin(GLPrimative.QUADS);
		gl.color(1, 1, 1, 0.75f);
		gl.texCoord(1, 0);
		gl.vertex(x, y);
		gl.texCoord(1, 1);
		gl.vertex(x + 512, y);
		gl.texCoord(0, 1);
		gl.vertex(x + 512, y + 512);
		gl.texCoord(0, 0);
		gl.vertex(x, y + 512);
		gl.end();
		gl.bindTexture(GLTexture.TEX_2D_ARRAY, 0);
		gl.disable(GLFlag.TEXTURE_2D);
	}
	

	
	@Override
	public boolean isClickable() {
		return false;
	}
}
