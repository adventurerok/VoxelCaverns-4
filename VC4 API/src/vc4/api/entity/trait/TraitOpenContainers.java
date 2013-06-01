package vc4.api.entity.trait;

import java.util.ArrayList;

import vc4.api.block.OpenContainer;
import vc4.api.entity.Entity;
import vc4.api.tileentity.TileEntityContainer;

public class TraitOpenContainers extends Trait {

	ArrayList<OpenContainer> containers = new ArrayList<>();
	
	public TraitOpenContainers(Entity entity) {
		super(entity);
		// TASK Auto-generated constructor stub
	}
	
	public ArrayList<OpenContainer> getContainers() {
		return containers;
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
	
	public void open(TileEntityContainer cont){
		containers.add(new OpenContainer(cont));
	}

	@Override
	public boolean persistent() {
		return false;
	}

}
