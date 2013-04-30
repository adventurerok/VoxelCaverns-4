/**
 * 
 */
package vc4.client;

import java.awt.Rectangle;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import vc4.api.GameState;
import vc4.api.client.*;
import vc4.api.entity.EntityPlayer;
import vc4.api.font.Font;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.graphics.shader.ShaderManager;
import vc4.api.graphics.texture.Texture;
import vc4.api.graphics.texture.TextureLoader;
import vc4.api.gui.Component;
import vc4.api.gui.Gui;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.*;
import vc4.api.logging.Logger;
import vc4.api.util.DirectoryLocator;
import vc4.api.util.Setting;
import vc4.api.world.ChunkPos;
import vc4.api.yaml.ThreadYaml;
import vc4.client.camera.PlayerController;
import vc4.client.gui.*;
import vc4.impl.gui.GuiTypeMenu;
import vc4.impl.plugin.PluginLoader;
import vc4.impl.world.ImplWorld;

/**
 * @author paul
 * 
 */
public class Game extends Component implements ClientGame {

	private static OpenGL gl;
	
	private Window window;
	private HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private LinkedHashMap<String, Font> fonts = new LinkedHashMap<String, Font>();
	private LinkedHashMap<String, ColorScheme> colorSchemes = new LinkedHashMap<String, ColorScheme>();
	private LinkedHashMap<String, Integer> crosshairs = new LinkedHashMap<>();
	private LinkedHashMap<String, Setting<Object>> settings = new LinkedHashMap<String, Setting<Object>>();
	private HashMap<String, Long> menus = new HashMap<String, Long>();
	private HashMap<String, Gui> guis = new HashMap<String, Gui>();
	private ClientLoadingScreen loadingScreen;
	private GameState gameState = GameState.MENU;
	private EntityPlayer player;
	private boolean paused = false;
	private IngameGui ingameGui;

	/**
	 * @return the paused
	 */
	@Override
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	private Component _oldc;
	private Component _currentc;
	private Component _mouseupc;

	private MouseSet mouseSet = new MouseSet();

	private long _lastFrame = 0;
	private double delta = 0;

	private long lastMenu2 = 0;
	private long lastMenu1 = 0;
	private long lastMenu = 0;
	private long currentMenu = 0;

	private ImplWorld world;
	private PlayerController camera;

	ChatBox chatBox;

	/**
	 * 
	 */
	public Game(Window window) {
		Client.setGame(this);
		this.window = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#draw()
	 */
	@Override
	public void draw() {
		if (gameState == GameState.SINGLEPLAYER) {
			getWindow().enterRenderMode(RenderType.GAME);
			camera.rotate();
			world.drawBackground(camera.getPosition());
			camera.translate();
			world.draw(camera.getPosition());
			player.drawCube();
		}
		gl.disable(GLFlag.DEPTH_TEST);
		getWindow().enterRenderMode(RenderType.GUI);
		if(gameState == GameState.SINGLEPLAYER && !isPaused()){
			Graphics.getClientShaderManager().bindShader("texture");
			getTexture("crosshair").bind();
			int w = getWindow().getWidth() / 2 - 64;
			int h = getWindow().getHeight() / 2 - 64;
			int tex = getCrosshair(getCurrentCrosshair().getString());
			gl.begin(GLPrimative.QUADS);
			gl.color(1, 1, 1, 1);
			gl.texCoord(0, 0, tex);
			gl.vertex(w, h);
			gl.texCoord(1, 0, tex);
			gl.vertex(w + 128, h);
			gl.texCoord(1, 1, tex);
			gl.vertex(w + 128, h + 128);
			gl.texCoord(0, 1, tex);
			gl.vertex(w, h + 128);
			gl.end();
		}
		super.draw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#update()
	 */
	@Override
	public void update() {
		Input.getClientKeyboard().update();
		Input.getClientMouse().update();
		mouseSet.update();
		_oldc = _currentc;
		_currentc = getHovering(mouseSet.getMouseRectangle());
		int x = mouseSet.getCurrent().getX();
		int y = mouseSet.getCurrent().getY();

		if (_oldc != _currentc) {
			if (_oldc != null) {
				_oldc.fireMouseEvent("exited", -1, x, y);
			}
			if (_currentc != null) {
				_currentc.fireMouseEvent("entered", -1, x, y);
			}
		}
		if (mouseSet.buttonPressed(0)) {
			if (_currentc != null) {
				_currentc.fireMouseEvent("pressed", 0, x, y);
				_mouseupc = _currentc;
			}
		} else if (mouseSet.buttonReleased(0)) {
			if (_mouseupc != _currentc) _currentc.fireMouseEvent("released", 0, x, y);
			if (_currentc != null) _currentc.fireMouseEvent("released", 0, x, y);

			if (_oldc != _currentc) _oldc.setFocus(false);
			if (_currentc != null) _currentc.setFocus(true);

			if (_currentc != null) _currentc.fireMouseEvent("clicked", 0, x, y);
		}
		if (mouseSet.buttonPressed(1)) {
			if (_currentc != null) _currentc.fireMouseEvent("pressed", 1, x, y);
		} else if (mouseSet.buttonReleased(1)) {
			if (_mouseupc != _currentc) _currentc.fireMouseEvent("released", 1, x, y);
			if (_currentc != null) _currentc.fireMouseEvent("released", 1, x, y);

			if (_oldc != _currentc) _oldc.setFocus(false);
			_currentc.setFocus(true);

			if (_currentc != null) _currentc.fireMouseEvent("clicked", 1, x, y);
		}
		super.update();
		if (gameState == GameState.SINGLEPLAYER) {
			if(Input.getClientKeyboard().keyPressed(Key.ESCAPE)) setPaused(!isPaused());
			camera.handleInput(delta);
			player.decreaseCooldown(delta);
			player.rayTrace(camera.getLook(), 10);
			if(!isPaused()){
				if (mouseSet.getCurrent().leftButtonPressed()) player.leftMouseDown(delta);
				if (mouseSet.getCurrent().rightButtomPressed()) player.rightMouseDown(delta);
			}
			player.updateInput();
			world.update(camera.getPosition(), delta);
		}
		long time = System.nanoTime() - _lastFrame;
		_lastFrame = System.nanoTime();
		if (time > 0) delta = time / 1000000D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#load()
	 */
	@Override
	public void load() {
		setBounds(getBounds());
		loadResources();
		loadSettings();
		currentMenu = menus.get("main");
		add(chatBox = new ChatBox());
		PluginLoader.loadAndEnablePlugins();
		world = new ImplWorld("World");
		try {
			world.save();
		} catch (IOException e) {
			Logger.getLogger(Game.class).warning("Exception occured", e);
		}
		player = new EntityPlayer(world);
		player.setPosition(0, 20, 0);
		world.generateChunk(ChunkPos.createFromWorldVector(player.position));
		player.addToWorld();
		world.addPlayer(player);
		camera = new PlayerController(player);
		gl = Graphics.getClientOpenGL();
		ingameGui = new IngameGui();
		add(ingameGui);
		ingameGui.resized();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#unload()
	 */
	@Override
	public void unload() {
		saveSettingsFile(DirectoryLocator.getPath() + "/settings/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#resized()
	 */
	@Override
	public void resized() {
		setBounds(getBounds());
		super.resized();
	}

	/**
	 * @return the window
	 */
	@Override
	public Window getWindow() {
		return window;
	}

	@Override
	public Texture getTexture(String name) {
		return textures.get(name);
	}

	@Override
	public Font getFont(String name) {
		return fonts.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#addComponent(vc4.api.gui.Component)
	 */
	@Override
	public void addComponent(Component c) {
		super.add(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component#getBounds()
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle(0, 0, window.getWidth(), window.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getMouseSet()
	 */
	@Override
	public MouseSet getMouseSet() {
		return mouseSet;
	}

	public double getUpdateDelta() {
		return delta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component#action(java.lang.String)
	 */
	@Override
	public void action(String action) {
		Logger.getLogger("VC4").info("Recieved action: " + action);
		if (action.equals("quit")) {
			window.close();
			return;
		} else if (action.startsWith("goto:")) {
			String where = action.substring(5);
			long loc = menus.get(where);
			if (loc == lastMenu) action = "back";
			else {
				lastMenu2 = lastMenu1;
				lastMenu1 = lastMenu;
				lastMenu = currentMenu;
				currentMenu = loc;
				return;
			}
		} else if (action.startsWith("gs:")) {
			String where = action.substring(3);
			try {
				GameState n = GameState.valueOf(where.toUpperCase());
				if (n == null) return;
				gameState = n;
			} catch (Exception e) {
				return;
			}
		}
		if (action.equals("back")) {
			currentMenu = lastMenu;
			lastMenu = lastMenu1;
			lastMenu1 = lastMenu2;
			lastMenu2 = 0;
		} else if(action.startsWith("setting:")){
			String type = action.substring(8);
			if (type.equals("colorscheme:next")) {
				List<String> list = new ArrayList<String>(colorSchemes.keySet());
				int n = list.indexOf(getCurrentColorScheme().getString()) + 1;
				if (n >= list.size()) n = 0;
				changeSetting("colorscheme", list.get(n));
			} else if (type.equals("colorscheme:last")) {
				List<String> list = new ArrayList<String>(colorSchemes.keySet());
				int n = list.indexOf(getCurrentColorScheme().getString()) - 1;
				if (n < 0) n = list.size() - 1;
				changeSetting("colorscheme", list.get(n));
			} else if(type.equals("crosshair:next")){
				List<String> list = new ArrayList<String>(crosshairs.keySet());
				int n = list.indexOf(getCurrentCrosshair().getString()) + 1;
				if (n >= list.size()) n = 0;
				changeSetting("crosshair", list.get(n));
			} else if(type.equals("crosshair:last")){
				List<String> list = new ArrayList<String>(crosshairs.keySet());
				int n = list.indexOf(getCurrentCrosshair().getString()) - 1;
				if (n < 0) n = list.size() - 1;
				changeSetting("crosshair", list.get(n));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getColorScheme(java.lang.String)
	 */
	@Override
	public ColorScheme getColorScheme(String name) {
		return colorSchemes.get(name);
	}
	
	@Override
	public int getCrosshair(String name){
		return crosshairs.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getColorSchemeNames()
	 */
	@Override
	public String[] getColorSchemeNames() {
		List<String> list = new ArrayList<String>(colorSchemes.keySet());
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			result[i] = list.get(i);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getFontNames()
	 */
	@Override
	public String[] getFontNames() {
		List<String> list = new ArrayList<String>(fonts.keySet());
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			result[i] = list.get(i);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void loadResources() {
		InputStream resourceList = getClass().getClassLoader().getResourceAsStream("vc4/resources/resources.yml");
		Yaml yaml = ThreadYaml.getYamlForThread();
		LinkedHashMap<String, Object> doc = (LinkedHashMap<String, Object>) yaml.load(resourceList);
		ArrayList<String> lTextures = new ArrayList<String>();
		ArrayList<String> lShaders = new ArrayList<String>();
		ArrayList<String> lFonts = new ArrayList<String>();
		ArrayList<Entry<String, ArrayList<String>>> lGuis = new ArrayList<Entry<String, ArrayList<String>>>();
		for (Entry<String, Object> e : doc.entrySet()) {
			if (e.getKey().equals("gui") && e.getValue() instanceof LinkedHashMap) {
				for (Entry<String, ?> f : ((LinkedHashMap<String, ?>) e.getValue()).entrySet()) {
					lGuis.add((Entry<String, ArrayList<String>>) f);
				}
			}
			if (!(e.getValue() instanceof ArrayList)) continue;
			ArrayList<String> values = new ArrayList<String>();
			for (Object o : ((ArrayList<?>) e.getValue())) {
				values.add(o.toString());
			}
			if (e.getKey().equals("texture")) lTextures = values;
			else if (e.getKey().equals("shader")) lShaders = values;
			else if (e.getKey().equals("font")) lFonts = values;
		}

		TextureLoader l = Graphics.getClientTextureLoader();
		for (String s : lTextures) {
			try {
				Logger.getLogger("VC4").fine("Loading texture: " + s);
				Texture t = l.loadTexture("vc4/resources/texture/" + s);
				t.setSmooth(false);
				textures.put(s, t);
			} catch (Exception e1) {
				Logger.getLogger(getClass()).warning("Failed to load texture: \"" + s + "\"", e1);
			}
		}
		ShaderManager m = Graphics.getClientShaderManager();
		for (String s : lShaders) {
			Logger.getLogger("VC4").fine("Loading shader: " + s);
			URL base = getClass().getClassLoader().getResource("vc4/resources/shader/" + s + ".vert");
			try {
				base = new URL(base.toString().substring(0, base.toString().lastIndexOf(".")));
				m.createShader(base, s);
			} catch (Exception e1) {
				Logger.getLogger(Game.class).warning("Failed to load shader: \"" + s + "\"", e1);
			}

		}
		for (String s : lFonts) {
			fonts.put(s, new Font(s));
		}
		loadingScreen = new ClientLoadingScreen(FontRenderer.createFontRenderer("unispaced_14", 14), getWindow());
		loadingScreen.setLoadingTitle("Loading Gui:");
		new ClientComponentUtils();
		Gui.registerGuiType("menu", GuiTypeMenu.class);
		for (Entry<String, ArrayList<String>> e : lGuis) {
			ArrayList<Gui> nGuis = new ArrayList<Gui>();
			for (String s : e.getValue()) {
				InputStream i = Game.class.getClassLoader().getResourceAsStream("vc4/resources/gui/" + e.getKey() + "/" + s + ".yml");
				Gui g = new Gui((LinkedHashMap<String, ?>) yaml.load(i));
				loadingScreen.setLoadingInfo(e.getKey());
				nGuis.add(g);
			}
			boolean isMenu = e.getKey().equals("menu");
			for (Gui g : nGuis) {
				guis.put(g.getName(), g);
				if (isMenu) {
					menus.put(g.getName(), ((GuiTypeMenu) g.getType()).getState());
				}
				add(g);
			}
		}
	}

	private void loadSettings() {
		String settings = DirectoryLocator.getPath() + "/settings/";
		loadColorSchemes(settings);
		loadCrossHairs();
		loadSettingsFile(settings);
	}

	
	private void loadCrossHairs() {
		crosshairs.put("default", 0);
		crosshairs.put("circle", 1);
		crosshairs.put("target", 2);
		crosshairs.put("diagonal", 3);
	}

	/**
	 * @param settings
	 *            The settings directory
	 */
	@SuppressWarnings("unchecked")
	private void loadSettingsFile(String settings) {
		settings = settings + "settings.yml";
		Yaml yaml = ThreadYaml.getYamlForThread();
		File file = new File(settings);
		if (!file.exists()) {
			try {
				copyFile("vc4/resources/settings/settings.yml", file);
			} catch (IOException e) {
				Logger.getLogger(Game.class).warning("Failed to copy settings file into user settings", e);
				return;
			}
		}
		try {
			LinkedHashMap<String, ?> data = (LinkedHashMap<String, ?>) yaml.load(new FileInputStream(file));
			for (Entry<String, ?> e : data.entrySet()) {
				changeSetting(e.getKey(), e.getValue());
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(Game.class).warning("Failed to find settings file. How?", e);
		}
	}

	private void saveSettingsFile(String settings) {
		settings = settings + "settings.yml";
		Yaml yaml = ThreadYaml.getYamlForThread();
		File file = new File(settings);
		try {
			if (!file.exists() && !file.getParentFile().mkdirs() && !file.createNewFile()) {
				Logger.getLogger(Game.class).severe("Cannot save user settings");
			}
		} catch (IOException e) {
			Logger.getLogger(Game.class).severe("Cannot save user settings", e);
		}
		LinkedHashMap<String, Object> out = new LinkedHashMap<String, Object>();
		for (Entry<String, Setting<Object>> e : this.settings.entrySet()) {
			out.put(e.getKey(), e.getValue().get());
		}
		try {
			yaml.dump(out, new FileWriter(file));
		} catch (IOException e1) {
			Logger.getLogger(Game.class).warning("Failed to save user settings", e1);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadColorSchemes(String settings) {
		Yaml yaml = ThreadYaml.getYamlForThread();
		File clrSch = new File(settings + "colorscheme.yml");
		if (!clrSch.exists()) {
			try {
				copyFile("vc4/resources/settings/colorscheme.yml", clrSch);
			} catch (IOException e) {
				Logger.getLogger(Game.class).warning("Failed to copy ColorScheme file into user settings", e);
				return;
			}
		}
		try {
			LinkedHashMap<String, ?> data = (LinkedHashMap<String, ?>) yaml.load(new FileInputStream(clrSch));
			for (Entry<String, ?> e : data.entrySet()) {
				if (e.getValue() instanceof LinkedHashMap) {
					ColorScheme c = new ColorScheme();
					c.load((LinkedHashMap<String, ?>) e.getValue());
					colorSchemes.put(c.getName(), c);
				}
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(Game.class).warning("Failed to load colorscheme file", e);
		}
	}

	private void copyFile(String jarPath, File f) throws IOException {
		InputStream i = Game.class.getClassLoader().getResourceAsStream(jarPath);
		f.getParentFile().mkdirs();
		f.createNewFile();
		FileOutputStream o = new FileOutputStream(f);
		int b;
		while ((b = i.read()) != -1) {
			o.write(b);
		}
		o.close();
		i.close();
	}

	@Override
	public Setting<Object> getCurrentColorScheme() {
		Setting<Object> o = settings.get("colorscheme");
		return o;

	}
	
	@Override
	public Setting<Object> getCurrentCrosshair(){
		Setting<Object> o = settings.get("crosshair");
		return o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getMenuState()
	 */
	@Override
	public long getMenuState() {
		return currentMenu;
	}

	@Override
	public void changeSetting(String name, Object setting) {
		if (settings.containsKey(name)) {
			settings.get(name).set(setting);
		} else {
			settings.put(name, new Setting<Object>(setting));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getSetting(java.lang.String)
	 */
	@Override
	public Setting<Object> getSetting(String name) {
		return settings.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#printChatLine(java.lang.String)
	 */
	@Override
	public void printChatLine(String text) {
		if (chatBox != null) chatBox.addLine(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#clearChatBox()
	 */
	@Override
	public void clearChatBox() {
		if (chatBox != null) chatBox.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getLoadingScreen()
	 */
	@Override
	public LoadingScreen getLoadingScreen() {
		return loadingScreen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#getGameState()
	 */
	@Override
	public GameState getGameState() {
		return gameState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#setGameState(vc4.api.GameState)
	 */
	@Override
	public void setGameState(GameState g) {
		gameState = g;
	}
	
	/**
	 * @return the blockInteractor
	 */
	@Override
	public EntityPlayer getBlockInteractor() {
		return player;
	}

}
