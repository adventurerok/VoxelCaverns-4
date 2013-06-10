package vc4.vanilla.npc;

import java.util.Random;

public class Names{
	String[] start, middle, end;

	public Names(String[] start, String[] middle, String[] end) {
		super();
		this.start = start;
		this.middle = middle;
		this.end = end;
	}
	
	public String getRandomName(Random rand){
		return start[rand.nextInt(start.length)] + middle[rand.nextInt(middle.length)] + end[rand.nextInt(end.length)];
	}
	
	public int maxNames(){
		return start.length * middle.length * end.length;
	}
	
	
}