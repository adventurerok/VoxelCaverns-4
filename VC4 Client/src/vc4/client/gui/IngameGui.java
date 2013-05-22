/**
 * 
 */
package vc4.client.gui;

import java.awt.Color;

import vc4.api.GameState;
import vc4.api.Resources;
import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.*;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.vector.Vector2f;
import vc4.client.Window;
import vc4.impl.gui.GuiCreative;
import vc4.impl.gui.GuiInventory;

/**
 * @author paul
 *
 */
public class IngameGui extends Component {

	OpenGL gl;
	
	GuiInventory invGui;
	GuiCreative creativeGui;
	ScreenDebug debug;
	
	FontRenderer font;
	
	/**
	 * 
	 */
	public IngameGui() {
		invGui = new GuiInventory();
		add(invGui);
		creativeGui = new GuiCreative();
		add(creativeGui);
		debug = new ScreenDebug();
		add(debug);
		add(new OverlayRenderer());
		font = FontRenderer.createFontRenderer("unispaced_14", 14);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#draw()
	 */
	@Override
	public void draw() {
		if(!isVisible()) return;
		super.draw();
		ColorScheme scheme = Client.getGame().getColorScheme(Client.getGame().getCurrentColorScheme().toString());
		EntityPlayer player = Client.getGame().getPlayer();
		gl = Graphics.getClientOpenGL();
		Graphics.getClientShaderManager().bindShader("texture");
		Resources.getSheetTexture("gui").bind();
		{
			int sx = Window.getClientWindow().getWidth() / 2 - 176;
			int sy = Window.getClientWindow().getHeight() - (player.getInventory().isOpen() ? 128 : 32) - 17;
			float health = player.health / (float) player.getMaxHealth();
			float healing = player.healing / (float) player.getMaxHealing();
			float x1 = 352/512f;
			float x2 = 176/512f;
			float y1 = 16/512f;
			float y2 = 32/512f;
			gl.begin(GLPrimative.QUADS);
			gl.color(Color.red);
			gl.texCoord(0, y1, 0);
			gl.vertex(sx, sy);
			gl.texCoord(x2 * health, y1, 0);
			gl.vertex(sx + 176 * health, sy);
			gl.texCoord(x2 * health, y2, 0);
			gl.vertex(sx + 176 * health, sy + 16);
			gl.texCoord(0, y2, 0);
			gl.vertex(sx, sy + 16);
			
			gl.color(Color.white);
			gl.texCoord(x2 * health, y1, 0);
			gl.vertex(sx + 176 * health, sy);
			gl.texCoord(x2, y1, 0);
			gl.vertex(sx + 176, sy);
			gl.texCoord(x2, y2, 0);
			gl.vertex(sx + 176, sy + 16);
			gl.texCoord(x2 * health, y2, 0);
			gl.vertex(sx + 176 * health, sy + 16);
			
			gl.color(Color.green);
			gl.texCoord(x1, y1, 0);
			gl.vertex(sx + 352, sy);
			gl.texCoord(x1 - x2 * healing, y1, 0);
			gl.vertex(sx + 352 - 176 * healing, sy);
			gl.texCoord(x1 - x2 * healing, y2, 0);
			gl.vertex(sx + 352 - 176 * healing, sy + 16);
			gl.texCoord(x1, y2, 0);
			gl.vertex(sx + 352, sy + 16);
			
			gl.color(Color.white);
			gl.texCoord(x1 - x2 * healing, y1, 0);
			gl.vertex(sx + 352 - 176 * healing, sy);
			gl.texCoord(x2, y1, 0);
			gl.vertex(sx + 176, sy);
			gl.texCoord(x2, y2, 0);
			gl.vertex(sx + 176, sy + 16);
			gl.texCoord(x1 - x2 * healing, y2, 0);
			gl.vertex(sx + 352 - 176 * healing, sy + 16);
			
			
			gl.color(scheme.backgroundNormal);
			gl.texCoord(0, 0, 0);
			gl.vertex(sx, sy);
			gl.texCoord(x1, 0, 0);
			gl.vertex(sx + 352, sy);
			gl.texCoord(x1, y1, 0);
			gl.vertex(sx + 352, sy + 16);
			gl.texCoord(0, y1, 0);
			gl.vertex(sx, sy + 16);
			gl.end();
			
			float size = 13;
			String hp = player.health + "/" + player.getMaxHealth();
			Vector2f hpLength = font.measureString(hp, size);
			String he = player.healing + "/" + player.getMaxHealing();
			Vector2f heLength = font.measureString(he, size);
			float px = sx + 88 - hpLength.x / 2;
			float py = sy + 8 - hpLength.y / 2;
			font.renderString(px, py, "{c:0}" + hp, size);
			px = sx + 264 - heLength.x / 2;
			py = sy + 8 - heLength.y / 2;
			font.renderString(px, py, "{c:0}" + he, size);
		}
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#update()
	 */
	@Override
	public void update() {
		if(!isVisible()) return;
		super.update();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return Client.getGame().getGameState() == GameState.SINGLEPLAYER;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isClickable()
	 */
	@Override
	public boolean isClickable() {
		return isVisible();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#getBorderToAttach()
	 */
	@Override
	public Border getBorderToAttach() {
		return Border.FILL;
	}
}
