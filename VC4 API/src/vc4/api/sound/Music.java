package vc4.api.sound;

public class Music {

	String path;
	MusicType type;
	
	public static final Music TITLE = new Music("First_Day", MusicType.TITLE);
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
