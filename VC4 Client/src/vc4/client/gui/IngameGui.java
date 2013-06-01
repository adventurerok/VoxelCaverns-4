/**
 * 
 */
package vc4.client.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import vc4.api.GameState;
import vc4.api.Resources;
import vc4.api.block.OpenContainer;
import vc4.api.client.Client;
import vc4.api.client.ClientWindow;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.trait.TraitOpenContainers;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.*;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.vector.Vector2f;
import vc4.client.Window;
import vc4.impl.gui.*;

/**
 * @author paul
 *
 */
public class IngameGui extends Component {

	private HashMap<OpenContainer, GuiOpenContainer> ocGui = new HashMap<>();
	
	OpenGL gl;
	
	GuiInventory invGui;
	GuiCreative creativeGui;
	GuiArmour armourGui;
	GuiGame gameGui;
	ScreenDebug debug;
	GuiCrafting craftingGui;
	OverlayRenderer overlay;
	
	FontRenderer font;
	
	/**
	 * 
	 */
	public IngameGui() {
		setResizer(new BorderResizer(Border.FILL));
		setBounds(new Rectangle(0, 0, ClientWindow.getClientWindow().getWidth(), ClientWindow.getClientWindow().getHeight()));
		invGui = new GuiInventory();
		add(invGui);
		creativeGui = new GuiCreative();
		add(creativeGui);
		debug = new ScreenDebug();
		add(debug);
		craftingGui = new GuiCrafting();
		craftingGui.setVisible(false);
		craftingGui.setBounds(craftingGui.getDefaultBounds());
		add(craftingGui);
		armourGui = new GuiArmour();
		armourGui.setVisible(false);
		armourGui.setBounds(armourGui.getDefaultBounds());
		add(armourGui);
		gameGui = new GuiGame();
		gameGui.setVisible(false);
		gameGui.setBounds(gameGui.getDefaultBounds());
		add(gameGui);
		overlay = new OverlayRenderer();
		font = FontRenderer.createFontRenderer("unispaced_14", 14);
	}
	
	public void toggleVisibility(String guiName){
		if(guiName.equals("crafting")) craftingGui.setVisible(!craftingGui.isVisible());
		else if(guiName.equals("armour")) armourGui.setVisible(!armourGui.isVisible());
		else if(guiName.equals("game")) gameGui.setVisible(!gameGui.isVisible());
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#draw()
	 */
	@Override
	public void draw() {
		if(!isVisible()) return;
		ColorScheme scheme = Client.getGame().getColorScheme(Client.getGame().getColorSchemeSetting().toString());
		EntityPlayer player = Client.getGame().getPlayer();
		gl = Graphics.getOpenGL();
		Graphics.getClientShaderManager().bindShader("texture");
		Resources.getSheetTexture("gui").bind();
		{
			int sx = Window.getClientWindow().getWidth() / 2 - 176;
			int sy = Window.getClientWindow().getHeight() - (player.getInventory().isOpen() ? 128 : 32) - 17;
			float health = player.health / (float) player.getMaxHealth();
			if(health < 0) health = 0;
			else if(health > 1) health = 1;
			float healing = player.healing / (float) player.getMaxHealing();
			Color healColor = Color.green;
			if(healing < 0){
				healing = 1;
				healColor = Color.yellow;
			}
			else if(healing > 1) healing = 1;
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
			
			gl.color(healColor);
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
		super.draw();
		overlay.draw();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#update()
	 */
	@Override
	public void update() {
		if(!isVisible()) return;
		TraitOpenContainers oc = (TraitOpenContainers) Client.getPlayer().getTrait("opencontainers");
		//HashMap<OpenContainer, GuiOpenContainer> nGui = new HashMap<>();
		for(OpenContainer o : oc.getContainers()){
			GuiOpenContainer g = ocGui.get(o);
			if(g == null){
				g = GuiOpenContainer.createContainerGui(o);
				add(g);
				g.setBounds(g.getDefaultBounds());
				g.resized();
			}
			ocGui.put(o, g);
		}
		ArrayList<OpenContainer> toRem = new ArrayList<>(ocGui.keySet());
		toRem.removeAll(oc.getContainers());
		for(OpenContainer o : toRem){
			GuiOpenContainer g = ocGui.remove(o);
			remove(g);
		}
		//ocGui = nGui;
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
	
}
