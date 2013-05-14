package vc4.client.gui;

import vc4.api.biome.Biome;
import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.api.font.FontRenderer;
import vc4.api.gui.Component;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class ScreenDebug extends Component {

	FontRenderer font = FontRenderer.createFontRenderer("unispaced_14", 14);
	int debugLine;
	
	@Override
	public void draw() {
		debugLine = 0;
		EntityPlayer player = Client.getGame().getBlockInteractor();
		World world = player.getWorld();
		Vector3l blockPos = player.position.toVector3l();
		renderDebugLine("World: " + world.getName() + ", seed: + " + world.getSeed());
		renderDebugLine("X: " + blockPos.x);
		renderDebugLine("Y: " + blockPos.y);
		renderDebugLine("Z: " + blockPos.z);
		Biome biome = world.getBiome(blockPos.x, blockPos.z);
		renderDebugLine("Biome: " + biome.getName());
	}
	
	public void renderDebugLine(String line){
		font.renderString(10, 10 + 16 * debugLine, line);
		++debugLine;
	}
}
