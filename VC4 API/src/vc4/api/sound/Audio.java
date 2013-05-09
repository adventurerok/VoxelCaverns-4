package vc4.api.sound;

import java.io.IOException;

public abstract class Audio {

	private static Audio audio;
	
	public static void setAudio(Audio audio) {
		Audio.audio = audio;
	}
	
	public abstract void aloadSound(String path) throws IOException;
	public abstract void aloadMusic(String path) throws IOException;
	public abstract void aplayMusic(String name);
	public abstract void aplayMusic(Music music);
	public abstract void aplaySound(String name, float volume, float pitch);
	public abstract void aplaySound(String name, double x, double y, double z, float volume, float pitch);
	
	public static void loadSound(String path) throws IOException{
		audio.aloadSound(path);
	}
	public static void loadMusic(String path) throws IOException{
		audio.aloadMusic(path);
	}
	public static void playMusic(String name){
		audio.aplayMusic(name);
	}
	public static void playMusic(Music music){
		audio.aplayMusic(music);
	}
	public static void playSound(String name, float volume, float pitch){
		audio.aplaySound(name, volume, pitch);
	}
	public static void playSound(String name, double x, double y, double z, float volume, float pitch){
		audio.aplaySound(name, x, y, z, volume, pitch);
	}
	
}
