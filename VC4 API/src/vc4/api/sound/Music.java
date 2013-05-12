package vc4.api.sound;

public class Music {

	String path;
	MusicType type;
	
	public static final Music TITLE = new Music("Menu_Screen", MusicType.TITLE);
	public static final Music OVERWORLD = new Music("First_Day", MusicType.BIOME);
	public static final Music NONE = null;
	
	public Music(String path, MusicType type) {
		super();
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}
	
	public MusicType getType() {
		return type;
	}
}
