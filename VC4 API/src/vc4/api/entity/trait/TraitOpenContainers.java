package vc4.api.entity.trait;

import java.util.ArrayList;

import vc4.api.block.OpenContainer;
import vc4.api.entity.Entity;

public class TraitOpenContainers extends Trait {

	ArrayList<OpenContainer> containers = new ArrayList<>();
	
	public TraitOpenContainers(Entity entity) {
		super(entity);
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public void update() {
		ArrayList<OpenContainer> news = new ArrayList<>();
		for(OpenContainer c : containers){
			if(c.update(entity)) news.add(c);
		}
		containers = news;

	}

	@Override
	public String name() {
		return "opencontainers";
	}

	@Override
	public boolean persistent() {
		return false;
	}

}
