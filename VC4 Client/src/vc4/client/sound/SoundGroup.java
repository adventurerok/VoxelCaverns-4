package vc4.client.sound;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import vc4.api.Resources;
import vc4.api.util.FileSorter;

public class SoundGroup {

	protected static Random rand = new Random();

	public String name;

	public ArrayList<Sound> sounds = new ArrayList<Sound>();

	public SoundGroup(String path, String type) throws MalformedURLException {
		this.name = path;
		for (URL res : Resources.getResourceURLs()) {
			URL full = new URL(res.toString() + "/" + type + "/" + path);
			ArrayList<URL> result = FileSorter.getFiles(full);
			for (URL url : result) {
				sounds.add(new Sound(name, url));
			}
		}
	}

	public Sound getRandomSound() {
		int i = sounds.size();
		if (i < 1) return null;
		if (i == 1) return sounds.get(0);
		else return sounds.get(rand.nextInt(i));
	}

	public void addSound(Sound sound) {
		sounds.add(sound);
	}

	public void merge(SoundGroup sounds) {
		this.sounds.addAll(sounds.sounds);

	}
}
