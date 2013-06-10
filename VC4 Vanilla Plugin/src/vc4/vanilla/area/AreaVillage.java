package vc4.vanilla.area;

import java.util.List;

import org.jnbt.CompoundTag;

import vc4.api.area.Area;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.util.AABB;
import vc4.api.world.World;
import vc4.vanilla.entity.EntityNpc;

public class AreaVillage extends Area {

	String villageName;
	int players;
	int villagers;
	
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	
	public String getVillageName() {
		return villageName;
	}
	
	public AreaVillage(World world) {
		super(world);
	}

	@Override
	public String getName() {
		return "vanilla.village";
	}

	public AreaVillage(World world, AABB bounds) {
		super(world, bounds);
	}
	
	@Override
	public CompoundTag getSaveCompound() {
		CompoundTag tag = super.getSaveCompound();
		tag.setString("name", villageName);
		return tag;
	}
	
	@Override
	public void loadSaveCompound(CompoundTag tag) {
		super.loadSaveCompound(tag);
		villageName = tag.getString("name", "ErrorVille");
	}
	
	@Override
	public void updateTick() {
		players = 0;
		for(EntityPlayer plr : world.getPlayers()){
			if(bounds.isVecInside(plr.position)){
				++players;
				plr.setArea("{l:area.village," + villageName + "}");
			}
		}
		villagers = 0;
		List<Entity> npcs = world.getEntitiesInBounds(bounds, EntityNpc.class);
		for(Entity e : npcs){
			((EntityNpc)e).setVillage(this);
			++villagers;
		}
	}

}
