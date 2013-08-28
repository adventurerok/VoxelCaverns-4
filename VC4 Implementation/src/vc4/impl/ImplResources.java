package vc4.impl;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import vc4.api.Resources;
import vc4.api.font.Font;
import vc4.api.graphics.Graphics;
import vc4.api.graphics.shader.ShaderManager;
import vc4.api.graphics.texture.*;
import vc4.api.logging.Logger;
import vc4.api.model.Model;
import vc4.api.sound.Audio;
import vc4.api.yaml.ThreadYaml;
import vc4.impl.plugin.PluginManager;

public class ImplResources extends Resources {
	
	private HashMap<String, AnimatedTexture> animatedTextures = new HashMap<>();
	private HashMap<String, SheetTexture> sheetTextures = new HashMap<>();
	private HashMap<String, Font> fonts = new HashMap<>();
	private HashMap<String, Model> models = new HashMap<>();
	
	public HashMap<String, AnimatedTexture> getAnimatedTextures() {
		return animatedTextures;
	}
	
	public HashMap<String, SheetTexture> getSheetTextures() {
		return sheetTextures;
	}
	
	public HashMap<String, Font> getFonts() {
		return fonts;
	}

	public ImplResources() {
		_res = this;
	}
	
	@Override
	public AnimatedTexture agetAnimatedTexture(String name) {
		return animatedTextures.get(name);
	}

	@Override
	public SheetTexture agetSheetTexture(String name) {
		return sheetTextures.get(name);
	}

	@Override
	public Font agetFont(String name) {
		return fonts.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws IOException{
		ArrayList<String> lSheetTextures = new ArrayList<String>();
		ArrayList<String> lSounds = new ArrayList<>();
		ArrayList<String> lMusic = new ArrayList<>();
		ArrayList<String> lAnimatedTextures = new ArrayList<String>();
		ArrayList<String> lShaders = new ArrayList<String>();
		ArrayList<String> lFonts = new ArrayList<String>();
		ArrayList<String> lModels = new ArrayList<String>();
		ArrayList<Entry<String, ArrayList<String>>> lGuis = new ArrayList<Entry<String, ArrayList<String>>>();
		Yaml yaml = ThreadYaml.getYamlForThread();
		for(URL url : PluginManager.getResourceURLs()){
			try{
				String refConstruct = url.toString();
				if(!refConstruct.endsWith("/")) refConstruct = refConstruct + "/";
				refConstruct = refConstruct + "resources.yml";
				URL ref = new URL(refConstruct);
				LinkedHashMap<String, Object> doc = (LinkedHashMap<String, Object>) yaml.load(ref.openStream());
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
					if (e.getKey().equals("sheettexture")) lSheetTextures.addAll(values);
					else if(e.getKey().equals("animatedtexture")) lAnimatedTextures.addAll(values);
					else if (e.getKey().equals("shader")) lShaders.addAll(values);
					else if (e.getKey().equals("font")) lFonts.addAll(values);
					else if (e.getKey().equals("music")) lMusic.addAll(values);
					else if (e.getKey().equals("sound")) lSounds.addAll(values);
					else if (e.getKey().equals("model")) lModels.addAll(values);
				}
			} catch(IOException e){}
		}
		SheetTextureLoader l = Graphics.getSheetLoader();
		for (String s : lSheetTextures) {
			try {
				Logger.getLogger("VC4").fine("Loading  sheet texture: " + s);
				SheetTexture t = l.loadTexture(s);
				t.setSmooth(false);
				sheetTextures.put(s, t);
			} catch (Exception e1) {
				Logger.getLogger(getClass()).warning("Failed to load texture: \"" + s + "\"", e1);
			}
		}
		AnimatedTextureLoader al = Graphics.getAnimatedLoader();
		for (String s : lAnimatedTextures) {
			try {
				Logger.getLogger("VC4").fine("Loading animated texture: " + s);
				AnimatedTexture t = al.loadTexture(s);
				t.setSmooth(false);
				animatedTextures.put(s, t);
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
				Logger.getLogger(ImplResources.class).warning("Failed to load shader: \"" + s + "\"", e1);
			}

		}
		for(String s : lModels){
			Logger.getLogger("VC4").fine("Loading model: " + s);
			models.put(s, Model.loadModel(s));
		}
		for(String s : lSounds){
			Logger.getLogger("VC4").fine("Loading sound: " + s);
			Audio.loadSound(s);
		}
		for(String s : lMusic){
			Logger.getLogger("VC4").fine("Loading music: " + s);
			Audio.loadMusic(s);
		}
		for (String s : lFonts) {
			Logger.getLogger("VC4").fine("Loading font: " + s);
			fonts.put(s, new Font(s));
		}
		loadGui(lGuis);
	}

	protected void loadGui(ArrayList<Entry<String, ArrayList<String>>> lGuis) {}

	@Override
	public List<URL> agetResourceURLs() {
		return PluginManager.getResourceURLs();
	}

	@Override
	public Model agetModel(String name) {
		return models.get(name);
	}
	

}
