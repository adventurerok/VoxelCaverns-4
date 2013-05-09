package vc4.client.sound;

import java.util.HashMap;

public class SoundCollection {

	public String name;

	public HashMap<String, SoundGroup> sounds = new HashMap<String, SoundGroup>();

	public SoundCollection(String name) {
		this.name = name;
	}

	public void addSounds(SoundGroup sounds) {
		if(this.sounds.containsKey(sounds.name)){
			SoundGroup other = this.sounds.get(sounds.name);
			other.merge(sounds);
		}
		this.sounds.put(sounds.name, sounds);
	}



	public SoundGroup getSounds(String name) {
		return sounds.get(name);
	}

	public Sound getRandomSound(String name) {
		SoundGroup g = sounds.get(name);
		if (g == null) return null;
		else return g.getRandomSound();
	}
}
