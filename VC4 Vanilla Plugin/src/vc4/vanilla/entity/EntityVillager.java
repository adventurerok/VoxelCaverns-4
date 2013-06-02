package vc4.vanilla.entity;

import org.jnbt.CompoundTag;

import vc4.api.entity.EntityLiving;
import vc4.api.world.World;

public class EntityVillager extends EntityLiving {

	
	
	String firstName = "Unnamed";
	String lastName =  "Villager";
	boolean man = true;
	
	public void setMan(boolean man) {
		this.man = man;
	}
	
	public EntityVillager(World world) {
		super(world);
		
	}

	@Override
	public String getName() {
		return "vanilla.villager";
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getSkin(){
		return man ? "villager" : "villagergirl";
	}
	
	@Override
	public void draw() {
		renderHumanModel(getSkin(), firstName + " " + lastName); 
	}
	
	
	@Override
	public CompoundTag getSaveCompound() {
		CompoundTag tag = super.getSaveCompound();
		tag.setString("firstName", firstName);
		tag.setString("lastName", lastName);
		tag.setBoolean("man", man);
		return tag;
	}
	
	@Override
	public void loadSaveCompound(CompoundTag tag) {
		super.loadSaveCompound(tag);
		firstName = tag.getString("firstName", "Unnamed");
		lastName = tag.getString("lastName", "Villager");
		man = tag.getBoolean("man", true);
	}
	
	@Override
	public void update() {
		if(collisionHorizontal){
			jump();
			if(rand.nextInt(5) == 0) yaw = rand.nextInt(360) + rand.nextDouble();
		}
		walk(1, 0);
		super.update();
	}

}
