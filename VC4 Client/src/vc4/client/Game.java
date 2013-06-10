/**
 * 
 */
package vc4.client;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.yaml.snakeyaml.Yaml;

import vc4.api.GameState;
import vc4.api.Resources;
import vc4.api.client.*;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.MovementStyle;
import vc4.api.graphics.*;
import vc4.api.gui.*;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.*;
import vc4.api.logging.Level;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.api.sound.Audio;
import vc4.api.sound.Music;
import vc4.api.text.Localization;
import vc4.api.util.*;
import vc4.api.vector.Vector2f;
import vc4.api.vector.Vector3d;
import vc4.api.yaml.ThreadYaml;
import vc4.client.camera.PlayerController;
import vc4.client.gui.*;
import vc4.client.sound.SoundManager;
import vc4.impl.plugin.PluginLoader;
import vc4.impl.world.ImplWorld;

/**
 * @author paul
 * 
 */
public class Game extends Component implements ClientGame {

	private static OpenGL gl;
	private static Vector3d up = new Vector3d(0, 1, 0);

	private Vector2f nearFar = new Vector2f(0.1F, 476F);
	private Window window;
	private LinkedHashMap<String, Cursor> cursors = new LinkedHashMap<>();
	private LinkedHashMap<String, ColorScheme> colorSchemes = new LinkedHashMap<String, ColorScheme>();
	private LinkedHashMap<String, Integer> crosshairs = new LinkedHashMap<>();
	private LinkedHashMap<String, Setting<Object>> settings = new LinkedHashMap<String, Setting<Object>>();
	HashMap<String, Long> menus = new HashMap<String, Long>();
	HashMap<String, Screen> guis = new HashMap<String, Screen>();
	ClientLoadingScreen loadingScreen;
	private GameState gameState = GameState.MENU;
	private EntityPlayer player;
	private IngameGui ingameGui;
	private ClientResources resources;
	private long previousTenTicks = 0;
	private Fustrum fustrum = new Fustrum();
	

	/**
	 * @return the paused
	 */
	@Override
	public boolean isPaused() {
		return player.isPaused();
	}
	
	@Override
	public void addCursor(Cursor cursor){
		cursors.put(cursor.getName(), cursor);
	}
	
	@Override
	public Cursor getCursor(String name){
		return cursors.get(name);
	}

	/**
	 * @param paused
	 *            the paused to set
	 */
	@Override
	public void setPaused(boolean paused) {
		player.setPaused(paused);
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
	
	private Cursor cursor;

	ChatBox chatBox;
	
	@Override
	public Component getHoveringComponent() {
		return _currentc;
	}

	/**
	 * 
	 */
	public Game(Window window) {
		Client.setGame(this);
		this.window = window;
	}
	
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#draw()
	 */
	@Override
	public void draw() {
		long tenTicks = System.nanoTime() / 100000000;
		if (tenTicks != previousTenTicks) {
			resources.animatedTextureTick((int) tenTicks);
			previousTenTicks = tenTicks;
		}
		if (gameState == GameState.SINGLEPLAYER) {
			getWindow().enterRenderMode(RenderType.GAME);
			calculateFustrum();
			camera.rotate();
			world.drawBackground(player);
			camera.translate();
			world.draw(camera.getPosition());
			player.drawCube();
		}
		gl.disable(GLFlag.DEPTH_TEST);
		getWindow().enterRenderMode(RenderType.GUI);
		if (gameState == GameState.SINGLEPLAYER && !isPaused()) {
			Graphics.getClientShaderManager().bindShader("texture");
			Resources.getSheetTexture("crosshair").bind();
			int w = getWindow().getWidth() / 2 - 64;
			int h = getWindow().getHeight() / 2 - 64;
			int tex = getCrosshair(getCrosshairSetting().getString());
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
		if(!Mouse.isGrabbed()){
			Color col = Color.green;
			Graphics.getClientShaderManager().bindShader("texture");
			Resources.getAnimatedTexture("cursor").bind();
			int ind = Resources.getAnimatedTexture("cursor").getArrayIndex(cursor.getName());
			float mx = Input.getClientMouse().getX();
			float my = Input.getClientMouse().getY();
			mx -= cursor.getX();
			my -= cursor.getY();
			gl.begin(GLPrimative.QUADS);
			gl.color(col);
			gl.texCoord(0.001, 0.001, ind);
			gl.vertex(mx, my);
			gl.texCoord(0.999, 0.001, ind);
			gl.vertex(mx + 16, my);
			gl.texCoord(0.999, 0.999, ind);
			gl.vertex(mx + 16, my + 16);
			gl.texCoord(0.001, 0.999, ind);
			gl.vertex(mx, my + 16);
			gl.end();
			Graphics.getClientShaderManager().unbindShader();
		}
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#update()
	 */
	@Override
	public void update() {
		getCursor("pointer").bind();
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
				_currentc.bringToFront();
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
			if (_currentc != null){
				_currentc.bringToFront();
				_currentc.fireMouseEvent("pressed", 1, x, y);
			}
		} else if (mouseSet.buttonReleased(1)) {
			if (_mouseupc != _currentc) _currentc.fireMouseEvent("released", 1, x, y);
			if (_currentc != null) _currentc.fireMouseEvent("released", 1, x, y);

			if (_oldc != _currentc) _oldc.setFocus(false);
			_currentc.setFocus(true);

			if (_currentc != null) _currentc.fireMouseEvent("clicked", 1, x, y);
		}
		super.update();
		if (gameState == GameState.SINGLEPLAYER) {
			long oldTime = world.getTime();
			String oldArea = player.getArea();
			player.setArea("{l:area.wilderness}");
			SoundManager.setListener(player);
			if (Input.getClientKeyboard().keyPressed(Key.ESCAPE)) setPaused(!isPaused());
			camera.handleInput(delta);
			player.decreaseCooldown(delta);
			player.rayTrace(camera.getLook(), 10);
			if (!isPaused()) {
				if (mouseSet.getCurrent().leftButtonPressed()) player.leftMouseDown(delta);
				if (mouseSet.getCurrent().rightButtomPressed()) player.rightMouseDown(delta);
			}
			player.updateInput();
			world.update(camera.getPosition(), delta);
			Audio.playMusic(world.getMusic(player));
			Keyboard keys = Input.getClientKeyboard();
			if(keys.keyPressed(Key.C)) ingameGui.toggleVisibility("crafting");
			if(keys.keyPressed(Key.X)) ingameGui.toggleVisibility("armour");
			if(keys.keyPressed(Key.Z)) ingameGui.toggleVisibility("game");
			if(oldTime != world.getTime() && oldArea != player.getArea()){
				printChatLine("{l:area.enter} " + player.getArea());
			}
		}
		if(Input.getClientKeyboard().keyPressed(Key.F6)){
			JOptionPane.showMessageDialog(null, "Hovering: " + _currentc.toString());
		}
		if (Input.getClientKeyboard().keyReleased(Key.F2)) {
			takeScreenshot();
		}
		long time = System.nanoTime() - _lastFrame;
		_lastFrame = System.nanoTime();
		if (time > 0) delta = time / 1000000D;
	}

	@Override
	public String takeScreenshot() {
		int startX = 0;
		int startY = 0;
		int width = (int) window.getWidth();
		int height = (int) window.getHeight();
		GL11.glReadBuffer(GL11.GL_FRONT);
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(Display.getWidth() * Display.getHeight() * bpp);
		// ByteBuffer buffer = BufferUtils.createByteBuffer(Display.getWidth() * Display.getHeight() * bpp);
		GL11.glReadPixels(0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		String time = sdf.format(cal.getTime());
		String path = DirectoryLocator.getPath();
		File file = new File(path + "/screenshots/" + time + ".png"); // The file to save to.
		new File(path + "/screenshots").mkdirs();

		try {
			file.createNewFile();
			Logger.getLogger("VC4").info("Screenshot saved at: " + "{c:e}" + file.getPath());
			// if (player != null) player.message("Screenshot saved at: " + "{c:e}" + file.getPath());
		} catch (IOException e1) {
			Logger.getLogger("Screenshots").log(Level.WARNING, e1);
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = startX; x < startX + width; x++)
			for (int y = startY; y < startY + height; y++) {
				int i = (x + (Display.getWidth() * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x - startX, height - (y + 1) - startY, (0xFF << 24) | (r << 16) | (g << 8) | b);
			}

		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ImageIO.write(image, "PNG", fileOut);
			fileOut.close();

		} catch (IOException e) {
			Logger.getLogger("Screenshots").log(Level.WARNING, e);
		}

		return file.getPath();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#load()
	 */
	@Override
	public void load() {
		try {
			org.lwjgl.input.Cursor none = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), BufferUtils.createIntBuffer(1));
			Mouse.setNativeCursor(none);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		setBounds(getBounds());
		SoundManager.init();
		PluginLoader.loadAndEnablePlugins();
		loadResources();
		loadSettings();
		addCursor(new ClientCursor("pointer"));
		addCursor(new ClientCursor("resize_ns", 8, 8));
		addCursor(new ClientCursor("resize_we", 8, 8));
		addCursor(new ClientCursor("resize_swne", 8, 8));
		addCursor(new ClientCursor("resize_nwse", 8, 8));
		addCursor(new ClientCursor("move", 8, 8));
		addCursor(new ClientCursor("precision", 8, 8));
		cursor = getCursor("pointer");
		currentMenu = menus.get("main");
		add(chatBox = new ChatBox());
		world = new ImplWorld("World");
		try {
			world.saveInfo();
		} catch (IOException e) {
			Logger.getLogger(Game.class).warning("Exception occured", e);
		}
		player = world.loadPlayer("player");
		camera = new PlayerController(player);
		gl = Graphics.getOpenGL();
		ingameGui = new IngameGui();
		add(ingameGui);
		ingameGui.resized();
		Audio.playMusic(Music.TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.client.ClientGame#unload()
	 */
	@Override
	public void unload() {
		world.savePlayer(player);
		world.unload();
		saveSettingsFile(DirectoryLocator.getPath() + "/settings/");
		SoundManager.dispose();
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

	public Vector2f getNearFar() {
		return nearFar;
	}

	/**
	 * @return the window
	 */
	@Override
	public Window getWindow() {
		return window;
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
		} else if (action.startsWith("setting:")) {
			String type = action.substring(8);
			if (type.equals("colorscheme:next")) {
				List<String> list = new ArrayList<String>(colorSchemes.keySet());
				int n = list.indexOf(getColorSchemeSetting().getString()) + 1;
				if (n >= list.size()) n = 0;
				changeSetting("colorscheme", list.get(n));
			} else if (type.equals("colorscheme:last")) {
				List<String> list = new ArrayList<String>(colorSchemes.keySet());
				int n = list.indexOf(getColorSchemeSetting().getString()) - 1;
				if (n < 0) n = list.size() - 1;
				changeSetting("colorscheme", list.get(n));
			} else if (type.equals("crosshair:next")) {
				List<String> list = new ArrayList<String>(crosshairs.keySet());
				int n = list.indexOf(getCrosshairSetting().getString()) + 1;
				if (n >= list.size()) n = 0;
				changeSetting("crosshair", list.get(n));
			} else if (type.equals("crosshair:last")) {
				List<String> list = new ArrayList<String>(crosshairs.keySet());
				int n = list.indexOf(getCrosshairSetting().getString()) - 1;
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
	public int getCrosshair(String name) {
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
		List<String> list = new ArrayList<String>(resources.getFonts().keySet());
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			result[i] = list.get(i);
		}
		return result;
	}

	private void loadResources() {
		try {
			resources = (ClientResources) Resources.getRes();
			resources.load();
		} catch (IOException e) {
			Logger.getLogger(Game.class).warning("Exception occured", e);
		}
		Localization.loadLocalization("en_GB");
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
	public Setting<Object> getColorSchemeSetting() {
		Setting<Object> o = settings.get("colorscheme");
		return o;

	}

	@Override
	public Setting<Object> getCrosshairSetting() {
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

	public void calculateFustrum() {
		Vector2f nf = getNearFar();
		fustrum.setCamInternals(getFieldOfVision(), window.getAspectRatio(), nf.x, nf.y);
		Vector3d p = camera.getPosition();
		Vector3d l = p.add(camera.getLook());
		fustrum.setCamDef(p, l, up);
	}

	/**
	 * @return the blockInteractor
	 */
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	public float getFieldOfVision() {
		if (player != null && player.getMovement() == MovementStyle.SPRINT) return 80;
		else return 70;
	}

	@Override
	public Fustrum getViewFustrum() {
		return fustrum;
	}

	@Override
	public void handlePacket(Packet pack) {
		
		
	}

	@Override
	public ColorScheme getCurrentColorScheme() {
		return getColorScheme(getColorSchemeSetting().getString());
	}

}
