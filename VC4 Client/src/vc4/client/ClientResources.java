package vc4.client;

import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import vc4.api.client.Client;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.texture.AnimatedTexture;
import vc4.api.gui.Screen;
import vc4.api.yaml.ThreadYaml;
import vc4.client.gui.ClientComponentUtils;
import vc4.impl.ImplResources;
import vc4.impl.gui.ScreenTypeMenu;

public class ClientResources extends ImplResources {

	@SuppressWarnings("unchecked")
	@Override
	protected void loadGui(ArrayList<Entry<String, ArrayList<String>>> lGuis) {
		Game game = (Game) Client.getGame();
		game.loadingScreen = new ClientLoadingScreen(FontRenderer.createFontRenderer("unispaced_14", 14), game.getWindow());
		game.loadingScreen.setLoadingTitle("Loading Gui:");
		new ClientComponentUtils();
		Screen.registerGuiType("menu", ScreenTypeMenu.class);
		for (Entry<String, ArrayList<String>> e : lGuis) {
			ArrayList<Screen> nGuis = new ArrayList<Screen>();
			for (String s : e.getValue()) {
				InputStream i = Game.class.getClassLoader().getResourceAsStream("vc4/resources/gui/" + e.getKey() + "/" + s + ".yml");
				Screen g = new Screen((LinkedHashMap<String, ?>) ThreadYaml.getYamlForThread().load(i));
				game.loadingScreen.setLoadingInfo(e.getKey());
				nGuis.add(g);
			}
			boolean isMenu = e.getKey().equals("menu");
			for (Screen g : nGuis) {
				game.guis.put(g.getName(), g);
				if (isMenu) {
					game.menus.put(g.getName(), ((ScreenTypeMenu) g.getType()).getState());
				}
				game.add(g);
			}
		}
	}

	public void animatedTextureTick(int ticks) {
		for (AnimatedTexture t : getAnimatedTextures().values()) {
			t.updateAnimation(ticks);
		}
	}
}
