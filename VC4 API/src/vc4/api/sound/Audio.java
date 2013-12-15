package vc4.api.sound;

import java.io.IOException;

import vc4.api.entity.Entity;
import vc4.api.vector.Vector3d;

/**
 * Plays sound effects and music on the client
 * 
 * @author paul
 *
 */
public abstract class Audio {

	private static Audio audio;
	
	/**
	 * Sets the audio implementation that the API is using
	 * 
	 * @param audio The new audio implementation for the api to use
	 */
	public static void setAudio(Audio audio) {
		Audio.audio = audio;
	}
	
	public abstract void aloadSound(String path) throws IOException;
	public abstract void aloadMusic(String path) throws IOException;
	public abstract void aplayMusic(String name);
	public abstract void aplayMusic(Music music);
	public abstract void aplaySound(String name, float volume, float pitch);
	public abstract void aplaySound(String name, double x, double y, double z, float volume, float pitch);
	
	/**
	 * Loads a sound effect with the given resourcedir path
	 * 
	 * @param path The path within the /sound/ directory of the resourcedir
	 * @throws IOException If there is an io error
	 */
	public static void loadSound(String path) throws IOException{
		audio.aloadSound(path);
	}
	
	/**
	 * Loads a music track with the given resourcedir path
	 * 
	 * @param path The path within the /music/ directory of the resourcedir
	 * @throws IOException If there is an io error
	 */
	public static void loadMusic(String path) throws IOException{
		audio.aloadMusic(path);
	}
	
	/**
	 * Plays a music track on the client
	 * 
	 * @param name The resourcedir path of the music track to play
	 */
	public static void playMusic(String name){
		audio.aplayMusic(name);
	}
	
	/**
	 * Plays a music object on the client.
	 * The music will only play if it has the highest priority.
	 * 
	 * @param music The music object to play
	 */
	public static void playMusic(Music music){
		audio.aplayMusic(music);
	}
	
	/**
	 * Plays a sound effect on the client
	 * 
	 * @param name The resourcedir path of the sound effect to play
	 * @param volume The volume to play the sound at
	 * @param pitch The pitch to play the sound at
	 */
	public static void playSound(String name, float volume, float pitch){
		audio.aplaySound(name, volume, pitch);
	}
	public static void playSound(String name, double x, double y, double z, float volume, float pitch){
		audio.aplaySound(name, x, y, z, volume, pitch);
	}
	public static void playSound(String name, Vector3d pos, float volume, float pitch){
		audio.aplaySound(name, pos.x, pos.y, pos.z, volume, pitch);
	}
	public static void playSound(String name, Entity e, float volume, float pitch){
		playSound(name, e.position, volume, pitch);
	}
	
}
