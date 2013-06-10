package vc4.vanilla.npc;

import java.util.ArrayList;
import java.util.Random;

import vc4.vanilla.Vanilla;

public class Trade {

	static Trade[] trades = new Trade[1024];
	
	static ArrayList<Trade> list = new ArrayList<>();
	
	public String name;
	public int id;
	String skin;
	
	public Trade(String name) {
		this(name, name);
	}
	
	public int getId() {
		return id;
	}
	
	public String getSkin() {
		return skin;
	}
	
	public String getName() {
		return name;
	}

	public Trade(String name, String skinName) {
		super();
		this.name = name;
		this.skin = skinName;
		id = Vanilla.getRegisteredTrade(name);
		trades[id] = this;
	}
	
	public Trade addToList(){
		if(!list.contains(this)) list.add(this);
		return this;
	}
	
	public static int random(Random rand){
		return list.get(rand.nextInt(list.size())).getId();
	}
	
	public static Trade byId(int id){
		return trades[id];
	}
	
	public static Trade byName(String name){
		return trades[Vanilla.getRegisteredTrade(name)];
	}
	
	
}
