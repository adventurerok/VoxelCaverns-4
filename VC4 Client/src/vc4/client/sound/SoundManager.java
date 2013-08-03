package vc4.client.sound;

import java.io.IOException;
import java.net.MalformedURLException;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import vc4.api.entity.EntityLiving;
import vc4.api.logging.Logger;
import vc4.api.math.MathUtils;
import vc4.api.sound.Audio;
import vc4.api.sound.Music;

public class SoundManager extends Audio{

	protected static SoundSystem player;

	protected static long soundId;
	protected static long musicId;
	protected static int fadeTime = 2048;
	
	static String musicSource;
	static Music musicObject;
	static String musicPath;


	protected static SoundCollection sounds = new SoundCollection("sounds");
	protected static SoundCollection music = new SoundCollection("music");
	
	

	public static void init() {
		try {
			new SoundManager();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			player = new SoundSystem();
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//SFXLoader.loadGameSFX();

	}

	public SoundManager() {
		setAudio(this);
	}
	
	public static void dispose() {
		player.cleanup();
	}
	
	public static void loadSound(String path) throws MalformedURLException{
		SoundGroup sGroup = new SoundGroup(path, "sound");
		sounds.addSounds(sGroup);
	}

	public static void loadMusic(String path) throws MalformedURLException{
		SoundGroup sGroup = new SoundGroup(path, "music");
		music.addSounds(sGroup);
	}
	
	
	public static void playMusic(String name){
		if(name == null){
			if(musicSource != null) player.fadeOut(musicSource, null, null, fadeTime);
			musicPath = musicSource = null;
			musicObject = null;
			return;
		}
		if(name.equals(musicPath)) return;
		Sound sound = music.getRandomSound(name);
		if (sound == null) return;
		if(musicPath != null){
			player.fadeOutIn(musicSource, sound.location, sound.location.toString(), fadeTime, fadeTime);
			musicPath = name;
		} else {
			musicSource = "MUSIC_" + soundId;
			musicPath = name;
			++musicId;
			player.newStreamingSource(true, musicSource, sound.location, sound.location.toString(), true, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
			player.play(musicSource);
		}
	}
	
	

	public static void playSound(String name, float volume, float pitch) {
		//if (VoxelCaverns.getGameOptions().soundVolume < 0.0001F) return;

		Sound sound = sounds.getRandomSound(name);
		if (sound == null){
			Logger.getLogger("VC4").info("No sound found: " + name);
			return;
		}

		String source = "SOURCE_" + soundId;
		++soundId;
		player.newSource(false, source, sound.location, sound.location.toString(), false, 0, 0, 0, 0, 0);
		player.setPitch(source, pitch);
		float vol = volume;// * VoxelCaverns.getGameOptions().soundVolume;
		if (volume > 1.0F) vol = 1;//VoxelCaverns.getGameOptions().soundVolume;
		player.setVolume(source, vol);
		player.play(source);
	}

	public static void playSound(String name, double x, double y, double z, float volume, float pitch) {
		//if (VoxelCaverns.getGameOptions().soundVolume < 0.0001F) return;

		Sound sound = sounds.getRandomSound(name);
		if (sound == null){
			Logger.getLogger("VC4").info("No sound found: " + name);
			return;
		}

		String source = "SOURCE_" + soundId;
		++soundId;
		float rolloff = 16F;
		if (volume > 1.0F) rolloff *= volume;
		int i = SoundSystemConfig.ATTENUATION_LINEAR;
		player.newSource(volume > 1.0F ? true : false, source, sound.location, sound.location.toString(), false, (float) x, (float) y, (float) z, i, rolloff);
		player.setPitch(source, pitch);
		float vol = volume; //* VoxelCaverns.getGameOptions().soundVolume;
		if (volume > 1.0F) vol = 1;//VoxelCaverns.getGameOptions().soundVolume;
		player.setVolume(source, vol);
		player.play(source);
	}

	public static void setListener(EntityLiving l) {
		//if (VoxelCaverns.getGameOptions().soundVolume < 0.0001F) return;
		double posX = l.position.x;
		double posY = l.position.y;
		double posZ = l.position.z;
		player.setListenerPosition((float) posX, (float) posY, (float) posZ);
		float yaw = (float) l.moveYaw;
		float xAngle = MathUtils.cos(-yaw * 0.01745329F - (float) Math.PI);
		float yAngle = MathUtils.sin(-yaw * 0.01745329F - (float) Math.PI);
		player.setListenerOrientation(-yAngle, 0.0F, -xAngle, 0, 1, 0);
		return;
	}

	@Override
	public void aloadSound(String path) throws IOException {
		loadSound(path);
		
	}

	@Override
	public void aloadMusic(String path) throws IOException {
		loadMusic(path);
		
	}

	@Override
	public void aplayMusic(String name) {
		playMusic(name);
		
	}

	@Override
	public void aplaySound(String name, float volume, float pitch) {
		playSound(name, volume, pitch);
		
	}

	@Override
	public void aplaySound(String name, double x, double y, double z, float volume, float pitch) {
		playSound(name, x, y, z, volume, pitch);
		
	}
	
	@Override
	public void aplayMusic(Music music) {
		if(musicObject != null && !music.getType().canOverride(musicObject.getType())) return;
		if(music == null){
			if(musicSource != null) player.stop(musicSource);
			return;
		}
		playMusic(music.getPath());
		musicObject = music;
	}
}
