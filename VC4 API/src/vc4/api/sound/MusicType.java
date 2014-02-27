package vc4.api.sound;

public enum MusicType {

	TITLE(0), BIOME(5), // caves and hell also count
	AREA(10),
	PLAYER(100);

	int priority;

	private MusicType(int prio) {
		priority = prio;
	}

	public int getPriority() {
		return priority;
	}

	public boolean canOverride(MusicType music) {
		return priority >= music.priority;
	}

}
